package uraniumape.mcbot.exceptions;

public class NoConnectionString extends Exception{
    public NoConnectionString(String botName) {
        super(botName + " has no connection string. Cannot create MySQL connection.");
    }
}
