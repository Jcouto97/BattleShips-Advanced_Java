package field;

import colors.Colors;
import gameobjects.Ship;
import gameobjects.ShipsENUM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Board is a class that creates game boards, it creates one for the attacker and another for the defender.
 * It also creates a list of all the players ships and an array with all the sizes of the ships in ascendent order.
 */
public class Board {
    private final static int BOARD_MAX_SIZE = 10;
    private final static String EMPTY_SPACE = " ";
    private final static String WATER = "░";
    private final static String BOAT_PIECE = "█";
    private final static String BOAT_PIECE_HIT = "╬";
    private final static String WATER_HIT = "■";
    private final static String LINE_BREAK = "\n";
    private final String[][] yourBoard;
    private final String[][] enemyBoard;
    private final int[] allShipSizes;
    private List<Ship> allTheShips;
    private final Set<Position> listOfPreviousAttacks = new HashSet<>();

    /**
     * Constructs the attackers and defenders board;
     * Draws both boards (cloneBoard());
     * Adds ships to attackers board.
     */
    public Board() {
        allShipSizes = new int[]{2};
        allTheShips = new ArrayList<>();
        this.yourBoard = new String[BOARD_MAX_SIZE][BOARD_MAX_SIZE];
        this.enemyBoard = new String[BOARD_MAX_SIZE][BOARD_MAX_SIZE];
        int numberOfRows = 1;
        int numberOfCols = 1;
        drawBoardLayout(numberOfRows, numberOfCols);
        cloneBoard();
        addShip();
    }

    /**
     * This method creates a clone of the attackers board, filled with water ONLY -> "■".
     * This cloned board is the adversarys board that we will attack on.
     */
    public void cloneBoard() {
        for (int rows = 0; rows < this.yourBoard.length; rows++) {
            for (int cols = 0; cols < this.yourBoard[rows].length; cols++) {
                this.enemyBoard[rows][cols] = this.yourBoard[rows][cols];
            }
        }
    }

    /*
    Draw the attackers board with rows and columns, and filled with water "~"
     */

    /**
     * This method draws the attackers board rows, columns, and content in between
     *
     * @param numberOfRows This parameter receives the max number of rows
     * @param numberOfCols This parameter receives the max number of columns
     */
    private void drawBoardLayout(int numberOfRows, int numberOfCols) {
        for (int rows = 0; rows < this.yourBoard.length; rows++) {
            for (int cols = 0; cols < this.yourBoard[rows].length; cols++) {
                if (rows == 0 && cols == 0) {
                    this.yourBoard[rows][cols] = EMPTY_SPACE;
                    continue;
                }
                if (rows == 0) {
                    this.yourBoard[rows][cols] = String.valueOf(numberOfRows);
                    numberOfRows++;
                    continue;
                }
                if (cols == 0) {
                    this.yourBoard[rows][cols] = ColumnENUM.values()[numberOfCols].getLetter();
                    numberOfCols++;
                    continue;
                }
                this.yourBoard[rows][cols] = Colors.BLUE + WATER + Colors.RESET;
            }
        }
    }

    public Set<Position> getListOfPreviousAttacks() {
        return listOfPreviousAttacks;
    }

    /**
     * This method loops until all the ships are created;
     * Creates a new ship with a random position;
     * Verifies if it's inside the limit of the board;
     * Verifies if there's collision between ships and if there are any ships around each other;
     * If all checks are cleared, it draws the ships on the board and adds them to a list of ships.
     */
    public void addShip() {
        int nextIndex = 0;
        while (allShipSizes.length > nextIndex) {
            Ship ship = new Ship(allShipSizes[nextIndex], new Position((int) Math.floor(Math.random() * BOARD_MAX_SIZE) + 1, (int) Math.floor(Math.random() * BOARD_MAX_SIZE) + 1));

            if (!isShipInsideBoard(ship) || isShipOnTopOfOtherShip(ship)) {
                ship = null;
            }

            if (ship != null) {
                drawShipOnBoard(ship);
                allTheShips.add(ship);
                nextIndex++;
            }
        }
    }

    /**
     * This method draws the head of the ship on the board, and for each iteration of the loop, it sets the next ship piece on the corresponding position
     *
     * @param ship This parameter receives a ship.
     */
    private void drawShipOnBoard(Ship ship) {
        for (int i = 0; i < ship.getFullShip().size(); i++) {
            this.yourBoard[ship.getFullShip().get(i).getX()][ship.getFullShip().get(i).getY()] =
                    Colors.YELLOW + BOAT_PIECE + Colors.RESET;
        }
    }

    /**
     * This method checks if a ship is inside the boards limits.
     *
     * @param ship This parameter receives a ship.
     * @return Returns true if a boat is inside the board, and false if it is outside.
     */
    private boolean isShipInsideBoard(Ship ship) {
        for (int i = 0; i < ship.getFullShip().size(); i++) {
            if (ship.getFullShip().get(i).getX() < 1
                    || ship.getFullShip().get(i).getY() < 1
                    || ship.getFullShip().get(i).getX() > BOARD_MAX_SIZE - 1
                    || ship.getFullShip().get(i).getY() > BOARD_MAX_SIZE - 1) {
                return false;
            }
        }
        return true;
    }

    // check if ship is on top of another and there is no ship around the new ship
    /*

     */

    /**
     * This method checks if a ship is over another, and also if there are no ships around it by 1 position in every direction
     *
     * @param ship This parameter receives a ship.
     * @return Returns true if all conditions check out, and false if not.
     */
    private boolean isShipOnTopOfOtherShip(Ship ship) {
        if (ship == null) {
            return true;
        }
        for (int newShipPositions = 0; newShipPositions < ship.getFullShip().size(); newShipPositions++) {
            for (int indexOfAllShips = 0; indexOfAllShips < allTheShips.size(); indexOfAllShips++) {
                for (int shipPositions = 0; shipPositions < allTheShips.get(indexOfAllShips).getFullShip().size(); shipPositions++) {
                    if (allTheShips.get(indexOfAllShips).getFullShip().get(shipPositions).equals(ship.getFullShip().get(newShipPositions))) {
                        return true;
                    }
                    if (checkIfShipsConnected(ship, newShipPositions, indexOfAllShips, shipPositions)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //check if the new ship created is connected to the other ships

    /**
     * This method checks if the ship which was created is connected to other ships.
     *
     * @param ship             This parameter receives a ship.
     * @param newShipPositions This parameter receives an int that will be the positions where the new ship will be drawn in.
     * @param indexOfAllShips  This parameter receives an int that will be the index of all ships in the board.
     * @param shipPositions    This parameter receives an int that will be the positions of all ships already placed on the board.
     * @return Returns true if there are ships connected, false if there aren't.
     */
    private boolean checkIfShipsConnected(Ship ship, int newShipPositions, int indexOfAllShips, int shipPositions) {
        for (int indexShipEnum = 0; indexShipEnum < ShipsENUM.values().length; indexShipEnum++) {
            if (allTheShips.get(indexOfAllShips).getFullShip().get(shipPositions).getX() == (ship.getFullShip()
                    .get(newShipPositions).getX() + ShipsENUM.values()[indexShipEnum].getAxisX())
                    && (allTheShips.get(indexOfAllShips).getFullShip().get(shipPositions).getY() == (ship.getFullShip()
                    .get(newShipPositions).getY() + ShipsENUM.values()[indexShipEnum].getAxisY()))) {
                return true;
            }
        }
        return false;
    }

    public List<Ship> getAllTheShips() {
        return allTheShips;
    }

    /**
     * This method gets the players own board and divides every section with an empty space -> " ", while concationg it with line breaks "\n".
     *
     * @return Returns the board printed on the console.
     */
    public String getYourBoard() {
        String boardString = "";
        for (String[] rows : this.yourBoard) {
            for (String cols : rows) {
                boardString = boardString.concat(EMPTY_SPACE + cols + EMPTY_SPACE);
            }
            boardString = boardString.concat(LINE_BREAK);
        }
        return boardString;
    }

    /**
     * This method gets the player's version of the adversary board and divides every section with an empty space -> " ", while concationg it with line breaks "\n".
     *
     * @return Returns the board printed on the console.
     */
    public String getAdversaryBoard() {
        String newBoardString = "";
        for (String[] strings : this.enemyBoard) {
            for (String string : strings) {
                newBoardString = newBoardString.concat(EMPTY_SPACE + string + EMPTY_SPACE);
            }
            newBoardString = newBoardString.concat(LINE_BREAK);
        }
        return newBoardString;
    }

    /**
     * This method updates the player's version of the adversary board whenever the player hit's a position. It marks the coordinates attacked.
     *
     * @param position This parameter is the X and Y position of where the player decides to attack.
     * @param hit      This parameter is the character that will show on the board depending on if the player attacked a boat or water.
     */
    public void updateAdversaryBoard(Position position, String hit) {
        enemyBoard[position.getX()][position.getY()] = hit;
    }

    /**
     * This method attacks the player's adversary
     *
     * @param position This parameter is the X and Y position of where the player decides to attack.
     * @return Returns a string, that will change how the game is played depending on what said string is.
     */
    public String hit(Position position) {

        if (!listOfPreviousAttacks.add(position)) {
            return "Same position";
        }

        if (position.getX() <= 0 || position.getY() <= 0 || position.getX() > (BOARD_MAX_SIZE - 1) || position.getY() > (BOARD_MAX_SIZE - 1)) {
            return "Out of bounds";
        }

        if (isShip(position)) {
            yourBoard[position.getX()][position.getY()] = Colors.RED + BOAT_PIECE_HIT + Colors.RESET;
            return Colors.RED + BOAT_PIECE_HIT + Colors.RESET;
        }
        yourBoard[position.getX()][position.getY()] = Colors.BLACK_BRIGHT + WATER_HIT + Colors.RESET;
        return Colors.BLACK_BRIGHT + WATER_HIT + Colors.RESET;
    }

    /**
     * This method says wheter the position a player attacked is a boat or not.
     *
     * @param position This parameter is the X and Y position of where the player decides to attack.
     * @return Returns true if the position is a ship, or false if it isn't.
     */
    private boolean isShip(Position position) {
        if (yourBoard[position.getX()][position.getY()].equals(Colors.YELLOW + BOAT_PIECE + Colors.RESET)) {
            return true;
        }
        return false;
    }
}