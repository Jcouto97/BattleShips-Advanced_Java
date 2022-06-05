package commands;

import field.BoardSymbols;
import field.ColumnENUM;
import field.Position;
import gameobjects.Ship;
import gameobjects.ShipsENUM;
import game.GameServer;

import java.util.Arrays;
import static field.BoardSymbols.*;
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
        if (coordinates.length < 3) {
            return;
        }
        int columnEnumIndex = isEnum(coordinates[1].toUpperCase());
        if (columnEnumIndex == -1 || !isInt(coordinates[2])) {
            return;
        }
        Position hitPosition = new Position(ColumnENUM.values()[columnEnumIndex].getValue(), Integer.parseInt(coordinates[2]));
        /*if it is: attacks player2 board on the position provided; (first line of if condition)
            updates defender board (changes "~" to "." or "x" depending on if it was water or a ship) (second line of if condition)*/
        checkHit(attacker, server, hitPosition);
        for (GameServer.PlayerHandler players : server.getPlayerList()) {
            if (attacker.getPlayerGameId() == players.getPlayerGameId()) {
                synchronized (players.getLock()) {
                    players.getLock().notifyAll();
                }
            }
        }
    }
    private void checkHit(GameServer.PlayerHandler attacker, GameServer server, Position hitPosition) {
        for (GameServer.PlayerHandler defender : server.getPlayerList()) {
            if (!attacker.getName().equals(defender.getName()) && attacker.getPlayerGameId() == defender.getPlayerGameId()) {
                String hit = defender.getPlayerBoard().hit(hitPosition); // defender gets hit by attacker
                if (samePosition(hit)) {
                    attacker.send("You already attacked in those coordinates, try again!");
                    break; // checks if is the same position
                }
                if (checkOutOfBounds(hit)) {
                    attacker.send("Coordinates are outside the board, try again!");
                    break; // check is it´s out of bounds
                }
                attacker.getPlayerBoard().updateAdversaryBoard(hitPosition, hit); //Update the attackers enemy board;
                reDrawPlayerBoards(defender); //Redraws both of the defender boards (attacker and defender)
                if (checkIfMissShip(attacker, defender, hit)) {
                    break; // check if missed ship
                }
                shipHit(hitPosition, defender, attacker); // hit the ship and reduces its life
                winnerAndLoser(attacker, defender); // check if there is a winner
            }
        }
    }

    /**
     * Checks if hitting in the same position
     */
    private boolean samePosition(String hit) {
        return hit.equals(SAME_POSITION.getSymbol());
    }

    /**
     * Check hit is it´s out of bounds (of the board)
     */
    private boolean checkOutOfBounds(String hit) {
        return hit.equals(OUT_OFF_BOUNDS.getSymbol());
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
        if (!hit.equals(BoardSymbols.BOAT_PIECE_HIT.getSymbol())) {
            attacker.setAttacker(false);
            defender.setAttacker(true);
            return true;
        }
        return false;
    }

    /**
     * Hit the ship and reduces its life
     * 1º loop, iterates through array of ships;
     * 2º loop, iterates through the ship positions;
     * In that position hit the ship;
     * If ships number of hits == 0 then set ship isDead bollean to true;
     * Calls shipBorder function to surround dead ship;
     */
    private void shipHit(Position hitPosition, GameServer.PlayerHandler defender, GameServer.PlayerHandler attacker) {
        defender.getPlayerBoard().getAllTheShips()
                .forEach(fullShip -> fullShip.getFullShip().stream()
                        .filter(position -> position.equals(hitPosition))
                        .forEach((position) -> {
                            fullShip.shipHit();
                            if (fullShip.getNumberOfHits() == 0 && !fullShip.isDead()) {
                                fullShip.setDead();
                                shipBorder(fullShip, defender, attacker);
                            }
                        }));
    }

    /**
     * Uses full ship size + ShipsEnums to make ship border when ship is dead
     * Updates attacker board and redraws defenders
     */
    private void shipBorder(Ship ship, GameServer.PlayerHandler defender, GameServer.PlayerHandler attacker) {
        ship.getFullShip()
                .forEach(shipPosition-> Arrays.stream(ShipsENUM.values())
                        .forEach(enumPosition-> fillShipBorders(defender, attacker, shipPosition, enumPosition)));
        reDrawPlayerBoards(defender);
    }

    private void fillShipBorders(GameServer.PlayerHandler defender, GameServer.PlayerHandler attacker, Position shipPosition, ShipsENUM enumPosition) {
        Position tempPosition = getNewPosition(shipPosition, enumPosition);
        if(defender.getPlayerBoard().hit(tempPosition).equals(WATER_HIT.getSymbol())){
            attacker.getPlayerBoard().updateAdversaryBoard(tempPosition, WATER_HIT.getSymbol());
        }
    }
    private Position getNewPosition(Position shipPosition, ShipsENUM enumPosition) {
        return new Position(shipPosition.getX() + enumPosition.getAxisX(), shipPosition.getY() + enumPosition.getAxisY());
    }

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
     *
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
