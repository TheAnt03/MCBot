package uraniumape.mcbot.script.parameters;

import org.bukkit.entity.Player;

public class Message {

    private Player player;
    private String content;

    public Message(Player player, String content) {
         this.player = player;
         this.content = content;
    }

    public Player getPlayer() {
        return player;
    }

    public String getContent() {
        return content;
    }

}
