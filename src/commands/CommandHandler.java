package commands;

import game.GameServer;

public interface CommandHandler {
    void command(GameServer.PlayerHandler player, GameServer server);
}
