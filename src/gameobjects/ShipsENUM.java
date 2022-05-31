package gameobjects;
/*
This make the body of the ship
take the Head of the ship and
creates a body randomize to one the enums.
 */
public enum ShipsENUM {
    SHIP_LEFT(-1,0),
    SHIP_RIGHT(1,0),
    SHIP_UP(0,1),
    SHIP_DOWN(0,-1);
//Coords to generate the body
    private final int AxisX;
    private final int AxisY;

    // constructor
    ShipsENUM(int axisX, int axisY) {
        AxisX = axisX;
        AxisY = axisY;
    }

    // get the X  axis
    public int getAxisX() {
        return AxisX;
    }
// get the Y axis
    public int getAxisY() {
        return AxisY;
    }
}
