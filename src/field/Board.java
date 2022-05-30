package field;

import gameobjects.Ship;

public class Board {
    private Position[][] position;

    public Board(Position[][] position) {
        this.position = new Position[10][10]; // mudar para constantes
        addShip();
    }

    // NUNO
    public void addShip(){
        //buscar barcos aos enums
        // 1 de 5 blocos, 1 de 4, 2 de 3, 1 de 2
        //Ship ship = new Ship(1,"x", new Position(1,2)); mudar 1 e 2 para random
    }
}
