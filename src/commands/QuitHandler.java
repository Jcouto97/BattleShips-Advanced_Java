package commands;

import game.GameServer;

public class QuitHandler implements CommandHandler{

    /**
     * Removes player from playerList
     * Closes his socket
     */
    @Override
    public void command(GameServer.PlayerHandler player, GameServer server) {
        player.close();
    }
}
