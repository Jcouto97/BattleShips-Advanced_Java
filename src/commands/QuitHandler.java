package commands;

import network.GameServer;

public class QuitHandler implements CommandHandler{
    @Override
    public void command(GameServer.PlayerHandler player, GameServer server) {
        player.close();
    }
}
