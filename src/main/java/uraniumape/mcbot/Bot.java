package uraniumape.mcbot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import uraniumape.mcbot.database.DatabaseConnection;
import uraniumape.mcbot.script.responses.Message;

import javax.script.*;
import java.io.IOException;
import java.net.ConnectException;
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
    private ScriptEngine engine;
    private String script = "";
    private String name;
    private String botPrefix;
    private Server server;

    private DatabaseConnection connection;
    private boolean useDatabase;

    private static final Pattern STRIP_AMP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('&') + "[0-9A-FK-ORX]");

    public Bot(String name, String script) {
        this.instantiateBot(name, script);
    }

    public Bot(String name, String script, boolean autoCommit) {
        this.instantiateBot(name, script);
        this.useDatabase = true;

        connection = new DatabaseConnection(this.getNameNoColor(), autoCommit);
        databaseInitEvent(connection);
    }

    private void databaseInitEvent(DatabaseConnection connection) {
        Object[] args = {connection.getConnection()};
        Invocable invocable = (Invocable) engine;

        try {
            Object funcResult = invocable.invokeFunction("databaseInit", args);
        } catch (ScriptException e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not run databaseInit() for bot " + this.getName() + ChatColor.WHITE + "\n:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            // Do nothing
        }
    }

    private void instantiateBot(String name, String script) {
        this.name = name;
        this.botPrefix = "[" + ChatColor.translateAlternateColorCodes('&', name) + "§f]";
        this.script = script;
        this.server = MCBot.getInstance().getServer();
        this.useDatabase = false;

        engine = new NashornScriptEngineFactory().getScriptEngine();

        try {
            engine.eval(this.script);
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Bot " + this.name + " initialized!");
        } catch (ScriptException e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " There was an error when initializing script for bot " + name + ChatColor.WHITE + "\n:" + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        Bukkit.broadcastMessage(botPrefix + " " + message);
    }

    public String get(String path) {
        HttpResponse<String> response;
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(URI.create(path)).build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not run get() for bot " + this.getName() + ChatColor.WHITE + "\n:" + e.getMessage());
            throw new RuntimeException(e);
        }

        return response.body();
    }

    public Object[] getPlayers() {
        return server.getOnlinePlayers().toArray();
    }

    public ItemStack createItem(String type, int amount) {
        return new ItemStack(Material.valueOf(type.toUpperCase()), amount);
    }

    public void executeUpdate(Connection connection, String query) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            statement.close();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not run executeUpdate()");
            throw new RuntimeException(e);
        }
    }

    public ResultSet executeQuery(Connection connection, String query) {
        ResultSet rs;

        try {
            Statement statement = connection.createStatement();
            rs = statement.executeQuery(query);
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not run executeUpdate()");
            throw new RuntimeException(e);
        }

        return rs;
    }


    public Connection getDatabaseConnection() {
        if(!this.useDatabase) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Database is not enabled on this bot");
            return null;
        }

        return this.connection.getConnection();
    }

    //TODO: Hide this from JS
    public void readMessage(Message message) {
        Object[] args = {this, message};
        Invocable invocable = (Invocable) engine;

        try {
            Object funcResult = invocable.invokeFunction("onMessageSend", args);
        } catch (ScriptException e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not run readMessage() for bot " + this.getName() + ChatColor.WHITE + "\n:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            // Do nothing
        }
    }

    //TODO: Hide this from JS
    public void onPlayerJoin(Player player) {
        Object[] args = {this, player};
        Invocable invocable = (Invocable) engine;

        try {
            Object funcResult = invocable.invokeFunction("onPlayerJoin", args);
        } catch (ScriptException e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not run onPlayerJoin() for bot " + this.getName() + ChatColor.WHITE + "\n:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            // Do nothing
        }
    }

    //TODO: Hide this from JS
    public void onPlayerLeave(Player player) {
        Object[] args = {this, player};
        Invocable invocable = (Invocable) engine;

        try {
            Object funcResult = invocable.invokeFunction("onPlayerLeave", args);
        } catch (ScriptException e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not run onPlayerLeave() for bot " + this.getName() + ChatColor.WHITE + "\n:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            // Do nothing
        }
    }

    public String getName() {
        return name;
    }

    public String getNameNoColor() {
        String name = this.name;
        name = STRIP_AMP_COLOR_PATTERN.matcher(name).replaceAll("");
        name = ChatColor.stripColor(name);

        return name;
    }

    public Server getServer() {
        return server;
    }
}
