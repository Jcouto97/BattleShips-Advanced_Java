import game.GameServer;

/**
 * Launches server
 */
public class ServerLauncher {
    public static void main(String[] args) {
        GameServer gameServer = new GameServer();
        gameServer.start(8082);
    }
}