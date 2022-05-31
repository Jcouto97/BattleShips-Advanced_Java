package commands;

import network.GameServer;

public interface CommandHandler {
    void command(GameServer.PlayerHandler client, GameServer server);
}
