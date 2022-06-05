package field;

import gameobjects.Ship;
import gameobjects.ShipsENUM;

import java.util.*;

import static field.BoardSymbols.*;

/**
 * Board is a class that creates game boards, it creates one for the attacker and another for the defender.
 * It also creates a list of all the players ships and an array with all the sizes of the ships in ascendent order.
 */
public class Board {
    private final static int BOARD_MAX_SIZE = 10;
    private final String[][] yourBoard;
    private final String[][] enemyBoard;
    private final int[] allShipSizes;
    private final List<Ship> allTheShips;
    private final Set<Position> allShipsPositions;
    private final Set<Position> listOfPreviousAttacks;

    /**
     * Constructs the attackers and defenders board;
     * Draws both boards (cloneBoard());
     * Adds ships to attackers board.
     */
    public Board() {
        this.allShipsPositions = new HashSet<>();
        this.allShipSizes = new int[]{2, 3, 3, 4, 5};
        this.allTheShips = new ArrayList<>();
        this.yourBoard = new String[BOARD_MAX_SIZE][BOARD_MAX_SIZE];
        this.enemyBoard = new String[BOARD_MAX_SIZE][BOARD_MAX_SIZE];
        this.listOfPreviousAttacks = new HashSet<>();
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

    /**
     * This method draws the attackers board rows, columns, and content in between.
     *
     * @param numberOfRows This parameter receives the max number of rows
     * @param numberOfCols This parameter receives the max number of columns
     */
    private void drawBoardLayout(int numberOfRows, int numberOfCols) {
        for (int rows = 0; rows < this.yourBoard.length; rows++) {
            for (int cols = 0; cols < this.yourBoard[rows].length; cols++) {
                if (rows == 0 && cols == 0) {
                    this.yourBoard[rows][cols] = EMPTY_SPACE.getSymbol();
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
                this.yourBoard[rows][cols] = WATER.getSymbol();
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
            Ship ship = generateNewShip(nextIndex);
            if (!isShipInsideBoard(ship) || isShipOnTopOfOtherShip(ship)) {
                continue;
            }
            drawShipOnBoard(ship);
            allTheShips.add(ship);
            addPositionsAroundTheShip(ship);
            allShipsPositions.addAll(ship.getFullShip());
            nextIndex++;
        }
    }

    private Ship generateNewShip(int nextIndex) {
        int newXCoord = (int) Math.floor(Math.random() * BOARD_MAX_SIZE) + 1;
        int newYCoord = (int) Math.floor(Math.random() * BOARD_MAX_SIZE) + 1;
        return new Ship(allShipSizes[nextIndex], new Position(newXCoord,newYCoord));
    }

    /**
     * This method draws the head of the ship on the board, and for each iteration of the loop, it sets the next ship piece on the corresponding position
     *
     * @param ship This parameter receives a ship.
     */
    private void drawShipOnBoard(Ship ship) {
        ship.getFullShip().forEach(i->this.yourBoard[i.getX()][i.getY()] = BOAT_PIECE.getSymbol());
    }

    /**
     * This method checks if a ship is inside the boards limits.
     *
     * @param ship This parameter receives a ship.
     * @return Returns true if a boat is inside the board, and false if it is outside.
     */
    private boolean isShipInsideBoard(Ship ship) {
        return ship.getFullShip().stream()
                .noneMatch(position -> position.getX() < 1 ||  position.getY() < 1
                        || position.getX() > BOARD_MAX_SIZE - 1 || position.getY()> BOARD_MAX_SIZE - 1);
    }

    /**
     * This method checks if a ship is over another, and also if there are no ships around it by 1 position in every direction
     *
     * @param ship This parameter receives a ship.
     * @return Returns true if all conditions check out, and false if not.
     */
    private boolean isShipOnTopOfOtherShip(Ship ship) {
        for (int i = 0; i < ship.getFullShip().size(); i++) {
            if (this.allShipsPositions.contains(ship.getFullShip().get(i))) {
                return true;
            }
        }
        return false;
    }

    private void addPositionsAroundTheShip(Ship ship) {
        Arrays.stream(ShipsENUM.values())
                .forEach(enumPositions-> ship.getFullShip()
                        .forEach(shipPosition ->
                                allShipsPositions.add(new Position(shipPosition.getX()+ enumPositions.getAxisX()
                                        ,shipPosition.getY()+ enumPositions.getAxisY())))
                );
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
                boardString = boardString.concat(EMPTY_SPACE.getSymbol()+ cols +EMPTY_SPACE.getSymbol());
            }
            boardString = boardString.concat(LINE_BREAK.getSymbol());
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
        for (String[] rows : this.enemyBoard) {
            for (String cols : rows) {
                newBoardString = newBoardString.concat(EMPTY_SPACE.getSymbol()+ cols +EMPTY_SPACE.getSymbol());
            }
            newBoardString = newBoardString.concat(LINE_BREAK.getSymbol());
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
            return SAME_POSITION.getSymbol();
        }
        if (position.getX() <= 0 || position.getY() <= 0 || position.getX() > (BOARD_MAX_SIZE - 1) || position.getY() > (BOARD_MAX_SIZE - 1)) {
            return  OUT_OFF_BOUNDS.getSymbol();
        }
        if (isShip(position)) {
            yourBoard[position.getX()][position.getY()] = BOAT_PIECE_HIT.getSymbol();
            return BOAT_PIECE_HIT.getSymbol();
        }
        yourBoard[position.getX()][position.getY()] = WATER_HIT.getSymbol();
        return WATER_HIT.getSymbol();
    }

    /**
     * This method says wheter the position a player attacked is a boat or not.
     *
     * @param position This parameter is the X and Y position of where the player decides to attack.
     * @return Returns true if the position is a ship, or false if it isn't.
     */
    private boolean isShip(Position position) {
        return yourBoard[position.getX()][position.getY()].equals(BOAT_PIECE.getSymbol());
    }
}