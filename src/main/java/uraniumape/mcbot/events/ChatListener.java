package uraniumape.mcbot.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import uraniumape.mcbot.Bot;
import uraniumape.mcbot.Main;
import uraniumape.mcbot.storage.Bots;

import javax.script.ScriptException;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class ChatListener implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) throws ScriptException, NoSuchMethodException {
        getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), () -> {
            String message = e.getMessage();
            List<Bot> bots = Bots.getInstance().getBots();

            for(Bot bot : bots) {
                try {
                    bot.readMessage(message);
                } catch (ScriptException ex) {
                    getLogger().warning("Failed to process chat message for bot: " + bot.getName());
                    throw new RuntimeException(ex);
                } catch (NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
