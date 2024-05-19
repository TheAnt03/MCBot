package uraniumape.mcbot;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import uraniumape.mcbot.commands.MCBotCommand;
import uraniumape.mcbot.events.ChatListener;
import uraniumape.mcbot.bot.Bots;
import uraniumape.mcbot.log.BotLogger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MCBot extends JavaPlugin {
    public static MCBot instance;
    private Bots bots;
    private BotLogger logger;

    private void generateFolder(String path) {
        if (!Files.isDirectory(Paths.get(getDataFolder() + path))) {
            File dir = new File(getDataFolder() + path);

            if(dir.mkdir()) {
                this.logger.logInfo("Created " + path + " directory!");
            } else {
                this.logger.logError("Could not make " + path + " directory!");
            }
        }
    }

    @Override
    public void onEnable() {
        this.logger = new BotLogger(ChatColor.DARK_RED + this.getDescription().getName());
        this.saveDefaultConfig();
        this.generateFolder("/scripts/");
        this.generateFolder("/databases/");
        this.bots = new Bots(logger, this);
        this.bots.loadBots();

        this.getCommand("mcbot").setExecutor(new MCBotCommand(logger,this));
        this.getServer().getPluginManager().registerEvents(new ChatListener(this, this.bots), this);
        this.logger.logInfo("Enabled MCBot");
    }

    @Override
    public void onDisable() {
        this.bots.closePools();
    }

    public void reloadBots() {
        this.bots.closePools();
        this.bots.loadBots();
    }
}