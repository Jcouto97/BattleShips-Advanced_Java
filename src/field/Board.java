package field;

import gameobjects.Ship;
public class Board {
    private final static int BOARD_MAX_SIZE = 10;

    private final String[][] board;

    public Board() {
        this.board = new String[BOARD_MAX_SIZE][BOARD_MAX_SIZE]; // mudar para constantes
        int numberOfRows = 1;
        int numberOfCols = 1;
        drawNumbersAndWater(numberOfRows, numberOfCols);
        addShip();
    }

    private void drawNumbersAndWater(int numberOfRows, int numberOfCols) {
        for (int rows = 0; rows < this.board.length; rows++) {
            for (int cols = 0; cols < this.board[rows].length; cols++) {
                if(rows==0 && cols == 0){
                    this.board[rows][cols] = " ";
                    continue;
                }
                if(rows == 0){
                    this.board[rows][cols] = String.valueOf(numberOfRows);
                    numberOfRows++;
                    continue;
                }
                if(cols == 0){
                    this.board[rows][cols] = String.valueOf(numberOfCols);
                    numberOfCols++;
                    continue;
                }
                this.board[rows][cols] = "~";
            }
        }
    }

    // NUNO
    public void addShip(){
        Ship ship = new Ship(1,new Position((int) Math.floor(Math.random()*5)+1,(int) Math.floor(Math.random()*5)+1));
        this.board[ship.getPosition().getX()][ship.getPosition().getY()] = "#";
    }

    // get the full board
    public String getBoard() {
        String board="";
        for (String[] rows : this.board) {
            for (String cols : rows) {
                board = board.concat(" " + cols + " ");
            }
            board = board.concat("\n");
        }
        return board;
    }

}
