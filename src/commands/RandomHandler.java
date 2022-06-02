package commands;

import field.Board;
import game.GameServer;

public class RandomHandler implements CommandHandler{

    /**
     * To randomize the player board;
     * Sets his board to null;
     * Creates a new Board;
     * Sets player board to new board;
     */
    @Override
    public void command(GameServer.PlayerHandler player, GameServer server) {
        if(player.getMaxNumberOfRandomBoards() == 0)return;
        player.setBoard(null);
        Board newBoard = new Board();
        player.setBoard(newBoard);
        player.setMaxNumberOfRandomBoards(player.getMaxNumberOfRandomBoards() - 1);
    }
}
