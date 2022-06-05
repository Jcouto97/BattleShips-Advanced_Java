import java.io.*;
import java.net.Socket;

/**
 * This class will create players
 */
public class Player {
    public Player() {
    }

    /**
     * This method establishes a connections between the player and the server by creating a server socket
     * It creates a new Thread for each KeyboardHandler instance
     *
     *
     * @param host This is an IP address, which is the server
     * @param port This is the port that will be generated automatically by the server
     */
    public void start(String host, int port) throws IOException {
        Socket socket = new Socket(host, port); //criar thread (cada vez que um player se connecta, cria uma nova)
        new Thread(new KeyboardHandler(socket)).start(); //iniciar thread
    }

    /**
     *  Connecting the player to a server
     *
     */
    public static void main(String[] args) {
        Player player = new Player();
        try {
            player.start("localhost", 8082);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This class reads/writes what the player types onto the console
     */
    private static class KeyboardHandler implements Runnable {
        private BufferedWriter writer;
        private BufferedReader reader;
        private BufferedReader consoleReader;
        private Socket keyboardSocket;

        /**
         * This is the constructor of the class KeyboardHandler that receives a Socket
         */
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

        /**
         * This method is used to create a Thread and implement the another one from Runnable
         * The first Thread (independent) is an independent one that was created to loop and read the player input (readLine)
         * The second Thread (runnable) was created to write the players output
         */
        @Override
        public void run() {
            new Thread(() -> {
                while(!keyboardSocket.isClosed()) {
                    try {
                        String message = consoleReader.readLine();
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

            //Thread from run to write players output
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