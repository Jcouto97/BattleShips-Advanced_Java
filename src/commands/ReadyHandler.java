package commands;

import game.GameServer;

public class ReadyHandler implements CommandHandler {
    /*
    Sets ready boolean to true in GameServer
     */
    @Override
    public void command(GameServer.PlayerHandler player, GameServer server) {
        player.setReady(true);
    }
}
