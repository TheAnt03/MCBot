package uraniumape.mcbot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.openjdk.nashorn.api.scripting.NashornScriptEngineFactory;
import uraniumape.mcbot.script.responses.Message;

import javax.script.*;

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
    }

    public void sendMessage(String message) {
        Bukkit.broadcastMessage(botPrefix + " " + message);
    }


    public void readMessage(Message message) throws ScriptException, NoSuchMethodException {
        engine.eval(this.script);

        Object[] args = {this, message};
        Invocable invocable = (Invocable) engine;
        Object funcResult = invocable.invokeFunction("onMessageSend", args);
    }

    public String getName() {
        return name;
    }

    public Server getServer() {
        return server;
    }

}
