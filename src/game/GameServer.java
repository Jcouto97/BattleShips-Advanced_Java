package game;

import colors.Colors;
import commands.Command;
import field.Board;
import field.ColumnENUM;
import field.Position;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static utils.asciiArt.*;

public class GameServer {
    private ServerSocket serverSocket;
    private ExecutorService service;
    private List<PlayerHandler> playerList;
    private boolean isWaiting;
    private final Object lock2;
    private int gameIds;
    private Thread bot = null;

    /**
     * Constructor that initializes each game with an ID
     * Creates lock object for thread handling
     * isWaiting boolean to false
     */
    public GameServer() {
        this.gameIds = 1;
        this.lock2 = new Object();
        isWaiting = false;
    }

    /*
        Starts server with port as a parameter;
        Starts a thread pool with unlimited thread space;
        Starts a new list were players will be added;
        Adds number of connections of players to the server;
    */

    /**
     * Starts server with port as a parameter;
     * Starts a thread pool with unlimited thread space;
     * Starts a new list were players will be added;
     * Adds number of connections of players to the server;
     * @param port -> server port
     */
    public void start(int port) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.service = Executors.newCachedThreadPool();
            this.playerList = new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int numberOfConnections = 0;

        while (true) {
            ++numberOfConnections;
            acceptConnection(numberOfConnections);
        }
    }

    /**
     * First player creates bot, goes to synchronize and waits for second person that jumps to second if,
     * notifies all and interrupts bot
     * Lock object used for player to wait a notifyAll
     * @param player
     */
    private void waitingRoom(PlayerHandler player) {
        if (!isWaiting) {
            bot();
        }
        if (isWaiting) {
            synchronized (lock2) {
                lock2.notifyAll();
            }
            bot.interrupt();
            chooseAttacker();
            gameIds++;
            return;
        }
        synchronized (lock2) {
            try {
                player.send("Waiting for adversary to connect!");
                isWaiting = true;
                lock2.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            isWaiting = false;
        }
    }

    /**
     * Bot will deploy after 30 seconds if the second player doesn't connect
     */

    private void bot() {
        bot = new Thread(() -> {
            try {
                Thread.sleep(30000);
                new Bot();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        bot.start();
    }

    /**
     * Created new list to place the two players of this game
     * Randomize between the players who starts attacking
     */
    private void chooseAttacker() {
        List<Integer> players = new ArrayList<>();
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).playerGameId == gameIds) {
                players.add(i);
                if(players.size()==2){
                    break;
                }
            }
        }
        playerList.get(players.get((int) Math.floor(Math.random() * 2))).isAttacker = true;
    }

    /**
     * Function used to remove player from list when close() or quit (command) and game ends
     * @param name
     */
    public void removePlayers(String name) {
        for (int i = 0; i < playerList.size(); i++) {
            if (playerList.get(i).name.equals(name)) {
                playerList.remove(i);
                return;
            }
        }
    }

    /**
     * Server socket accepts the players socket;
     * Created new Player with name (using numOfConnections) and his socket;
     * Invoke addPlayer function (below) on this new PlayerHandler instance;
     * @param numberOfConnections give unique id to the player
     */
    public void acceptConnection(int numberOfConnections) {
        try {
            Socket playerSocket = serverSocket.accept();
            addPlayer(new PlayerHandler("Player-".concat(String.valueOf(numberOfConnections)), playerSocket));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * The new PlayerHandler instance will be added to the player list;
     * It's runnable will be submitted to the thread pool
     * @param player receives a playerHandler
     */
    public void addPlayer(PlayerHandler player) {
        playerList.add(player);
        service.submit(player);
        System.out.println(player.getName() + " joined the game!");
    }

    public List<PlayerHandler> getPlayerList() {
        return playerList;
    }

    /**
     * Object that stores each player info
     */
    public class PlayerHandler implements Runnable {
        private final String name;
        private Board board;
        private final Socket playerSocket;
        private final BufferedWriter writer;
        private final BufferedReader reader;
        private String message;
        private boolean isAttacker;
        private final Object lock;
        private final Object messageLock;
        private boolean loser;
        private boolean ready;
        private int maxNumberOfRandomBoards;
        private int playerGameId;
        private boolean winner;

        /**
         *  Constructor that receives a name and a playerSocket
         *  Initializes:
         *  -A board
         *  -BufferedWriter + Reader
         *  -isAttacker (to check player that attacks)
         *  -Looser (check winner)
         *  -Lock (object used to synchronize and wait switch between turns
         *  -Ready (to check if players are ready to start)
         * @param name receives a players name
         * @param playerSocket receives a players socket
         * @throws IOException throws exception if something goes wrong
         */
        public PlayerHandler(String name, Socket playerSocket) throws IOException {
            this.name = name;
            this.board = new Board();
            this.playerSocket = playerSocket;
            this.writer = new BufferedWriter(new OutputStreamWriter(playerSocket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(playerSocket.getInputStream()));
            this.isAttacker = false;
            this.loser = false;
            this.winner = false;
            this.lock = new Object();
            this.messageLock = new Object();
            this.ready = false;
            this.maxNumberOfRandomBoards = 3;
        }

        /**
         * Checks if any of the player ship is alive
         * @return boolean
         */
        public boolean checkIfTheresShipsAlive() {
            for (int i = 0; i < board.getAllTheShips().size(); i++) {
                if (!board.getAllTheShips().get(i).isDead()) {
                    return true;
                }
            }
            return false;
        }

        public void setLoser() {
            this.loser = true;
        }

        public boolean isAttacker() {
            return isAttacker;
        }

        public void setAttacker(boolean attacker) {
            isAttacker = attacker;
        }

        /**
         * Calls startScreen;
         * Checks if players are ready;
         * Checks waitingRoom;
         * Play();
         */
        @Override
        public void run() {
            startScreen();
            readyCheck();
            playerGameId = gameIds;
            waitingRoom(this); //1st player will wait for the 2nd to start the game
            play();
        }

        /**
         * Calls newMessage() and verifyMessage();
         */
        private void play() {
            newMessage();
            verifyMessage();
        }

        /**
         * Verifies which players turn to write message (with synchronizes)
         * Sends boards to player
         * Verifies commands
         * Thread waits until message is sent
         * When message is sent interrupts the wait
         */
        private void verifyMessage() {
            while (!playerSocket.isClosed()) {
                try {
                    send(board.getYourBoard());
                    send(board.getAdversaryBoard());
                    while (!isAttacker) {
                        synchronized (lock) {
                            send("Waiting for adversary attack!");
                            lock.wait();
                        }
                    }
                    send("You are attacking, write /attack and choose your coordinates!\nFormat for coordinates is '# #', example: 'B 4'");
                    Thread waitTime = timer();
                    synchronized (messageLock) {
                        messageLock.wait();
                    }
                    waitTime.interrupt();//
                    waitTime.interrupt();
                    if(message.contains("/random")){
                        continue;
                    }
                    //o que vem do player //blocking method
                    if (isCommand(message)) {
                        dealWithCommand(message);
                        continue;
                    }
                    send("Not a command, try again!");
                } catch (InterruptedException ignored) {
                }
            }
        }

        /**
         * Thread that reads players messages
         */
        private void newMessage() {
            new Thread(() -> {
                while (!playerSocket.isClosed()) {
                    try {
                        message = reader.readLine();
                        synchronized (messageLock) {
                            messageLock.notifyAll();
                        }
                    } catch (IOException e) {
                        try {
                            clientDisconnect();
                            close();
                        } catch (ConcurrentModificationException ignored) {
                        }
                    }
                }
            }).start();
        }

        /**
         * Timer that deploys random position if player doesn't write message
         */
        private Thread timer() {
            ColumnENUM firstParameter;
            int secondParameter;
            Position randomPosition;
            boolean exists;
            do {
                exists = false;
                firstParameter = ColumnENUM.values()[(int) Math.floor(Math.random() * (ColumnENUM.values().length - 1) + 1)];
                secondParameter = (int) Math.floor(Math.random() * (10 - 1) + 1);
                randomPosition = new Position(firstParameter.getValue(), secondParameter);
                for (PlayerHandler playerHandler : playerList) {
                    if (playerHandler.playerGameId == this.playerGameId && !playerHandler.name.equals(this.name)) {
                        if (playerHandler.board.getListOfPreviousAttacks().contains(randomPosition)) {
                            exists = true;
                            break;
                        }
                    }
                }
            } while (exists);

            ColumnENUM finalFirstParameter = firstParameter;
            int finalSecondParameter = secondParameter;
            Thread waitTime = new Thread(() -> {
                try {
                    Thread.sleep(30000);
                    message = "/attack " + finalFirstParameter.getLetter() + " " + finalSecondParameter;
                    synchronized (messageLock) {
                        messageLock.notifyAll();
                    }
                } catch (InterruptedException ignored) {
                }
            });
            waitTime.start();
            return waitTime;
        }

        /**
         * While ready is false
         * Sends player the boards
         * Instructs player how to write commands (ready or random)
         * Deals with command
         */
        private void readyCheck() {
            while (!ready) {
                try {
                    send(board.getYourBoard()); //mostra primeiro a board e depois se queres ready ou random
                    send("Write /ready to start the game!\nWrite /random for a new board!\nNumber of random boards you can still generate: " + this.maxNumberOfRandomBoards);
                    this.message = reader.readLine();
                    if (isCommand(message)) {
                        dealWithCommand(message);
                    }
                } catch (IOException e) {
                    close();
                }
            }
        }

        /**
         * Checks if a player disconnects;
         * Makes other player winner;
         * @throws ConcurrentModificationException
         */
        public void clientDisconnect() throws ConcurrentModificationException {
            for (PlayerHandler players : playerList) {
                if (players.playerGameId == this.playerGameId && !players.name.equals(this.name)) {
                    players.send("\n\n" + WINNER);
                    players.winner = true;
                    players.close();
                }
            }
        }

        /**
         * Uses Ascci art from Utils class to make starting menu
         */
        private void startScreen() {
            String[] a = BATTLESHIP.split("");
            String b = "";
            for (String s : a) {
                if (s.equals("$")) {
                    b += Colors.RED + s;
                    continue;
                }
                b += Colors.BLUE + s;
            }


            send(b + "\n" + Colors.RESET);
            send(PLANE + "\n" + START_BUTTON);
            try {
                this.message = reader.readLine();
            } catch (IOException e) {
                close();
            }
        }

        /**
         * Verifies if message is a command
         */
        public boolean isCommand(String message) {
            return message.startsWith("/");
        }


        /**
         * Deals with the command by spliting the command with coordinates
         * Execute the command, in case it exists
         */
        public void dealWithCommand(String message) {
            String[] words = message.split(" ", 2);
            Command command = Command.getCommandFromDescription(words[0]); //para ter o /attack

            if (command == null) return;  //volta para o run

            command.getHandler().command(this, GameServer.this);  //executar o comando
        }

        /**
         * Deals with buffers
         */
        public void send(String message) {
            try {
                writer.write(message);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {

            }
        }

        /**
         * Closes player socket
         */
        public void close() {
            try {
                removePlayers(this.name);
                playerSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public Object getLock() {
            return lock;
        }

        public String getMessage() {
            return message;
        }

        public String getName() {
            return name;
        }

        public Socket getPlayerSocket() {
            return playerSocket;
        }

        public Board getPlayerBoard() {
            return board;
        }

        public void setReady(boolean ready) {
            this.ready = ready;
        }

        public void setBoard(Board board) {
            this.board = board;
        }

        public int getMaxNumberOfRandomBoards() {
            return maxNumberOfRandomBoards;
        }

        public void setMaxNumberOfRandomBoards(int maxNumberOfRandomBoards) {
            this.maxNumberOfRandomBoards = maxNumberOfRandomBoards;
        }

        public int getPlayerGameId() {
            return playerGameId;
        }
    }
}