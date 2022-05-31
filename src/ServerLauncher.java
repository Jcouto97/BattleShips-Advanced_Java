import network.GameServer;

public class ServerLauncher {

    /*
    Launches server
     */
    public static void main(String[] args) {

        GameServer gameServer = new GameServer();
        gameServer.start(8082);
    }
}

