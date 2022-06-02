package field;

public enum ColumnENUM {
    N ("NULL", 0),
    A ("A", 1),
    B ("B", 2),
    C ("C", 3),
    D ("D", 4),
    E ("E", 5),
    F ("F", 6),
    G ("G", 7),
    H ("H", 8),
    I ("I", 9);

    private final String letter;
    private final int value;

    ColumnENUM(String letter, int value) {
        this.letter = letter;
        this.value = value;
    }

    public String getLetter() {
        return letter;
    }

    public int getValue() {
        return value;
    }
}
