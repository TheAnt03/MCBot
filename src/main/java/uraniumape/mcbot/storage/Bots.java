package uraniumape.mcbot.storage;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import uraniumape.mcbot.Bot;
import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.script.ScriptLoader;

public class Bots {
    private static Bots instance;
    private List<Bot> bots;

    private Bots() {
        this.loadBots();
    }

    public void loadBots() {
        Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Loading Scripts");
        this.bots = new ArrayList<>();

        FileConfiguration config = MCBot.getInstance().getConfig();
        ConfigurationSection botsSection = config.getConfigurationSection("bots");

        for (String key : botsSection.getKeys(false)) {
            String name = botsSection.getString(key + ".name");
            String script = botsSection.getString(key + ".script");
            String scriptContent = ScriptLoader.loadScript(script);

            Bot bot = new Bot(name, scriptContent);

            bots.add(bot);
        }

        Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Script load completed");
    }

    public static Bots getInstance() {
        if (instance == null) {
            instance = new Bots();
        }

        return instance;
    }


    public List<Bot> getBots() {
        return this.bots;
    }
}