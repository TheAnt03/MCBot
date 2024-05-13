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

    private void createBots(ConfigurationSection botsSection) {
        for (String key : botsSection.getKeys(false)) {
            String name = botsSection.getString(key + ".name");
            String script = botsSection.getString(key + ".script");
            String scriptContent = ScriptLoader.loadScript(script);
            boolean useDatabase = botsSection.getBoolean(key + ".useDatabase");
            boolean autoCommit = botsSection.getBoolean(key + ".autoCommit");

            Bot bot;
            if(useDatabase) {
                bot = new Bot(name, scriptContent, autoCommit);
            } else {
                bot = new Bot(name, scriptContent);
            }

            bots.add(bot);
        }
    }

    public void loadBots() {
        Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Loading Scripts");
        this.bots = new ArrayList<>();

        FileConfiguration config = MCBot.getInstance().getConfig();
        ConfigurationSection botsSection = config.getConfigurationSection("bots");

        if(botsSection != null){
            createBots(botsSection);
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