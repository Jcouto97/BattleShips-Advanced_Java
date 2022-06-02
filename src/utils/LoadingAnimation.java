package utils;

public class LoadingAnimation {
    private String lastLine = "";

    /*
    This method is what allows each line to reset and draw again
     */
    public void print(String line) {
        //clear the last line if longer
        if (lastLine.length() > line.length()) {
            String temp = "";
            for (int i = 0; i < lastLine.length(); i++) {
                temp += " ";
            }
            if (temp.length() > 1)
                System.out.print("\r" + temp);
        }
        System.out.print("\r" + line);
        lastLine = line;
    }

    private int animationCounter;

    public void animate(String line) {
        switch (animationCounter) {
            case 1:
                print(line + " [ .. ]");
                break;
            case 2:
                print(line + " [ ... ]");
                break;
            default:
                animationCounter = 0;
                print(line + " [ . ]");
        }
        animationCounter++;
    }

    public String animationTime(LoadingAnimation loadingAnimation, int time) {
        for (int i = time; i >= 0; i--) {
            loadingAnimation.animate("You have " + i + " seconds left to attack");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return "You were too slow! The AI randomly attacked coordinates # #!";
    }
}
