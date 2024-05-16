package uraniumape.mcbot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import uraniumape.mcbot.commands.MCBotCommand;
import uraniumape.mcbot.events.ChatListener;
import uraniumape.mcbot.bot.Bots;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MCBot extends JavaPlugin {
    public static MCBot instance;
    public static String prefix;

    private void generateFolder(String path) {
        if (!Files.isDirectory(Paths.get(getDataFolder() + path))) {
            File dir = new File(getDataFolder() + path);

            if(dir.mkdir()) {
                Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Created " + path + " directory!");
            } else {
                Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not make " + path + " directory!");
            }
        }
    }

    @Override
    public void onEnable() {
        instance = this;
        prefix = "[" + ChatColor.DARK_RED + this.getDescription().getName() + ChatColor.WHITE + "]";
        this.saveDefaultConfig();
        this.getCommand("mcbot").setExecutor(new MCBotCommand());

        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        generateFolder("/scripts/");
        generateFolder("/databases/");

        Bots.getInstance().loadBots();
        Bukkit.getConsoleSender().sendMessage(prefix + " Enabled MCBot");
    }

    @Override
    public void onDisable() {
        Bots.getInstance().closePools();
    }

    public static MCBot getInstance() {
        return instance;
    }
}