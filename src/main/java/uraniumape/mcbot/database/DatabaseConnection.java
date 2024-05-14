package uraniumape.mcbot.database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.exceptions.NoConnectionString;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Pattern;

public abstract class DatabaseConnection {
    protected String botName;
    protected boolean autoCommit;
    private Connection connection;

    protected DatabaseConnection(String botName, boolean autoCommit) {
        this.botName = botName;
        this.autoCommit = autoCommit;
    }

    protected abstract Connection generateConnection() throws SQLException, ClassNotFoundException, NoConnectionString;

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not close database connection");
            throw new RuntimeException(e);
        }
    }

    public Connection getConnection() {
        try {
            if(connection == null || connection.isClosed()) {
                this.connection = generateConnection();
            }

            return connection;
        } catch (SQLException | ClassNotFoundException | NoConnectionString e) {
            Bukkit.getConsoleSender().sendMessage(MCBot.prefix + " Could not open database connection");
            throw new RuntimeException(e);
        }
    }


}
