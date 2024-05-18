package uraniumape.mcbot.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.bot.Bots;
import uraniumape.mcbot.log.BotLogger;

public class MCBotCommand implements CommandExecutor {
    private final String reloadMessage = "Configuration and scripts reloaded!";
    private final MCBot mcBot;
    private final BotLogger logger;
    private final String prefix = "[" + ChatColor.DARK_RED + "MCBotJS" + ChatColor.WHITE + "] ";

    public MCBotCommand(BotLogger logger, MCBot mcBot) {
        this.mcBot = mcBot;
        this.logger = logger;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            displayHelpCommand(sender);
            return true;
        }

        switch(args[0]) {
            case "reload":
                this.mcBot.reloadConfig();
                this.mcBot.reloadBots();

                if(sender instanceof Player) {
                    sender.sendMessage(prefix + reloadMessage);
                }

                this.logger.logInfo(reloadMessage);
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