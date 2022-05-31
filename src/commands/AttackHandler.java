package commands;

import field.Position;
import network.GameServer;

public class AttackHandler implements CommandHandler{
    @Override
    public void command(GameServer.PlayerHandler player, GameServer server) {
        String[] coordinates = player.getMessage().split(" ");

        // verificar se o que o player escreveu são coordenadas (integers)
        if (!(isInt(coordinates[1]) && isInt(coordinates[2]))){
            return;
        }
        Position hitPosition = new Position(Integer.parseInt(coordinates[1]), Integer.parseInt(coordinates[2]));

        GameServer gameServer = new GameServer();
        for (GameServer.PlayerHandler playerName : gameServer.getPlayerList()) {
            if (!player.getName().equals(playerName.getName())) {
                playerName.getBoard().hit(hitPosition);
            }
        }
    }

    public boolean isInt(String coordinate){
        try {
            Integer.parseInt(coordinate);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
