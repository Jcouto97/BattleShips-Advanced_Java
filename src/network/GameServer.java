package network;

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

    // JOAO
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

    // JOAO
    /*
    Server socket accepts the players socket;
    Created new Player with name (using numOfConnections) and his socket;
    Invoke addPlayer function (below) on this new PlayerHandler instance;
     */
    public void acceptConnection(int numberOfConnections) {
        try {
            Socket playerSocket = serverSocket.accept();
            addPlayer(new PlayerHandler("Player -".concat(String.valueOf(numberOfConnections)), playerSocket));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // JOAO
    /*
    The new PlayerHandler instance will be added to the player list;
    It's runnable will be submitted to the thread pool
     */
    public void addPlayer(PlayerHandler player) {
        playerList.add(player);
        service.submit(player);
        System.out.println(player.getName() + " joined the game!");
    }

    // Objecto que guarda informação do cliente (Nome, Socket, etc)
   public class PlayerHandler implements Runnable {
        private String name;
        private Board board;
        private Socket playerSocket;
        private BufferedWriter writer;
        private BufferedReader reader;
        private String message;


        public PlayerHandler(String name, Socket playerSocket) throws IOException {
            this.name = name;
            this.board = board;
            this.playerSocket = playerSocket;
            this.writer = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
        }


        @Override
        public void run() {
            while (!playerSocket.isClosed()) {
                try {
                    this.message = reader.readLine();//o que vem do player
                    send(message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        /*public void isCommand() {

        }*/

        // NUNO
        public void send(String message) {
            try {
                writer.write(message);
                writer.newLine();
                writer.flush();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void close() {
            try {
                playerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /*public void dealWithCommand(){

        }*/

        public String getMessage() {
            return message;
        }

        // Recebe toda a informação do player

        public String getName() {
            return name;
        }


        private boolean isCommand(String message) {
            return false;
        }

        public Socket getPlayerSocket() {
            return playerSocket;
        }
    }
}

