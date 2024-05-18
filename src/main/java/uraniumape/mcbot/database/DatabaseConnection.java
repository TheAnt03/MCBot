package uraniumape.mcbot.database;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.exceptions.NoConnectionString;
import uraniumape.mcbot.log.BotLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Pattern;

public abstract class DatabaseConnection {
    protected String botName;
    protected boolean autoCommit;
    protected BotLogger logger;
    private Connection connection;

    protected DatabaseConnection(BotLogger logger, String botName, boolean autoCommit) {
        this.botName = botName;
        this.autoCommit = autoCommit;
    }

    protected abstract Connection generateConnection() throws SQLException, ClassNotFoundException, NoConnectionString;

    public void close() {
        try {
            this.connection.close();
        } catch (SQLException e) {
            this.logger.logError("Could not close database connection");
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
            this.logger.logError("Could not open database connection");
            throw new RuntimeException(e);
        }
    }


}
