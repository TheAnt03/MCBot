package uraniumape.mcbot.script;

import org.bukkit.Bukkit;
import uraniumape.mcbot.MCBot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScriptLoader {
    private static void createScriptFile(String path) {
        Path newPath = Paths.get(MCBot.getInstance().getDataFolder() + "/" + path);

        try {
            Files.createFile(newPath);
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Generated script at " + path);
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not generate script at " + path);
            throw new RuntimeException(e);
        }

    }
    public static String loadScript(String scriptName) {
        String path = "/scripts/" + scriptName + ".js";

        try {
            return Files.readString(Paths.get(MCBot.getInstance().getDataFolder() + path));
        } catch (IOException e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not find script name: " + scriptName + ". Generating file automatically");
            createScriptFile(path);

            return loadScript(scriptName);
        }
    }
}
