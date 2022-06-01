package commands;

import field.Board;
import network.GameServer;

public class RandomHandler implements CommandHandler{
    @Override
    public void command(GameServer.PlayerHandler player, GameServer server) {
        /*
        player get board
        == null
        fazer nova
         */
        player.setBoard(null);
        Board newBoard = new Board();
        player.setBoard(newBoard);
    }
}
