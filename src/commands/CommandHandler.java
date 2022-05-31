package commands;

import network.GameServer;

public interface CommandHandler {
    void command(GameServer.PlayerHandler player, GameServer server);
}
