package commands;

import game.GameServer;

public interface CommandHandler {
    /**
     * method signature
     */
    void command(GameServer.PlayerHandler player, GameServer server);
}
