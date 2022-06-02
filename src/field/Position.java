package field;

import java.util.Objects;

public class Position {
    private int x, y;

    /**
     *
     * @param x this is the x-axis paramether
     * @param y this is the y-axis paramether
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    /**
     * Equals and hashCode methods overriden to compare the position objects and attributes
     * @param o object paramether compared
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}