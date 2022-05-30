import network.GameServer;

public class ServerLauncher {
    public static void main(String[] args) {
        //iniciar server
        GameServer gameServer = new GameServer(8082);
        gameServer.start();

    }
}
