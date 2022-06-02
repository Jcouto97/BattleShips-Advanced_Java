package commands;

import colors.Colors;
import field.ColumnENUM;
import field.Position;
import gameobjects.Ship;
import gameobjects.ShipsENUM;
import game.GameServer;
import static utils.asciiArt.LOSER;
import static utils.asciiArt.WINNER;


public class AttackHandler implements CommandHandler {

    /**
     * Divide message sent by the player into an array, to get attack coordinates after;
     * Check if what the player wrote were integers;
     * Create position where player wants to attack;
     * Iterate through list of players and if name of attacker is different
     * from name of defender, attack defenders board. Else attacker would attack himself also;
     * If so, defender gets hit by attacker;
     * Update the attackers enemy board;
     * Redraws both of the defender boards (attacker and defender);
     */
    @Override
    public void command(GameServer.PlayerHandler attacker, GameServer server) {
        String[] coordinates = attacker.getMessage().split(" ");
        int columnEnumIndex = isEnum(coordinates[1].toUpperCase());
        if (columnEnumIndex == -1 || !isInt(coordinates[2])) {
            return;
        }

        Position hitPosition = new Position(ColumnENUM.values()[columnEnumIndex].getValue(), Integer.parseInt(coordinates[2]));

        /*
        if it is: attacks player2 board on the position provided; (first line of if condition)
            updates defender board (changes "~" to "." or "x" depending on if it was water or a ship) (second line of if condition)
         */
        for (GameServer.PlayerHandler defender : server.getPlayerList()) {
            if (!attacker.getName().equals(defender.getName()) && attacker.getPlayerGameId() == defender.getPlayerGameId()) {
                String hit = defender.getPlayerBoard().hit(hitPosition); // defender gets hit by attacker
                if (samePosition(attacker, hit)) {
                    break; // checks if is the same position
                }

                if (checkOutOfBounds(attacker, hit)) {
                    break; // check is it´s out of bounds
                }

                attacker.getPlayerBoard().updateAdversaryBoard(hitPosition, hit); //Update the attackers enemy board;
                reDrawPlayerBoards(defender); //Redraws both of the defender boards (attacker and defender);

                if (checkIfMissShip(attacker, defender, hit)) {
                    break; // check if missed ship
                }

                shipHit(hitPosition, defender, attacker); // hit the ship and reduces its life
                winnerAndLoser(attacker, defender); // check if there is a winner
            }
        }
        for (GameServer.PlayerHandler players : server.getPlayerList()) {
            if (attacker.getPlayerGameId() == players.getPlayerGameId()){
                synchronized (players.getLock()) {
                    players.getLock().notifyAll();
                }
            }
        }
    }

    /**
     * Checks if hitting in the same position
     */
    private boolean samePosition(GameServer.PlayerHandler attacker, String hit) {
        if (hit.equals("Same position")) {
            attacker.send("You already attacked in those coordinates, try again!");
            return true;
        }
        return false;
    }

    /**
     * Check hit is it´s out of bounds (of the board)
     */
    private boolean checkOutOfBounds(GameServer.PlayerHandler attacker, String hit) {
        if (hit.equals("Out of bounds")) {
            attacker.send("Coordinates are outside the board, try again!");
            return true;
        }
        return false;
    }

    /**
     * Redraws both defender and attacker boards;
     */
    private void reDrawPlayerBoards(GameServer.PlayerHandler player) {
        player.send(player.getPlayerBoard().getYourBoard());
        player.send(player.getPlayerBoard().getAdversaryBoard());
    }

    /**
     * Check if attacker misses ship, in order to continue to attack
     */
    private boolean checkIfMissShip(GameServer.PlayerHandler attacker, GameServer.PlayerHandler defender, String hit) {
        if (!hit.equals(Colors.RED + "╬" + Colors.RESET)) {
            attacker.setAttacker(false);
            defender.setAttacker(true);
            return true;
        }
        return false;
    }

    /**
     *  Hit the ship and reduces its life
     *  1º loop, iterates through array of ships;
     *  2º loop, iterates through the ship positions;
     *  In that position hit the ship;
     *  If ships number of hits == 0 then set ship isDead bollean to true;
     *  Calls shipBorder function to surround dead ship;
     */
    private void shipHit(Position hitPosition, GameServer.PlayerHandler defender, GameServer.PlayerHandler attacker) {
        for (int i = 0; i < defender.getPlayerBoard().getAllTheShips().size(); i++) { //inside the array of ships
            for (int j = 0; j < defender.getPlayerBoard().getAllTheShips().get(i).getFullShip().size(); j++) { //inside the ship
                if (defender.getPlayerBoard().getAllTheShips().get(i).getFullShip().get(j).equals(hitPosition)) { //se no ship 0 e na posiçao 0 == hit position, da hit
                    defender.getPlayerBoard().getAllTheShips().get(i).shipHit();

                    if (defender.getPlayerBoard().getAllTheShips().get(i).getNumberOfHits() == 0 //number of hits remaining to die
                            && !defender.getPlayerBoard().getAllTheShips().get(i).isDead()) { //and if its not dead
                        defender.getPlayerBoard().getAllTheShips().get(i).setDead();

                        Ship ship = defender.getPlayerBoard().getAllTheShips().get(i);
                        shipBorder(ship, defender, attacker); //ship border when dead
                    }
                    return;
                }
            }
        }
    }

    /**
     * Uses full ship size + ShipsEnums to make ship border when ship is dead
     * Updates attacker board and redraws defenders
     */
    private void shipBorder(Ship ship, GameServer.PlayerHandler defender, GameServer.PlayerHandler attacker) {
        for (int i = 0; i < ship.getFullShip().size(); i++) {
            for (int j = 0; j < ShipsENUM.values().length; j++) {
                Position tempPosition = new Position(ship.getFullShip().get(i).getX() + ShipsENUM.values()[j].getAxisX()
                        , ship.getFullShip().get(i).getY() + ShipsENUM.values()[j].getAxisY());

                if (defender.getPlayerBoard().hit(tempPosition).equals(Colors.BLACK_BRIGHT + "■" + Colors.RESET)) {
                    attacker.getPlayerBoard().updateAdversaryBoard(tempPosition, Colors.BLACK_BRIGHT + "■" + Colors.RESET); //update in attackers adversary board the hit
                }
            }
        }
        reDrawPlayerBoards(defender);
    }
    /*
    Checks if there is a winner
     */

    /**
     * If defenders boats are all dead define winner and looser
     * Send AsciiArt
     * Close sockets
     */
    private void winnerAndLoser(GameServer.PlayerHandler attacker, GameServer.PlayerHandler defender) {
        if (!defender.checkIfTheresShipsAlive()) {
            reDrawPlayerBoards(attacker);
            defender.send("\n\n" + LOSER);
            attacker.send("\n\n" + WINNER);
            defender.close();
            attacker.close();
        }
    }

    /**
     * Convert String to int
     * If letters, returns false
     */
    public boolean isInt(String coordinate) {
        try {
            Integer.parseInt(coordinate);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if letter parameter equals to a letter from ColumnEnums
     * Returns an index to fetch letter from ColumnEnums
     * @param letter
     * @return
     */
    public int isEnum(String letter) {
        for (int i = 0; i < ColumnENUM.values().length; i++) {
            if (letter.equals(ColumnENUM.values()[i].getLetter())) {
                return i;
            }
        }
        return -1;
    }
}
