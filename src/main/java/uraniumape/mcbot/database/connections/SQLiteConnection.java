package uraniumape.mcbot.database.connections;

import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SQLiteConnection extends DatabaseConnection {
    public SQLiteConnection(String botName, boolean autoCommit) {
        super(botName, autoCommit);
    }

    @Override
    protected Connection generateConnection() throws SQLException, ClassNotFoundException {
        String dbPath = MCBot.getInstance().getDataFolder() + "/databases/" + this.botName + ".db";

        Class.forName("org.sqlite.JDBC");
        return DriverManager.getConnection("jdbc:sqlite:" + dbPath);
    }
}
