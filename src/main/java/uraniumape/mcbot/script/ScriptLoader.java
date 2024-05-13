package uraniumape.mcbot.script;

import org.bukkit.Bukkit;
import uraniumape.mcbot.MCBot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ScriptLoader {
    public static String loadScript(String scriptName) {
        String path = "/scripts/" + scriptName + ".js";

        try {
            return Files.readString(Paths.get(MCBot.getInstance().getDataFolder() + path));
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not find script name: " + scriptName);
            return null;
        }
    }
}
