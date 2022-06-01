package commands;

import network.GameServer;

import java.util.Objects;

public class ReadyHandler implements CommandHandler{
    @Override
    public void command(GameServer.PlayerHandler player, GameServer server) {
                player.setReady(true);
    }
}
