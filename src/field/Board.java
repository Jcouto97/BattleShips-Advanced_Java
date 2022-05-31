package field;

import gameobjects.Ship;
import gameobjects.ShipsENUM;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final static int BOARD_MAX_SIZE = 10;

    private final String[][] board;

    private final int[] allShipSizes;
    private List<Ship> allTheShips;

    public Board() {
        this.board = new String[BOARD_MAX_SIZE][BOARD_MAX_SIZE]; // mudar para constantes
        allShipSizes = new int[]{2, 3, 3, 4, 5};
        allTheShips = new ArrayList<>();
        int numberOfRows = 1;
        int numberOfCols = 1;
        drawNumbersAndWater(numberOfRows, numberOfCols);
        addShip();
    }

    private void drawNumbersAndWater(int numberOfRows, int numberOfCols) {
        for (int rows = 0; rows < this.board.length; rows++) {
            for (int cols = 0; cols < this.board[rows].length; cols++) {
                if (rows == 0 && cols == 0) {
                    this.board[rows][cols] = " ";
                    continue;
                }
                if (rows == 0) {
                    this.board[rows][cols] = String.valueOf(numberOfRows);
                    numberOfRows++;
                    continue;
                }
                if (cols == 0) {
                    this.board[rows][cols] = String.valueOf(numberOfCols);
                    numberOfCols++;
                    continue;
                }
                this.board[rows][cols] = "~";
            }
        }

    }

    // add ship
    public void addShip() {
        int nextIndex = 0;
        while (allShipSizes.length > nextIndex) {
            // creates a new ship
            Ship ship = new Ship(allShipSizes[nextIndex]
                    , new Position((int) Math.floor(Math.random() * BOARD_MAX_SIZE) + 1
                    , (int) Math.floor(Math.random() * BOARD_MAX_SIZE) + 1));

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
    private void drawShipOnBoard(Ship ship) {
        for (int i = 0; i < ship.getFullShip().size(); i++) {
            this.board[ship.getFullShip().get(i).getX()][ship.getFullShip().get(i).getY()] = "#";
        }
    }
// check if ship is inside the board if it is return true if its outside return false
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
    private boolean isShipOnTopOfOtherShip(Ship ship) {
        if (ship == null) return true;
        for (int newShipPositions = 0; newShipPositions < ship.getFullShip().size(); newShipPositions++) {
            for (int indexOfAllShips = 0; indexOfAllShips < allTheShips.size(); indexOfAllShips++) {
                for (int shipPositions = 0; shipPositions < allTheShips.get(indexOfAllShips).getFullShip().size(); shipPositions++) {
                    if (allTheShips.get(indexOfAllShips).getFullShip().get(shipPositions).equals(ship.getFullShip().get(newShipPositions))) {
                        return true;
                    }
                    if (checkIfShipsConnected(ship, newShipPositions, indexOfAllShips, shipPositions)) return true;
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
    public String getBoard() {
        String board = "";
        for (String[] rows : this.board) {
            for (String cols : rows) {
                board = board.concat(" " + cols + " ");
            }
            board = board.concat("\n");
        }
        return board;
    }

    public void hit(Position position) {
        if (isShip(position)) {
            board[position.getX()][position.getY()] = "X";
            return;
        }
        board[position.getX()][position.getY()] = ".";
    }

    private boolean isShip(Position position) {
        if (board[position.getX()][position.getY()].equals("#")) return true;
        return false;
    }

}

