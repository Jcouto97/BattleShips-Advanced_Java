package commands;

import field.Board;
import network.GameServer;

public class RandomHandler implements CommandHandler{
    /*
    To randomize the player board:
    1º Sets his board to null;
    2º Creates a new Board
    3º Sets player board to new board
     */
    @Override
    public void command(GameServer.PlayerHandler player, GameServer server) {
        player.setBoard(null);
        Board newBoard = new Board();
        player.setBoard(newBoard);
        player.setMaxNumberOfRandomBoards(player.getMaxNumberOfRandomBoards() - 1);
    }
}
