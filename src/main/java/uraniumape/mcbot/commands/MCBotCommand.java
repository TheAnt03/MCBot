package uraniumape.mcbot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.bot.Bots;

public class MCBotCommand implements CommandExecutor {
    private final String reloadMessage = MCBot.prefix + " Configuration and scripts reloaded!";

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            displayHelpCommand(sender);
            return true;
        }

        switch(args[0]) {
            case "reload":
                Bots.getInstance().closePools();
                MCBot.getInstance().reloadConfig();
                Bots.getInstance().loadBots();

                if(sender instanceof Player) {
                    sender.sendMessage(reloadMessage);
                }

                Bukkit.getConsoleSender().sendMessage(reloadMessage);
                break;
            default:
                displayHelpCommand(sender);
                break;
        }

        return true;
    }

    private void displayHelpCommand(CommandSender sender) {
        String message = "§4[MCBot Commands]\n";
        message += "§4/mcbot §freload - Reloads config & scripts";

        sender.sendMessage(message);
    }
}