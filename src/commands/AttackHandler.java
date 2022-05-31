package commands;

import field.Position;
import network.GameServer;

public class AttackHandler implements CommandHandler{
    @Override
    public void command(GameServer.PlayerHandler player, GameServer server) {
        // divide message sent by the player into an array, to get attack coordinates after
        String[] coordinates = player.getMessage().split(" ");

        // check if what the player wrote were integers
        if (!isInt(coordinates[1]) && !isInt(coordinates[2])){
            return;
        }

        // if they are, create
        Position hitPosition = new Position(Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2]));

        for (GameServer.PlayerHandler playerName : server.getPlayerList()) {
            if (!player.getName().equals(playerName.getName())) {
                playerName.getBoard().hit(hitPosition);
            }
        }
    }

    public boolean isInt(String coordinate){
        try {
            Integer.parseInt(coordinate);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
