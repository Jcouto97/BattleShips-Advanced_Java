package utils;

import colors.Colors;

import java.awt.*;

public class asciiArt {
    public final static String BATTLESHIP =
                    "\n" +
                    "$$$$$$$\\           $$\\    $$\\    $$\\                 $$$$$$\\ $$\\      $$\\                   \n" +
                    "$$  __$$\\          $$ |   $$ |   $$ |               $$  __$$\\$$ |     \\__|                  \n" +
                    "$$ |  $$ |$$$$$$\\$$$$$$\\$$$$$$\\  $$ |$$$$$$\\        $$ /  \\__$$$$$$$\\ $$\\ $$$$$$\\  $$$$$$$\\ \n" +
                    "$$$$$$$\\ |\\____$$\\_$$  _\\_$$  _| $$ $$  __$$\\       \\$$$$$$\\ $$  __$$\\$$ $$  __$$\\$$  _____|\n" +
                    "$$  __$$\\ $$$$$$$ |$$ |   $$ |   $$ $$$$$$$$ |       \\____$$\\$$ |  $$ $$ $$ /  $$ \\$$$$$$\\  \n" +
                    "$$ |  $$ $$  __$$ |$$ |$$\\$$ |$$\\$$ $$   ____|      $$\\   $$ $$ |  $$ $$ $$ |  $$ |\\____$$\\ \n" +
                    "$$$$$$$  \\$$$$$$$ |\\$$$$  \\$$$$  $$ \\$$$$$$$\\       \\$$$$$$  $$ |  $$ $$ $$$$$$$  $$$$$$$  |\n" +
                    "\\_______/ \\_______| \\____/ \\____/\\__|\\_______|       \\______/\\__|  \\__\\__$$  ____/\\_______/ \n" +
                    "                                                                         $$ |               \n" +
                    "                                                                         $$ |               \n" +
                    "                                                                         \\__|               ";

    public final static String PLANE =
                            "\t\t\t                                         |\n" +
                            "\t\t\t                  |                      |\n" +
                            "\t\t\t                  |                    -/_\\-\n" +
                            "\t\t\t                -/_\\-   ______________(/ . \\)______________\n" +
                            "\t\t\t   ____________(/ . \\)_____________    \\___/"+Colors.RED+"     <>\n"+Colors.RESET +
                            "\t\t\t   "+  Colors.RED+"<>"+Colors.RESET+"           \\___/"+ Colors.RED+"     <>    <>\n"+Colors.RESET +
                            "\t\t"+Colors.WHITE+" swoosh"+Colors.RESET+Colors.MAGENTA+"                                                   ||        \n"+Colors.RESET +
                            "\t\t\t"+Colors.MAGENTA+"      ||"+Colors.RESET+Colors.WHITE+"                           "+Colors.RESET+Colors.GREEN+"|\\                 "+Colors.RESET+Colors.RED+"<>"+Colors.RESET + Colors.WHITE+"   BIG BOOM\n"+Colors.RESET +
                            "\t\t\t"+Colors.RED+"      <>"+Colors.RESET+Colors.WHITE+"           "+Colors.RESET+Colors.GREEN+"                ---"+Colors.RESET + Colors.WHITE+"                    INCOMING!!!\n"+Colors.RESET +
                            Colors.GREEN+"\t\t                                     / | [\n" +
                            "\t\t                              !      | |||\n" +
                            "\t\t                            _/|     _/|-++'\n" +
                            "\t\t                        +  +--|    |--|--|_ |-\n" +
                            "\t\t                     { /|__|  |/\\__|  |--- |||__/\n" +
                            "\t\t                    +---------------___[}-_===_.'____                       \n" +
                            "\t\t                ____`-' ||___-{]_| _[}-  |     |_[___\\==--                 _\n" +
                            "\t\t __..._____--==/___]_|__|_____________________________[___\\==--____,------' \\ \n" +
                            "\t\t|                                                            MINDSWAP SS    /\n" +
                            "\t\t \\_________________________________________________________________________|\n"+Colors.RESET+
                            Colors.BLUE+"~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                                    "   ~   ~      ~           ~   ~               ~               ~   ~           ~      ~   ~   \n"+
                                    "~         ~         ~                 ~               ~                 ~         ~         ~\n"+Colors.RESET;

    public final static String LOSER = Colors.RED + "\t\t _                     \n" +
            "\t\t| |                    \n" +
            "\t\t| | ___  ___  ___ _ __ \n" +
            "\t\t| |/ _ \\/ __|/ _ \\ '__|\n" +
            "\t\t| | (_) \\__ \\  __/ |   \n" +
            "\t\t|_|\\___/|___/\\___|_|  " + Colors.RESET;
}
