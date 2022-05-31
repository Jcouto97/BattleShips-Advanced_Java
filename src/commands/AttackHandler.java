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

        // if they are integers, create position of where player wants to attack
        Position hitPosition = new Position(Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2]));

        /*
        iterate through list of players and if name of attacker is different
        from name of defender, attacks his board
         */

        for (GameServer.PlayerHandler playerName : server.getPlayerList()) {
            if (!player.getName().equals(playerName.getName())) {
                playerName.getBoard().hit(hitPosition);
                playerName.send(playerName.getBoard().getBoard());
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
