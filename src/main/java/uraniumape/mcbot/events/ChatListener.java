package uraniumape.mcbot.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import uraniumape.mcbot.Bot;
import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.script.responses.Message;
import uraniumape.mcbot.storage.Bots;

import javax.script.ScriptException;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        getServer().getScheduler().runTaskAsynchronously(MCBot.getInstance(), () -> {
            Message message = new Message(e.getPlayer(), e.getMessage());
            List<Bot> bots = Bots.getInstance().getBots();

            for(Bot bot : bots) {
                bot.readMessage(message);
            }
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        getServer().getScheduler().runTaskAsynchronously(MCBot.getInstance(), () -> {
            List<Bot> bots = Bots.getInstance().getBots();

            for(Bot bot : bots) {
                bot.onPlayerJoin(e.getPlayer());
            }
        });
    }
}
