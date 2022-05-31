package gameobjects;

import field.Position;

import java.util.ArrayList;
import java.util.List;


public class Ship {
    private int size; // size of the ship
    private Position head; //   private String axis;
    private List<Position> fullShip; // the full ship with positions

    public Ship(int size, Position position) {
        fullShip = new ArrayList<>();
        this.size = size;
        this.head = position;// this.axis = axis;
        // get a random side to generate the ship body
        ShipsENUM ship = ShipsENUM.values()[(int) Math.floor(Math.random()*ShipsENUM.values().length)];
        fullShip.add(head); // add the head of the ship to body
        Position currentPosition = head; // currentPosition variable that will change in the for loop
        /*
         Iterates according to the size given on the constructor
          and inserts a new updated position to the body according to the chosen enum
         */
        for (int i = 0; i < size-1; i++) {

            // variable receives a new position updated according to the chosen enum.
            Position newPosition = new Position(currentPosition.getX()+ship.getAxisX(),currentPosition.getY()+ship.getAxisY());
            fullShip.add(newPosition); // adds the position the body
            currentPosition = newPosition; // changes currentPosition variable with the new position variable and repeats until the size is met
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