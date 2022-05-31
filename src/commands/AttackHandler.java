package commands;

import field.Position;
import network.GameServer;

public class AttackHandler implements CommandHandler {

    /*
    Divide message sent by the player into an array, to get attack coordinates after;
    Check if what the player wrote were integers;
    Create position where player wants to attack;
    Iterate through list of players and if name of attacker is different
    from name of defender, attack defenders board. Else attacker would attack himself also;
    If so, defender gets hit by attacker;
    Update the attackers enemy board;
    Redraws both of the defender boards (attacker and defender);
     */

    @Override
    public void command(GameServer.PlayerHandler attacker, GameServer server) {
        String[] coordinates = attacker.getMessage().split(" ");

        if (!isInt(coordinates[1]) || !isInt(coordinates[2])) {
            return;
        }

        Position hitPosition = new Position(Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2]));

/*
        if it is:
            attacks player2 board on the position provided; (first line of if condition)
            updates defender board (changes "~" to "." or "x" depending on if it was water or a ship) (second line of if condition)

         */

        for (GameServer.PlayerHandler defender : server.getPlayerList()) {
            if (!attacker.getName().equals(defender.getName())) {
                String hit = defender.getPlayerBoard().hit(hitPosition); // defender gets hit by attacker
                attacker.getPlayerBoard().updateAdversaryBoard(hitPosition, hit); //Update the attackers enemy board;


                //Redraws both of the defender boards (attacker and defender);
                defender.send(defender.getPlayerBoard().getYourBoard());
                defender.send(defender.getPlayerBoard().getAdversaryBoard());


            }
        }

    }

    /*
    Convert String to int
    If letters, returns false
     */
    public boolean isInt(String coordinate) {
        try {
            Integer.parseInt(coordinate);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
