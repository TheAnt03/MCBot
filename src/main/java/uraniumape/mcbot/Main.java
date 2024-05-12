package uraniumape.mcbot;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import uraniumape.mcbot.events.ChatListener;

public class Main extends JavaPlugin {
    public static Main instance;

    FileConfiguration config;

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        getServer().getPluginManager().registerEvents(new ChatListener(), this);

        Bukkit.getLogger().info(ChatColor.GREEN + "Enabled " + this.getName());
        instance = this;
    }

    public static Main getInstance() {
        return instance;
    }
}