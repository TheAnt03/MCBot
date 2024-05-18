package uraniumape.mcbot.script;

import org.bukkit.Bukkit;
import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.log.BotLogger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ScriptLoader {
    private final MCBot mcBot;
    private final BotLogger logger;

    public ScriptLoader(BotLogger logger, MCBot mcBot) {
        this.logger = logger;
        this.mcBot = mcBot;
    }

    private void createScriptFile(String path) {
        Path newPath = Paths.get(mcBot.getDataFolder() + "/" + path);

        try {
            Files.createFile(newPath);
            this.logger.logInfo("Generated script at " + path);
        } catch (IOException e) {
            this.logger.logError("Could not generate script at " + path);
            throw new RuntimeException(e);
        }

    }
    public String loadScript(String scriptName) {
        String path = "/scripts/" + scriptName + ".js";

        try {
            return Files.readString(Paths.get(this.mcBot.getDataFolder() + path));
        } catch (IOException e) {
            this.logger.logError("Could not find script name: " + scriptName + ". Generating file automatically");
            createScriptFile(path);

            return this.loadScript(scriptName);
        }
    }
}
