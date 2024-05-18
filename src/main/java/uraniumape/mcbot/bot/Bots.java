package uraniumape.mcbot.bot;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.database.DBDriver;
import uraniumape.mcbot.database.connections.MySQLConnection;
import uraniumape.mcbot.log.BotLogger;
import uraniumape.mcbot.script.ScriptLoader;

public class Bots {
    private List<Bot> bots;
    private final MCBot mcBot;
    private final ScriptLoader scriptLoader;
    private final BotLogger logger;

    public Bots(BotLogger logger, MCBot mcBot) {
        this.mcBot = mcBot;
        this.scriptLoader = new ScriptLoader(logger, mcBot);
        this.logger = logger;
    }

    public void loadBots() {
        this.logger.logInfo("Loading Scripts");
        this.bots = new ArrayList<>();

        FileConfiguration config = mcBot.getConfig();
        ConfigurationSection botsSection = config.getConfigurationSection("bots");

        if(botsSection != null){
            try {
                createBots(botsSection);
            } catch (IllegalArgumentException e) {
                this.logger.logError("You may have used an incorrect database driver. Supported types are: SQLite, MySQL");
            }
        }

        this.logger.logInfo("Script load completed");
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
            Bot bot;
            String name = botsSection.getString(key + ".name");
            String script = botsSection.getString(key + ".script");
            String scriptContent = scriptLoader.loadScript(script);
            String driverString = botsSection.getString(key + ".dbDriver");
            boolean useDatabase = botsSection.getBoolean(key + ".useDatabase");
            boolean autoCommit = botsSection.getBoolean(key + ".autoCommit");
            boolean noDriverString = useDatabase && driverString == null;

            if(noDriverString) {
                this.logger.logError("Bot " + name + " has no driver string, but has useDatabase as true. Please provide the database type you will be using");
                continue;
            }

            if(useDatabase) {
                DBDriver driver = DBDriver.valueOf(driverString);

                bot = new Bot(this.logger, this.mcBot, name, scriptContent, autoCommit, driver);
            } else {
                bot = new Bot(this.logger, this.mcBot, name, scriptContent);
            }

            bots.add(bot);
        }
    }
}