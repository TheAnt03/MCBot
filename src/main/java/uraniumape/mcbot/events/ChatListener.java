package uraniumape.mcbot.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import uraniumape.mcbot.bot.Bot;
import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.script.parameters.Message;
import uraniumape.mcbot.bot.Bots;

import java.util.List;

import static org.bukkit.Bukkit.getServer;

public class ChatListener implements Listener {
    private final MCBot mcBot;
    private final Bots bots;

    public ChatListener(MCBot mcBot, Bots bots) {
        this.mcBot = mcBot;
        this.bots = bots;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        getServer().getScheduler().runTaskAsynchronously(this.mcBot, () -> {
            Message message = new Message(e.getPlayer(), e.getMessage());

            for (Bot bot : this.bots.getBots()) {
                bot.invoke("onMessageSend", new Object[]{bot, message});
            }
        });
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        getServer().getScheduler().runTaskAsynchronously(this.mcBot, () -> {
            for (Bot bot : this.bots.getBots()) {
                bot.invoke("onPlayerJoin", new Object[]{bot, e.getPlayer()});
            }
        });
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        getServer().getScheduler().runTaskAsynchronously(this.mcBot, () -> {
            for (Bot bot : this.bots.getBots()) {
                bot.invoke("onPlayerLeave", new Object[]{bot, e.getPlayer()});
            }
        });
    }
}
