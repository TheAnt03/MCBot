package uraniumape.mcbot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import uraniumape.mcbot.script.responses.Message;

import javax.script.*;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Bot  {
    private ScriptEngine engine;
    private String script = "";
    private String name;
    private String botPrefix;
    private Server server;


    public Bot(String name, String script) {
        this.name = name;
        this.botPrefix = "[" + ChatColor.translateAlternateColorCodes('&', name) + "§f]";
        this.script = script;
        this.server = MCBot.getInstance().getServer();
        engine = new NashornScriptEngineFactory().getScriptEngine();

        try {
            engine.eval(this.script);
        } catch (ScriptException e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " There was an error when initializing script for bot " + name + ChatColor.WHITE + "\n:" + e.getMessage());
        }
    }

    public void sendMessage(String message) {
        Bukkit.broadcastMessage(botPrefix + " " + message);
    }

    public String get(String path) {
        // create a client
        HttpResponse<String> response;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(path))
                .header("accept", "application/json")
                .build();

        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (InterruptedException | IOException e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not run get() for bot " + this.getName() + ChatColor.WHITE + "\n:" + e.getMessage());
            throw new RuntimeException(e);
        }

        return response.body();
    }


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

    public String getName() {
        return name;
    }

    public Server getServer() {
        return server;
    }
}
