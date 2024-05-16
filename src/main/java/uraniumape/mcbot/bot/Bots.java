package uraniumape.mcbot.bot;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.database.DBDriver;
import uraniumape.mcbot.database.connections.MySQLConnection;
import uraniumape.mcbot.script.ScriptLoader;

public class Bots {
    private static final Bots instance = new Bots();
    private List<Bot> bots;

    public void loadBots() {
        Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Loading Scripts");
        this.bots = new ArrayList<>();

        FileConfiguration config = MCBot.getInstance().getConfig();
        ConfigurationSection botsSection = config.getConfigurationSection("bots");

        if(botsSection != null){
            try {
                createBots(botsSection);
            } catch (IllegalArgumentException e) {
                Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " You may have used an incorrect database driver. Supported types are: SQLite, MySQL");
            }
        }

        Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Script load completed");
    }

    public List<Bot> getBots() {
        return this.bots;
    }

    public void closePools() {
        for(Bot bot : bots) {
            if(bot.getDatabaseConnection() instanceof MySQLConnection) {
                ((MySQLConnection) bot.getDatabaseConnection()).closePool();
            }
        }
    }

    private void createBots(ConfigurationSection botsSection) throws IllegalArgumentException {
        for (String key : botsSection.getKeys(false)) {
            String name = botsSection.getString(key + ".name");
            String script = botsSection.getString(key + ".script");
            String scriptContent = ScriptLoader.loadScript(script);
            boolean useDatabase = botsSection.getBoolean(key + ".useDatabase");
            boolean autoCommit = botsSection.getBoolean(key + ".autoCommit");
            String driverString = botsSection.getString(key + ".dbDriver");

            boolean noDriverString = useDatabase && driverString == null;
            Bot bot;

            if(noDriverString) {
                Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Bot " + name + " has no driver string, but has useDatabase as true. Please provide the database type you will be using");
                continue;
            }

            if(useDatabase) {
                DBDriver driver = DBDriver.valueOf(driverString);

                bot = new Bot(name, scriptContent, autoCommit, driver);
            } else {
                bot = new Bot(name, scriptContent);
            }

            bots.add(bot);
        }
    }

    public static Bots getInstance() {
        return instance;
    }
}