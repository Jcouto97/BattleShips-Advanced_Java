package utils;

import colors.Colors;

public class asciiArt {
    public final static String BATTLESHIP =
            "\n" +
                    "$$$$$$$\\   $$$$$$\\ $$$$$$$$\\ $$$$$$$$\\ $$\\       $$$$$$$$\\  $$$$$$\\  $$\\   $$\\ $$$$$$\\ $$$$$$$\\  \n" +
                    "$$  __$$\\ $$  __$$\\\\__$$  __|\\__$$  __|$$ |      $$  _____|$$  __$$\\ $$ |  $$ |\\_$$  _|$$  __$$\\ \n" +
                    "$$ |  $$ |$$ /  $$ |  $$ |      $$ |   $$ |      $$ |      $$ /  \\__|$$ |  $$ |  $$ |  $$ |  $$ |\n" +
                    "$$$$$$$\\ |$$$$$$$$ |  $$ |      $$ |   $$ |      $$$$$\\    \\$$$$$$\\  $$$$$$$$ |  $$ |  $$$$$$$  |\n" +
                    "$$  __$$\\ $$  __$$ |  $$ |      $$ |   $$ |      $$  __|    \\____$$\\ $$  __$$ |  $$ |  $$  ____/ \n" +
                    "$$ |  $$ |$$ |  $$ |  $$ |      $$ |   $$ |      $$ |      $$\\   $$ |$$ |  $$ |  $$ |  $$ |      \n" +
                    "$$$$$$$  |$$ |  $$ |  $$ |      $$ |   $$$$$$$$\\ $$$$$$$$\\ \\$$$$$$  |$$ |  $$ |$$$$$$\\ $$ |      \n" +
                    "\\_______/ \\__|  \\__|  \\__|      \\__|   \\________|\\________| \\______/ \\__|  \\__|\\______|\\__|      \n" +
                    "                                                                                                 ";

    public final static String PLANE =
            "\t\t\t                                         |\n" +
                    "\t\t\t                  |                      |\n" +
                    "\t\t\t                  |                    -/_\\-\n" +
                    "\t\t\t                -/_\\-   ______________(/ C \\)______________\n" +
                    "\t\t\t   ____________(/ D \\)_____________    \\___/" + Colors.RED + "     <>\n" + Colors.RESET +
                    "\t\t\t   " + Colors.RED + "<>" + Colors.RESET + "           \\___/" + Colors.RED + "     <>    <>\n" + Colors.RESET +
                    "\t\t" + Colors.WHITE + " swoosh" + Colors.RESET + Colors.MAGENTA + "                                                   ||        \n" + Colors.RESET +
                    "\t\t\t" + Colors.MAGENTA + "      ||" + Colors.RESET + Colors.WHITE + "                           " + Colors.RESET + Colors.GREEN + "|\\                 " + Colors.RESET + Colors.RED + "<>" + Colors.RESET + Colors.WHITE + "   BIG BOOM\n" + Colors.RESET +
                    "\t\t\t" + Colors.RED + "      <>" + Colors.RESET + Colors.WHITE + "           " + Colors.RESET + Colors.GREEN + "                ---" + Colors.RESET + Colors.WHITE + "                    INCOMING!!!\n" + Colors.RESET +
                    Colors.GREEN + "\t\t                                     / | [\n" +
                    "\t\t                              !      | |||\n" +
                    "\t\t                            _/|     _/|-++'\n" +
                    "\t\t                        +  +--|    |--|--|_ |-\n" +
                    "\t\t                     { /|__|  |/\\__|  |--- |||__/\n" +
                    "\t\t                    +---------------___[}-_===_.'____                       \n" +
                    "\t\t                ____`-' ||___-{]_| _[}-  |     |_[___\\==--                 _\n" +
                    "\t\t __..._____--==/___]_|__|_____________________________[___\\==--____,------' \\ \n" +
                    "\t\t|                                                            MINDSWAP SS    /\n" +
                    "\t\t \\_________________________________________________________________________|\n" + Colors.RESET +
                    Colors.BLUE +
                    "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
                    "   ~   ~      ~           ~   ~               ~               ~   ~           ~      ~   ~   \n" +
                    "~         ~         ~                 ~               ~                 ~         ~         ~\n" + Colors.RESET;

    public final static String START_BUTTON =
            "\n \t\t\t\t\t\t\t┎────────────────────────────────────┒" +
                    "\n \t\t\t\t\t\t\t┃  " + Colors.GREEN + "PRESS ENTER TO START THE GAME :)" + Colors.RESET + "  ┃" +
                    "\n \t\t\t\t\t\t\t┖────────────────────────────────────┚";

    public final static String LOSER = Colors.RED + "  _      ____   ____   _____ ______ _____  \n" +
            " | |    / __ \\ / __ \\ / ____|  ____|  __ \\ \n" +
            " | |   | |  | | |  | | (___ | |__  | |__) |\n" +
            " | |   | |  | | |  | |\\___ \\|  __| |  _  / \n" +
            " | |___| |__| | |__| |____) | |____| | \\ \\ \n" +
            " |______\\____/ \\____/|_____/|______|_|  \\_\\" + Colors.RESET;

    public final static String WINNER = Colors.YELLOW +
            "                 WINNER!\n" +
            " \t\t\t  .-=========-.\n" +
            "              \\'-=======-'/\n" +
            "              _|   .=.   |_\n" +
            "             ((|  {{1}}  |))\n" +
            "              \\|   /|\\   |/\n" +
            "               \\__ '`' __/\n" +
            "                 _`) (`_\n" +
            "               _/_______\\_\n" +
            "              /___________\\" + Colors.RESET;
}