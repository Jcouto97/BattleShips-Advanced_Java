package commands;

public enum Command {
    ATTACK("/attack", new AttackHandler()),
    QUIT ("/quit", new QuitHandler()),
    RANDOM ("/random", new RandomHandler()),
    READY ("/ready", new ReadyHandler());

    private String description;
    private CommandHandler handler;

    /*
    Chat commands
     */
    Command(String description, CommandHandler handler){
        this.description = description;
        this.handler = handler;
    }

    /*
    Gets a command from the description given
     */
    public static Command getCommandFromDescription(String description) {
        for (int i = 0; i < Command.values().length; i++) {
            if(Command.values()[i].description.equals(description)) {
                return Command.values()[i];
            }
        }
        return null;
    }

    public CommandHandler getHandler() {
        return handler;
    }
}
