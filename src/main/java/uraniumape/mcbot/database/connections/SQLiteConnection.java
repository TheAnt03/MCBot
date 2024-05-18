package uraniumape.mcbot.database.connections;

import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.database.DatabaseConnection;
import uraniumape.mcbot.log.BotLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection extends DatabaseConnection {
    private final MCBot mcBot;
    public SQLiteConnection(BotLogger logger, MCBot mcBot, String botName, boolean autoCommit) {
        super(logger, botName, autoCommit);
        this.mcBot = mcBot;
    }

    @Override
    protected Connection generateConnection() throws SQLException, ClassNotFoundException {
        String dbPath = this.mcBot.getDataFolder() + "/databases/" + this.botName + ".db";

        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }

}
