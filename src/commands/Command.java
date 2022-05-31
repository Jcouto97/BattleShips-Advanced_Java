package commands;

public enum Command {
    QUIT ("/quit", new QuitHandler()),
    RANDOM ("/random", new RandomHandler()),
    READY ("/ready", new ReadyHandler());

    private String description;
    private CommandHandler handler;

    Command(String description, CommandHandler handler){
        this.description = description;
        this.handler = handler;
    }

    public static Command getCommandFromDescription(String description) {
        return null;
    }

    public CommandHandler getHandler() {
        return handler;
    }
}
