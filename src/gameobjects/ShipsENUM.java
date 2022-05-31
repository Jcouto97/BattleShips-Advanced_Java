package gameobjects;
/*
This makes the body of the ship
Takes the Head of the ship and creates a body randomized to one the enums directions
 */

public enum ShipsENUM {
    SHIP_LEFT(-1,0),
    SHIP_RIGHT(1,0),
    SHIP_UP(0,1),
    SHIP_DOWN(0,-1),
    SHIP_TOP_LEFT(-1,1),
    SHIP_TOP_RIGHT(1,1),
    SHIP_BOT_LEFT(-1,-1),
    SHIP_BOT_RIGHT(1,-1);

/*
Coordinates
 */
    private final int AxisX;
    private final int AxisY;


    ShipsENUM(int axisX, int axisY) {
        AxisX = axisX;
        AxisY = axisY;
    }

    public int getAxisX() {
        return AxisX;
    }

    public int getAxisY() {
        return AxisY;
    }
}
