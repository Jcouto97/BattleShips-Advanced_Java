package network;

import field.Board;
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

    public GameServer(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.service = Executors.newCachedThreadPool();
            this.playerList = new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // JOAO
    public void start(){

    }

    // JOAO
    public void acceptConnection(){

    }

    // JOAO
    public void addPlayer(PlayerHandler player){

    }





    private class PlayerHandler{
        private Board board;
        private Socket playerSocket;
        private BufferedWriter writer;
        private BufferedWriter reader;
        private String message;


        public PlayerHandler(Board board, Socket playerSocket, String message) {
            this.board = board;
            this.playerSocket = playerSocket;
            this.writer = writer;
            this.reader = reader;
        }

        /*public void isCommand() {

        }*/

        // NUNO
        public void send() {

        }

        // NUNO
        public void close() {

        }

        /*public void dealWithCommand(){

        }*/

        public String getMessage() {
            return message;
        }
    }
}
