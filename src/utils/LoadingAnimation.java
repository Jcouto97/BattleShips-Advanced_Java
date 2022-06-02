package utils;

import game.GameServer;

public class LoadingAnimation {
    private String lastLine = "";

    /*
    This method is what allows each line to reset and draw again
     */
    public void print(String line, GameServer.PlayerHandler playerHandler) {
        //clear the last line if longer
        if (lastLine.length() > line.length()) {
            String temp = "";
            for (int i = 0; i < lastLine.length(); i++) {
                temp += " ";
            }
            /*if (temp.length() > 1)
                playerHandler.send("specialCode " + temp);*/

        }
        playerHandler.send("specialCode " + line);

        lastLine = line;
    }

    private int animationCounter;

    public void animate(String line, GameServer.PlayerHandler playerHandler) {
        switch (animationCounter) {
            case 1:
                print(line + " [ .. ]",playerHandler);
                break;
            case 2:
                print(line + " [ ... ]",playerHandler);
                break;
            default:
                animationCounter = 0;
                print(line + " [ . ]",playerHandler);
        }
        animationCounter++;
    }

    public String animationTime(LoadingAnimation loadingAnimation, int time, GameServer.PlayerHandler playerHandler) {
        for (int i = time; i >= 0; i--) {
            loadingAnimation.animate("You have " + i + " seconds left to attack",playerHandler);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "You were too slow! The AI randomly attacked coordinates # #!";
    }
}
