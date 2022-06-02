package game;

import java.io.*;
import java.net.Socket;

/**
 * Class bot is used has placeholder player
 *
 */
public class Bot implements Runnable {
    private Socket socket;
    private BufferedWriter writer;
    private BufferedReader serverReader;

    /**
     * Enters the server like a normal player but plays without input
     */
    public Bot() {
        try {
            this.socket = new Socket("localhost", 8082);
            this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.serverReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < 2; i++) {
                Thread.sleep(1000);
                writer.write("/ready\n");
                writer.newLine();
                writer.flush();
            }
            while (true) {
                if (serverReader.readLine() == null) {
                    socket.close();
                    break;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            System.exit(0);
        }
    }
}
