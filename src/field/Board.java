package field;

import gameobjects.Ship;

import java.util.Arrays;

public class Board {
    private String[][] board;

    public Board() {
        this.board = new String[10][10]; // mudar para constantes
        int numberOfRows = 1;
        int numberOfCols = 1;
        drawNumbersAndWater(numberOfRows, numberOfCols);
        addShip();
    }

    private void drawNumbersAndWater(int numberOfRows, int numberOfCols) {
        for (int i = 0; i < this.board.length; i++) {
            for (int i1 = 0; i1 < this.board[i].length; i1++) {
                if(i==0 && i1 == 0){
                    this.board[i][i1] = " ";
                    continue;
                }
                if(i == 0){
                    this.board[i][i1] = String.valueOf(numberOfRows);
                    numberOfRows++;
                    continue;
                }
                if(i1 == 0){
                    this.board[i][i1] = String.valueOf(numberOfCols);
                    numberOfCols++;
                    continue;
                }
                this.board[i][i1] = "~";
            }
        }
    }

    // NUNO
    public void addShip(){
        //buscar barcos aos enums
        // 1 de 5 blocos, 1 de 4, 2 de 3, 1 de 2
        //Ship ship = new Ship(1,"x", new Position(1,2)); mudar 1 e 2 para random
        Ship ship = new Ship(1,new Position(5,5));
        this.board[ship.getPosition().getX()][ship.getPosition().getY()] = "#";

    }

    public String[][] getBoard() {
        return board;
    }

    public static void main(String[] args) {
        Board b = new Board();
        for (int i = 0; i < b.getBoard().length; i++) {
            for (int i1 = 0; i1 < b.getBoard()[i].length; i1++) {
                System.out.print(" "+b.getBoard()[i][i1]+" ");
            }
            System.out.print("\n");
        }
    }

}
