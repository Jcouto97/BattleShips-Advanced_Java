package field;

import colors.Colors;

public enum BoardSymbols {
    EMPTY_SPACE(" "),
    WATER(Colors.BLUE +"░"+ colors.Colors.RESET),
    BOAT_PIECE(Colors.YELLOW +"█"+ Colors.RESET),
    BOAT_PIECE_HIT(Colors.RED +"╬"+ Colors.RESET),
    WATER_HIT(Colors.BLACK_BRIGHT+"■"+ Colors.RESET),
    LINE_BREAK("\n"),
    OUT_OFF_BOUNDS("Out of bounds"),
    SAME_POSITION("Same position");
    private final String symbol;

    BoardSymbols(String symbol) {
        this.symbol = symbol;
    }

    public String getSymbol() {
        return symbol;
    }

}
