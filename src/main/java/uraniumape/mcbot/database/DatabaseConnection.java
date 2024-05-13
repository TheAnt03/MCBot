package uraniumape.mcbot.database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import uraniumape.mcbot.MCBot;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class DatabaseConnection {
    private String botName;
    private boolean autoCommit;
    private Connection connection;


    public DatabaseConnection(String botName, boolean autoCommit) {
        this.botName = botName;
        this.autoCommit = autoCommit;
    }

    public Connection getConnection() {
        String dbPath = MCBot.getInstance().getDataFolder() + "/databases/" + this.botName + ".db";

        try {
            if(connection == null || connection.isClosed()) {
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

                return connection;
            }

            return connection;
        } catch (SQLException | ClassNotFoundException e ) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not open database connection");
            throw new RuntimeException(e);
        }
    }
}
