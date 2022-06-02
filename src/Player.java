import utils.LoadingAnimation;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

// LUIS
public class Player {

    public Player() {
    }

    /*
    Creating server socket
    Creates a new thread for each KeyboardHandler (player I/O)
     */
    public void start(String host, int port) throws IOException {
        Socket socket = new Socket(host, port); //criar thread (cada vez que um player se connecta, cria uma nova)
        new Thread(new KeyboardHandler(socket)).start(); //iniciar thread
    }

    /*
    Connecting the player to a server
     */
    public static void main(String[] args) {
        Player player = new Player();

        try {
            player.start("localhost", 8082);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class KeyboardHandler implements Runnable {
        private BufferedWriter writer;
        private BufferedReader reader;
        private BufferedReader consoleReader;
        private Socket keyboardSocket;

        public KeyboardHandler(Socket socket) {
            try {
                this.reader = new BufferedReader(new InputStreamReader(System.in));
                this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.consoleReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.keyboardSocket = socket;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        /*
        Thread to read players input
         */
        @Override
        public void run() {
            //Read Thread
            new Thread(() -> {
                while(!keyboardSocket.isClosed()) { // loop para estar sempre a fazer o readLine (lê o input do player)
                    try {

                        String message = consoleReader.readLine();
                        // condição para quando tivermos o "QuitHandle" a funcionar
                        if (message == null) {
                            keyboardSocket.close();
                            continue;
                        }
                        System.out.println(message);
                    } catch (IOException e) {
                        System.exit(1); //Terminar players se server estiver terminado
                    }
                }
                // se não fizer isto, o próximo while não correr porque fica preso até que o user submeta algo, mesmo depois de ter saído
                System.exit(0);
            }).start();

            //Thread to write players output
            while(!keyboardSocket.isClosed()) {
                try {
                    String message = reader.readLine();
                    writer.write(message);
                    writer.newLine();
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}