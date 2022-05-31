package field;

import gameobjects.Ship;

public class Board {
    private final static int BOARD_MAX_SIZE = 10;

    private final String[][] yourBoard;
    private final String[][] enemyBoard;

    public Board() {
        this.yourBoard = new String[BOARD_MAX_SIZE][BOARD_MAX_SIZE];
        this.enemyBoard = new String[BOARD_MAX_SIZE][BOARD_MAX_SIZE];
        int numberOfRows = 1;
        int numberOfCols = 1;
        drawNumbersAndWater(numberOfRows, numberOfCols);
        cloneBoard();
        addShip();
    }

    public void cloneBoard() {
        for (int rows = 0; rows < this.yourBoard.length; rows++) {
            for (int cols = 0; cols < this.yourBoard[rows].length; cols++) {
                this.enemyBoard[rows][cols] = this.yourBoard[rows][cols];
            }
        }
    }

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

    public void addShip() {
        Ship ship = new Ship(1, new Position(3, 5));
        //  new Position((int) Math.floor(Math.random() * 5) + 1, (int) Math.floor(Math.random() * 5) + 1));
        this.yourBoard[ship.getPosition().getX()][ship.getPosition().getY()] = "#";
        Ship ship = new Ship(2,new Position((int) Math.floor(Math.random() * 5) + 1, (int) Math.floor(Math.random() * 5) + 1));

        for (int i = 0; i < ship.getFullShip().size(); i++) {
            this.board[ship.getFullShip().get(i).getX()][ship.getFullShip().get(i).getY()] = "#";
        }


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
public void updateAdversaryBoard(Position position,String hit){
    enemyBoard[position.getX()][position.getY()] = hit;
}
//update the defender board
    public String hit(Position position) {
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

