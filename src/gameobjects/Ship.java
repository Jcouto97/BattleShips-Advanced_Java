package gameobjects;

import field.Position;

import java.util.ArrayList;
import java.util.List;


public class Ship {
    private int size; // size of the ship
    private Position head; //   private String axis;
    private List<Position> fullShip; // the full ship with positions

    /*
    Initialize ship with size and position (head);
    Enum for beginning direction;
    Add the head to the fullShip (empty list);
    CurrentPosition variable that will change in the for loop;
    Iterate according to the size given on the constructor and insert a new updated position to the body according to
    the chosen enum, add position to body and update current position, repeat until size is met;
     */

    public Ship(int size, Position position) {
        fullShip = new ArrayList<>();
        this.size = size;
        this.head = position;
        ShipsENUM shipDirection = ShipsENUM.values()[(int) Math.floor(Math.random()*ShipsENUM.values().length)];
        fullShip.add(head);
        Position currentPosition = head;

        for (int i = 0; i < size-1; i++) {

            Position newPosition = new Position(currentPosition.getX()+shipDirection.getAxisX(),currentPosition.getY()+shipDirection.getAxisY());
            fullShip.add(newPosition);
            currentPosition = newPosition;
        }
    }

    public Position getHead() {
        return head;
    }

    public List<Position> getFullShip() {
        return fullShip;
    }

    public int getSize() {
        return size;
    }
}