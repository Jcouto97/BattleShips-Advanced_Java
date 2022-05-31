package commands;

import field.Position;
import network.GameServer;

public class AttackHandler implements CommandHandler {

    @Override
    public void command(GameServer.PlayerHandler player, GameServer server) {
        // divide message sent by the player into an array, to get attack coordinates after
        String[] coordinates = player.getMessage().split(" ");

        // check if what the player wrote were integers
        if (!isInt(coordinates[1]) && !isInt(coordinates[2])) {
            return;
        }

        // if they are integers, create position of where player wants to attack
        Position hitPosition = new Position(Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2]));


        /*
        iterate through list of players and if name of player1 (attacker) is different
        from name of player2 (defender), attack player2 board


        for each player from the playerList saved in the GameServer,
        check if the attacker player name is different from the defender player name
        if it is:
            attacks player2 board on the position provided; (first line of if condition)
            updates defender board (changes "~" to "." or "x" depending on if it was water or a ship) (second line of if condition)

         */

        for (GameServer.PlayerHandler player2 : server.getPlayerList()) {
            if (!player.getName().equals(player2.getName())) {
                String hit = player2.getPlayerBoard().hit(hitPosition); // player2 gets hit by player1
                player.getPlayerBoard().updateAdversaryBoard(hitPosition, hit);

                player2.send(player2.getPlayerBoard().getYourBoard());// player2 prints his board in console
                player2.send(player2.getPlayerBoard().getAdversaryBoard()); // player2 prints adversary(player1) board in console

                /*player.send(player.getPlayerBoard().getBoard()); // player1 prints his board in console
                player.send(player2.getPlayerBoard().getAdversaryBoard());*/ // player1 prints adversary(player2) board in console
            }
        }

    }

    public boolean isInt(String coordinate) {
        try {
            Integer.parseInt(coordinate);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
