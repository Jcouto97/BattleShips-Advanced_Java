package field;

import gameobjects.Ship;
import gameobjects.ShipsENUM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


/*
Board is a class that creates game boards
Creates a board for the attacker and another for the defender
Creates a list of all the players ships
Creates an array with all the sizes of the ships in ascendent order
*/
public class Board {
    private final static int BOARD_MAX_SIZE = 10;

    private final String[][] yourBoard;
    private final String[][] enemyBoard;

    private final int[] allShipSizes;
    private List<Ship> allTheShips;

    private final HashSet<Position> listOfPreviousAttacks = new HashSet<Position>();


    /*
    Constructs the attackers board and defenders board
    Draws both boards (cloneBoard())
    Adds ships to attackers board ,
    */
    public Board() {
        allShipSizes = new int[]{2,3, 3, 4, 5};
        allTheShips = new ArrayList<>();
        this.yourBoard = new String[BOARD_MAX_SIZE][BOARD_MAX_SIZE];
        this.enemyBoard = new String[BOARD_MAX_SIZE][BOARD_MAX_SIZE];
        int numberOfRows = 1;
        int numberOfCols = 1;
        drawNumbersAndWater(numberOfRows, numberOfCols);
        cloneBoard();
        addShip();
    }

    /*
    Creates a clone of the attackers board, filled with water ONLY "~"
    It's the adversarys board, but only to keep track of where I attack
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
    private void drawNumbersAndWater(int numberOfRows, int numberOfCols) {
        for (int rows = 0; rows < this.yourBoard.length; rows++) {
            for (int cols = 0; cols < this.yourBoard[rows].length; cols++) {
                if (rows == 0 && cols == 0) {
                    this.yourBoard[rows][cols] = " ";
                    continue;
                }
                if (rows == 0) {
                    this.yourBoard[rows][cols] = String.valueOf(numberOfRows);
                    numberOfRows++;
                    continue;
                }
                if (cols == 0) {
                    this.yourBoard[rows][cols] = String.valueOf(numberOfCols);
                    numberOfCols++;
                    continue;
                }
                this.yourBoard[rows][cols] = "~";
            }
        }
    }

    /*
    It loops until all the ships are created
    Creates a new ship with a random position
    Verifies if it's inside the limit of the board
    Verifies if there's collision between ships and if there are any ships around it
    If ship passes all checks, it draws them on the board and adds them to a list of ships
     */
    public void addShip() {
        int nextIndex = 0;
        while (allShipSizes.length > nextIndex) {
            // creates a new ship
            Ship ship = new Ship(allShipSizes[nextIndex], new Position((int) Math.floor(Math.random() * BOARD_MAX_SIZE) + 1, (int) Math.floor(Math.random() * BOARD_MAX_SIZE) + 1));

            //check if ship is inside board and on top of other ship
            if (!isShipInsideBoard(ship) || isShipOnTopOfOtherShip(ship)) {
                ship = null;
            }

            // draws ship on the board and adds to a list of ships
            if (ship != null) {
                drawShipOnBoard(ship);
                allTheShips.add(ship);
                nextIndex++;
            }
        }
    }

    // draws ship on the board
    // for each iterations it sets one piece of the body on the next position
    // # = ship
    private void drawShipOnBoard(Ship ship) {
        for (int i = 0; i < ship.getFullShip().size(); i++) {
            this.yourBoard[ship.getFullShip().get(i).getX()][ship.getFullShip().get(i).getY()] = "#";
        }
    }

    // check if ship is inside the boards limits if it is return true if its outside return false
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

    // get the full board
    public String getYourBoard() {
        String boardString = "";
        for (String[] rows : this.yourBoard) {
            for (String cols : rows) {
                boardString = boardString.concat(" " + cols + " ");
            }
            boardString = boardString.concat("\n");
        }
        return boardString;
    }

    public String getAdversaryBoard() {
        String newBoardString = "";
        for (String[] strings : this.enemyBoard) {
            for (String string : strings) {
                newBoardString = newBoardString.concat(" " + string + " ");
            }
            newBoardString = newBoardString.concat("\n");
        }
        return newBoardString;
    }

    // updating enemy board from the attacker
    public void updateAdversaryBoard(Position position, String hit) {
        enemyBoard[position.getX()][position.getY()] = hit;
    }

    //update the defender board
    public String hit(Position position) {
        for (Position i : listOfPreviousAttacks) {
            if (i.getX() == position.getX() && i.getY() == position.getY()) {
                return "Same position";
            }
        }
        listOfPreviousAttacks.add(position);

        if (isShip(position)) {
            yourBoard[position.getX()][position.getY()] = "X";
            return "X";
        }
        yourBoard[position.getX()][position.getY()] = ".";
        return ".";
    }

    private boolean isShip(Position position) {
        if (yourBoard[position.getX()][position.getY()].equals("#") || enemyBoard[position.getX()][position.getY()].equals("#")) {
            return true;
        }
        return false;
    }

}

