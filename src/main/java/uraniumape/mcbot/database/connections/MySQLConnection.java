package uraniumape.mcbot.database.connections;

import uraniumape.mcbot.database.DatabaseConnection;
import uraniumape.mcbot.exceptions.NoConnectionString;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection extends DatabaseConnection {
    private String connectionString;

    public MySQLConnection(String botName, boolean autoCommit, String connectionString)  {
        super(botName, autoCommit);

        this.connectionString = connectionString;
    }

    @Override
    protected Connection generateConnection() throws SQLException, ClassNotFoundException, NoConnectionString {
        if(connectionString == null) {
            throw new NoConnectionString(botName);
        }

        Class.forName("com.mysql.jdbc.Driver");
        return DriverManager.getConnection(this.connectionString);
    }
}
