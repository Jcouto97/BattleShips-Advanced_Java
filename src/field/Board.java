package field;

import gameobjects.Ship;

public class Board {
    private final static int BOARD_MAX_SIZE = 10;

    private final String[][] board;
    private final String[][] board2;

    public Board() {
        this.board = new String[BOARD_MAX_SIZE][BOARD_MAX_SIZE];
        this.board2 = new String[BOARD_MAX_SIZE][BOARD_MAX_SIZE];
        int numberOfRows = 1;
        int numberOfCols = 1;
        drawNumbersAndWater(numberOfRows, numberOfCols);
        cloneBoard();
        addShip();
    }

    public String[][] getBoard2() {
        return board2;
    }

    public void cloneBoard() {
        for (int rows = 0; rows < this.board.length; rows++) {
            for (int cols = 0; cols < this.board[rows].length; cols++) {
                this.board2[rows][cols] = this.board[rows][cols];
            }
        }
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

    public void addShip() {
        Ship ship = new Ship(1, new Position(3, 5));
        //  new Position((int) Math.floor(Math.random() * 5) + 1, (int) Math.floor(Math.random() * 5) + 1));
        this.board[ship.getPosition().getX()][ship.getPosition().getY()] = "#";
    }

    // get the full board
    public String getBoard() {
        String boardString = "";
        for (String[] rows : this.board) {
            for (String cols : rows) {
                boardString = boardString.concat(" " + cols + " ");
            }
            boardString = boardString.concat("\n");
        }
        return boardString;
    }

    public String getAdversaryBoard() {
        String newBoardString = "";
        for (int i = 0; i < this.board2.length; i++) {
            for (int j = 0; j < this.board2[i].length; j++) {
                newBoardString = newBoardString.concat(" " + this.board2[i][j] + " ");
            }
            newBoardString = newBoardString.concat("\n");
        }
        return newBoardString;
    }


    public void hit(Position position) {
        if (isShip(position)) {
            board[position.getX()][position.getY()] = "X";
            board2[position.getX()][position.getY()] = "X";
            return;
        }
        board[position.getX()][position.getY()] = ".";
        board2[position.getX()][position.getY()] = ".";

    }

    private boolean isShip(Position position) {
        if (board[position.getX()][position.getY()].equals("#") || board2[position.getX()][position.getY()].equals("#") ) {
            return true;
        }
        return false;
    }

    public static void main(String[] args) {
        Board a = new Board();
        for (int i = 0; i < a.getBoard2().length; i++) {
            for (int i1 = 0; i1 < a.board2[i].length; i1++) {
                System.out.print(" " + a.getBoard2()[i][i1] + " ");
            }
            System.out.print("\n");
        }

    }
}

