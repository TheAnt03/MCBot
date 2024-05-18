package uraniumape.mcbot.database.connections;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import uraniumape.mcbot.MCBot;
import uraniumape.mcbot.database.DatabaseConnection;
import uraniumape.mcbot.exceptions.NoConnectionString;
import uraniumape.mcbot.log.BotLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection extends DatabaseConnection {
    private String connectionString;
    private HikariDataSource dataSource;

    public MySQLConnection(BotLogger logger, String botName, boolean autoCommit, String connectionString)  {
        super(logger, botName, autoCommit);
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(connectionString);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setAutoCommit(true);

        this.dataSource = new HikariDataSource(config);
        this.connectionString = connectionString;
    }

    @Override
    protected Connection generateConnection() throws SQLException, ClassNotFoundException, NoConnectionString {
        if(connectionString == null) {
            throw new NoConnectionString(botName);
        }

        return dataSource.getConnection();
    }

    private void closeConnection() throws SQLException {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.getConnection().close();
        }
    }

    @Override
    public void close() {
        try {
            closeConnection();
        } catch (SQLException e) {
            this.logger.logError("Could not close database connection");
            throw new RuntimeException(e);
        }
    }

    public void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }
    }
}
