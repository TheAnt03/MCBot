package uraniumape.mcbot.log;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ServerLogger implements BotLogger {
    private final String prefix = "[" + ChatColor.DARK_RED + "MCBotJS" + ChatColor.WHITE + "]";
    @Override
    public void logInfo(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + " " + message);
    }

    @Override
    public void logError(String message) {
        Bukkit.getConsoleSender().sendMessage(prefix + ChatColor.RED + " " + message);
    }
}
