package network;

import commands.Command;
import field.Board;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GameServer {
    private ServerSocket serverSocket;
    private ExecutorService service;
    private List<PlayerHandler> playerList;
    private int inc = 0;
    private final Object lock2 = new Object();

    /*
    Starts server with port as a parameter;
    Starts a thread pool with unlimited thread space;
    Starts a new list were players will be added;
    Adds number of connections of players to the server;
     */
    public void start(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.service = Executors.newCachedThreadPool();
            this.playerList = new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int numberOfConnections = 0;

        while (true) {
            ++numberOfConnections;
            acceptConnection(numberOfConnections);
        }
    }

    private void playersReady(PlayerHandler player) {
        if (inc == 2) {
            synchronized (lock2) {
                lock2.notifyAll();
            }
            playerList.get((int) Math.floor(Math.random() * 2)).isAttacker = true;
            for (PlayerHandler playerHandler : playerList) {
                synchronized (playerHandler.lock) {
                    playerHandler.lock.notifyAll();
                }
            }
            return;
        }
        synchronized (lock2) {
            try {
                player.send("Waiting for adversary to connect!");
                lock2.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void removePlayers(String name) {
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).name.equals(name)) {
                playerList.remove(i);
                return;
            }
        }
    }

    /*
    Server socket accepts the players socket;
    Created new Player with name (using numOfConnections) and his socket;
    Invoke addPlayer function (below) on this new PlayerHandler instance;
     */
    public void acceptConnection(int numberOfConnections) {
        try {
            Socket playerSocket = serverSocket.accept();
            addPlayer(new PlayerHandler("Player-".concat(String.valueOf(numberOfConnections)), playerSocket));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    The new PlayerHandler instance will be added to the player list;
    It's runnable will be submitted to the thread pool
     */
    public void addPlayer(PlayerHandler player) {
        playerList.add(player);
        service.submit(player);
        System.out.println(player.getName() + " joined the game!");
    }

    public List<PlayerHandler> getPlayerList() {
        return playerList;
    }


    /*
    Object that stores each player info
     */
    public class PlayerHandler implements Runnable {
        private final String name;
        private Board board;
        private final Socket playerSocket;
        private final BufferedWriter writer;
        private final BufferedReader reader;
        private String message;
        private boolean isAttacker;
        private final Object lock = new Object();
        private boolean loser;
        private boolean ready = false;

        public PlayerHandler(String name, Socket playerSocket) throws IOException {
            this.name = name;
            this.board = new Board();
            this.playerSocket = playerSocket;
            this.writer = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            this.isAttacker = false;
            this.loser = false;
        }

        public boolean checkIfTheresShipsAlive() {
            for (int i = 0; i < board.getAllTheShips().size(); i++) {
                if (!board.getAllTheShips().get(i).isDead()) {
                    return true;
                }
            }
            return false;
        }

        public void setLoser() {
            this.loser = true;
        }

        public boolean isAttacker() {
            return isAttacker;
        }

        public void setAttacker(boolean attacker) {
            isAttacker = attacker;
        }

        /*
        Thread that reads players messages
        Shows players boards
        Deals with commands
        */
        @Override
        public void run() {
            //ready
            //notifyAll
            //RANDOMIZE
            while (!ready) {
                try {
                    send(board.getYourBoard());
                    this.message = reader.readLine();
                    if (isCommand(message)) {
                        dealWithCommand(message);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            inc++;
            playersReady(this);
            while (!playerSocket.isClosed()) {
                try {
                    send(board.getYourBoard());
                    send(board.getAdversaryBoard());

                    while (!isAttacker) {
                        synchronized (lock) {
                            send("Waiting for adversary attack!");
                            lock.wait();
                        }
                    }
                    send("You are attacking!");

                    this.message = reader.readLine();//o que vem do player //blocking method
                    if (isCommand(message)) {
                        dealWithCommand(message);
                    } else if (!isCommand(message)) {
                        send("Not a command, try again!");
                    }
                } catch (IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        /*
        Verifies if message is a command
         */
        public boolean isCommand(String message) {
            return message.startsWith("/");
        }

        /*
        Deals with the command by spliting the command with coordinates
        Execute the command, in case it exists
         */
        public void dealWithCommand(String message) {
            String[] words = message.split(" ", 2);
            Command command = Command.getCommandFromDescription(words[0]); //para ter o /attack

            if (command == null) return;  //volta para o run

            command.getHandler().command(this, GameServer.this);  //executar o comando
        }

        /*
        Deals with buffers
         */
        public void send(String message) {
            try {
                writer.write(message);
                writer.newLine();
                writer.flush();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        /*
        Closes player socket
         */
        public void close() {
            try {
                removePlayers(this.name);
                playerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Object getLock() {
            return lock;
        }

        public String getMessage() {
            return message;
        }

        public String getName() {
            return name;
        }

        public Socket getPlayerSocket() {
            return playerSocket;
        }

        public Board getPlayerBoard() {
            return board;
        }

        public void setReady(boolean ready) {
            this.ready = ready;
        }

        public void setBoard(Board board) {
            this.board = board;
        }
    }
}