import java.io.IOException;
import java.net.Socket;

public class Player {

    private String name;
    //score??

//    public static void main(String[] args) {
//        Client a = new Client();
//        try {
//            a.start("localHost", 8082);
//        } catch (IOException e) {
//            System.out.println("Chat is under maintenance");
//        }
//    }
//    public void start(String host, int port) throws IOException {
//        Socket socket = new Socket(host, port);
//        new Thread(new KeyboardHandler(socket)).start();
//    }




    public Player(String name) {
        this.name = name;
    }

    // LUIS
    private static class KeyboardHandler implements Runnable {



        @Override
        public void run() {
            //fazer threads
        }
    }


    public static void main(String[] args) {
        //iniciar coisas do player
    }
}
