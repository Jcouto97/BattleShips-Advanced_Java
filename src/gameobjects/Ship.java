package gameobjects;

import field.Position;

public class Ship {
    private int size;
    private String axis;
    private Position position;

    public Ship(int size, String axis, Position position) {
        this.size = size;
        this.axis = axis;
        this.position = position;
    }
}