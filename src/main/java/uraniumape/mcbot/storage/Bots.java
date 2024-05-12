package uraniumape.mcbot.storage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import uraniumape.mcbot.Bot;
import uraniumape.mcbot.Main;
import uraniumape.mcbot.script.ScriptLoader;

public class Bots {
    private static Bots instance;
    private List<Bot> bots;

    private Bots() {
        FileConfiguration config = Main.getInstance().getConfig();
        ConfigurationSection botsSection = config.getConfigurationSection("bots");
        this.bots = new ArrayList<>();

        for (String key : botsSection.getKeys(false)) {
            String name = botsSection.getString(key + ".name");
            String script = botsSection.getString(key + ".script");
            String scriptContent = ScriptLoader.loadScript(script);

            Bot bot = new Bot(name, scriptContent);

            bots.add(bot);
        }
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