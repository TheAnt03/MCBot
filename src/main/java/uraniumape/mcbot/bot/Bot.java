package uraniumape.mcbot.bot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.database.DBDriver;
import uraniumape.mcbot.database.DatabaseConnection;
import uraniumape.mcbot.database.connections.MySQLConnection;
import uraniumape.mcbot.database.connections.SQLiteConnection;
import uraniumape.mcbot.log.BotLogger;
import uraniumape.mcbot.script.parameters.Message;

import javax.script.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.regex.Pattern;

public class Bot  {
    private MCBot mcBot;
    private BotLogger logger;
    private ScriptEngine engine;
    private String script = "";
    private String name;
    private String botPrefix;
    private Server server;
    private DatabaseConnection connection;
    private boolean useDatabase;
    private static final Pattern STRIP_AMP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('&') + "[0-9A-FK-ORX]");

    public Bot(BotLogger logger, MCBot mcBot, String name, String script) {
        this.instantiateBot(logger, mcBot, name, script);
    }

    public Bot(BotLogger logger, MCBot mcBot, String name, String script, boolean autoCommit, DBDriver driver) {
        this.instantiateBot(logger, mcBot, name, script);
        this.useDatabase = true;

        switch(driver) {
            case SQLite:
                connection = new SQLiteConnection(logger, mcBot, name, autoCommit);
                break;
            case MYSQL:
                String connectionString = mcBot.getConfig().getString("connection_string");
                connection = new MySQLConnection(logger, name, autoCommit, connectionString);
                break;
        }

        this.invoke("databaseInit", new Object[]{this, connection});
    }

    /**
     * Initialize the bot
     *
     * @param name - The bots in game name
     * @param script - The script js as a string
     */
    private void instantiateBot(BotLogger logger, MCBot mcBot, String name, String script) {
        this.logger = logger;
        this.mcBot = mcBot;
        this.name = name;
        this.botPrefix = "[" + ChatColor.translateAlternateColorCodes('&', name) + "§f]";
        this.script = script;
        this.server = Bukkit.getServer();
        this.useDatabase = false;

        engine = new NashornScriptEngineFactory().getScriptEngine();

        try {
            engine.eval(this.script);
            this.logger.logInfo("Bot " + this.name + " initialized!");
        } catch (ScriptException e) {
            this.logger.logError("There was an error when initializing script for bot " + name + ChatColor.WHITE + "\n:" + e.getMessage());
        }
    }

    /**
     * Sends a message as the bot
     *
     * @param message - The message you want to send
     */
    public void sendMessage(String message) {
        Bukkit.broadcastMessage(botPrefix + " " + message);
    }

    /**
     * Sends get request to path
     *
     * @param path - The request path
     * @return - The result as a String
     */
    public String get(String path) {
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(URI.create(path)).build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            this.logger.logError("Could not run get() for bot " + this.getName() + ChatColor.WHITE + "\n:" + e.getMessage());
            throw new RuntimeException(e);
        }

        return response.body();
    }


    /**
     * Creates an ItemStack and passes it back.
     *
     * @param type - The material
     * @param amount - How many
     * @return - The ItemStack
     */
    public ItemStack createItem(String type, int amount) {
        return new ItemStack(Material.valueOf(type.toUpperCase()), amount);
    }


    /**
     * Execute a query which does not return anything
     *
     * @param connection - The connection object
     * @param query - The query itself
     */
    public void executeUpdate(DatabaseConnection connection, String query) {
        Connection conn = connection.getConnection();

        try {
            Statement statement = conn.createStatement();
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            this.logger.logError("Could not run executeUpdate()");
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute a query which contains a ResultSet
     *
     * @param connection - The connection object
     * @param query - The query itself
     * @return - The ResultSet
     */
    public ResultSet executeQuery(DatabaseConnection connection, String query) {
        ResultSet rs;
        Connection conn = connection.getConnection();

        try {
            Statement statement = conn.createStatement();
            rs = statement.executeQuery(query);

        } catch (SQLException e) {
            this.logger.logError("Could not run executeUpdate()");
            throw new RuntimeException(e);
        }

        return rs;
    }


    /**
     * Invoke a function inside the script
     *
     * @param function - The function you would like to run
     * @param args - An object array of args
     */
    public void invoke(String function, Object[] args) {
        Invocable invocable = (Invocable) engine;

        try {
            Object funcResult = invocable.invokeFunction(function, args);
        } catch (ScriptException e) {
            this.logger.logError("Could not run onPlayerLeave() for bot " + this.getName() + ChatColor.WHITE + "\n:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            // Do nothing
        }
    }

    /**
     * Returns the name
     *
     * @return The bots name
     */
    public String getName() {
        return name;
    }

    public Server getServer() {
        return server;
    }

    public Object[] getPlayers() {
        return server.getOnlinePlayers().toArray();
    }

    public DatabaseConnection getDatabaseConnection() {
        if(!this.useDatabase) {
            this.logger.logError("Database is not enabled on this bot");
            return null;
        }

        return this.connection;
    }
    /**
     * Returns name without color
     *
     * @return The name with color codes stripped
     */
    public String getNameNoColor() {
        String name = this.name;
        name = STRIP_AMP_COLOR_PATTERN.matcher(name).replaceAll("");
        name = ChatColor.stripColor(name);

        return name;
    }


}
