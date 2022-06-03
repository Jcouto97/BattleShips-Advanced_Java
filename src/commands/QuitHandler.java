package commands;

import game.GameServer;
import static utils.asciiArt.*;

public class QuitHandler implements CommandHandler{

    /**
     * Removes player from playerList
     * Closes his socket
     */
    @Override
    public void command(GameServer.PlayerHandler player, GameServer server) {
        player.send(LOSER);
        player.close();
    }
}
