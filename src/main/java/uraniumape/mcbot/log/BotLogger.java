package uraniumape.mcbot.log;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class BotLogger {
    private String prefix;

    public BotLogger(String prefix) {
        this.prefix = ChatColor.WHITE + "[" + ChatColor.translateAlternateColorCodes('&', prefix) + ChatColor.WHITE + "]";
    }

    public void logInfo(String message) {
        Bukkit.getConsoleSender().sendMessage(this.prefix + " " + message);
    }

    public void logError(String message) {
        Bukkit.getConsoleSender().sendMessage(this.prefix + " " + ChatColor.RED + message);
    }
}
