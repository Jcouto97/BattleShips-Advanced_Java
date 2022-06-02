package network;

import commands.Command;
import field.Board;
import field.ColumnENUM;
import field.Position;
import utils.LoadingAnimation;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static utils.asciiArt.*;

public class GameServer {
    private ServerSocket serverSocket;
    private ExecutorService service;
    private List<PlayerHandler> playerList;
    private boolean isWaiting;
    private final Object lock2;
    private int gameIds;

    public GameServer() {
        this.gameIds = 1;
        this.lock2 = new Object();
        isWaiting = false;
    }

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

    /*
    Randomizes witch player starts as attacker
    If only 1 player enter it will jump to lock2.wait until 2nd player joins
    Then it will notifyAll() threads to start game
     */
    private void waitingRoom(PlayerHandler player) {
        if (isWaiting) {
            synchronized (lock2) {
                lock2.notifyAll();
            }
            chooseAttacker();
            gameIds++;
//            for (PlayerHandler playerHandler : playerList) {
//                synchronized (playerHandler.lock) {
//                    playerHandler.lock.notifyAll();
//                }
//            }
            return;
        }
        synchronized (lock2) {
            try {
                player.send("Waiting for adversary to connect!");
                isWaiting = true;
                lock2.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            isWaiting = false;
        }
    }

    private void chooseAttacker() {
        List<Integer> players = new ArrayList<>();
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).playerGameId == gameIds) {
                players.add(i);
            }
        }
        playerList.get(players.get((int) Math.floor(Math.random() * 2))).isAttacker = true;
    }

    /*
    Function used to remove player from list when quit (command)
     */
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
        private final Object lock;
        private final Object messageLock;
        private boolean loser;
        private boolean ready;
        private int maxNumberOfRandomBoards;
        private LoadingAnimation loadingAnimation;
        private int playerGameId;
        private boolean winner;

        /*
        Constructor that receives a name and a playerSocket
        Initializes:
        -A board
        -BufferedWriter + Reader
        -isAttacker (to check player that attacks)
        -Looser (check winner)
        -Lock (object used to synchronize and wait switch between turns
        -Ready (to check if players are ready to start)
         */
        public PlayerHandler(String name, Socket playerSocket) throws IOException {
            this.name = name;
            this.board = new Board();
            this.playerSocket = playerSocket;
            this.writer = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            this.isAttacker = false;
            this.loser = false;
            this.winner = false;
            this.lock = new Object();
            this.messageLock = new Object();
            this.ready = false;
            this.maxNumberOfRandomBoards = 3;
            this.loadingAnimation = new LoadingAnimation();
        }

        /*
        Checks if any of the player ship is alive
         */
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
        Deals with commands (Attack, ready, random, quit)
        After first player decides to ready or random he will be sent to the wanting room to wait 2nd player
        If not attacker, player will wait for attacker to attack (using synchronize)
        */
        @Override
        public void run() {
            startScreen();
            readyCheck();
            playerGameId = gameIds;
            while (true) {
                waitingRoom(this); //1st player will wait for the 2nd to start the game
                play();
            }
        }

        //public void message(String message){
//        if(isAttacker){
//
//        }
//}
        private void play() {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!playerSocket.isClosed()) {
                        try {

                            message = reader.readLine();
                            synchronized (messageLock) {
                                messageLock.notifyAll();
                            }
                        } catch (IOException e) {
                            try {
                                clientDC();
                                close();
                            } catch (ConcurrentModificationException j) {

                            }
                        }
                    }
                }
            }).start();
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
                    send("You are attacking, write /attack and choose your coordinates!\nFormat for coordinates is '# #', example: 'B 4'");
                    Thread waitTime = new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1000);
                                ColumnENUM firstParameter = null;
                                int secondParameter = 0;
                                Position randomPosition = null;
                                do {
                                    firstParameter = ColumnENUM.values()[(int) Math.floor(Math.random() * (ColumnENUM.values().length-1) + 1)];
                                    secondParameter = (int) Math.floor(Math.random() * (10-1) + 1);
                                    randomPosition = new Position(firstParameter.getValue(), secondParameter);
                                } while (board.getListOfPreviousAttacks().contains(randomPosition));
                                message = "/attack "+firstParameter.getLetter()+" "+secondParameter;
                                synchronized (messageLock) {
                                    messageLock.notifyAll();
                                }
                            } catch (InterruptedException e) {
                            }
                        }
                    });
                    waitTime.start();
                    synchronized (messageLock) {
                        messageLock.wait();
                    }
                    waitTime.interrupt();
                    //o que vem do player //blocking method
                    if (isCommand(message)) {
                        dealWithCommand(message);
                    } else if (!isCommand(message)) {
                        send("Not a command, try again!");
                    }
                } catch (InterruptedException ignored) {

                }
            }
        }

        private void readyCheck() {
            while (!ready) {
                try {
                    send(board.getYourBoard()); //mostra primeiro a board e depois se queres ready ou random
                    send("Write /ready to start the game!\nWrite /random for a new board!\nNumber of random boards you can still generate: " + this.maxNumberOfRandomBoards);
                    this.message = reader.readLine();
                    if (isCommand(message)) {
                        dealWithCommand(message);
                    }
                } catch (IOException e) {
                    close();
                }
            }
        }

        public void clientDC() throws ConcurrentModificationException {
            for (PlayerHandler players : playerList) {
                if (players.playerGameId == this.playerGameId && !players.name.equals(this.name)) {
                    players.send("You Win");
                    players.winner = true;
                    players.close();
                }
            }
        }

        public void loser() {
            for (PlayerHandler players : playerList) {
                if (players.playerGameId == this.playerGameId && !players.name.equals(this.name)) {
                    this.setLoser();
                    this.send("You Lose");
                    this.close();
                }
            }
        }

        /*
        Uses Ascci art from Utils class to make starting menu
         */
        private void startScreen() {
            send(BATTLESHIP + "\n");
            send(PLANE + "\n\n" + BOAT + "\n \n \t\t\t Press enter to start the game :)");
            try {
                this.message = reader.readLine();
            } catch (IOException e) {
                close();
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

        public int getMaxNumberOfRandomBoards() {
            return maxNumberOfRandomBoards;
        }

        public void setMaxNumberOfRandomBoards(int maxNumberOfRandomBoards) {
            this.maxNumberOfRandomBoards = maxNumberOfRandomBoards;
        }

        public int getPlayerGameId() {
            return playerGameId;
        }
    }
}