package network;

import field.Board;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
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
    public void start(int port){
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
    public void acceptConnection(int numberOfConnections){
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
    public void addPlayer(PlayerHandler player){
        playerList.add(player);
        service.submit(player);
        System.out.println(player.getName() + " joined the game!");
    }

    // Objecto que guarda informação do cliente (Nome, Socket, etc)
    private class PlayerHandler implements Runnable{
        private String name;
        private Board board;
        private Socket playerSocket;
        private BufferedWriter writer;
        private BufferedWriter reader;
        private String message;


        public PlayerHandler(String name, Socket playerSocket) {
            this.board = board;
            this.playerSocket = playerSocket;
            this.writer = writer;
            this.reader = reader;
            this.name = name;
        }

        /*public void isCommand() {

        }*/

        // NUNO
        public void send(String message) {

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
        @Override
        public void run() {

        }

        public String getName() {
            return name;
        }
    }
}
