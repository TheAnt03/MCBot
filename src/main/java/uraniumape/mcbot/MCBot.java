package uraniumape.mcbot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import uraniumape.mcbot.commands.MCBotCommand;
import uraniumape.mcbot.events.ChatListener;

public class MCBot extends JavaPlugin {
    public static MCBot instance;
    public static final String prefix = "[" + ChatColor.DARK_RED + "MCBot" + ChatColor.WHITE + "]";

    FileConfiguration config;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ChatListener(), this);
        this.getCommand("mcbot").setExecutor(new MCBotCommand());

        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + this.getName());
        instance = this;
    }

    public static MCBot getInstance() {
        return instance;
    }
}