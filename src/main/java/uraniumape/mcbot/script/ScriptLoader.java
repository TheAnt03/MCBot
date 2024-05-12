package uraniumape.mcbot.script;

import org.bukkit.Bukkit;
import uraniumape.mcbot.Main;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScriptLoader {
    public static String loadScript(String scriptName) {
        String path = "/scripts/" + scriptName + ".js";
        try {
            return Files.readString(Paths.get(Main.getInstance().getDataFolder() + path));
        } catch (IOException e) {
            Bukkit.getLogger().warning("Could not find script name: " + scriptName);
            return null;
        }
    }
}
