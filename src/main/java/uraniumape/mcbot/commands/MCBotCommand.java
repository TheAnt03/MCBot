package uraniumape.mcbot.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.storage.Bots;

public class MCBotCommand implements CommandExecutor {
    private final String reloadMessage = MCBot.prefix + " Configuration and scripts reloaded!";
    private void displayHelpCommand(CommandSender sender) {
        String message = "§4[MCBot Commands]\n";
        message += "§4/mcbot §freload - Reloads config & scripts";

        sender.sendMessage(message);
    }

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            displayHelpCommand(sender);
            return true;
        }

        String commandArg = args[0];

        switch(commandArg) {
            case "reload":
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
}