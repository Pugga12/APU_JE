package com.addypug.apu.data.dbsubst;

import com.addypug.apu.data.values;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDataSource {
    static Logger logger = LoggerFactory.getLogger(SQLiteDataSource.class);
    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource ds = null;

    static {
        try {
            final File dbFile = new File("apudb.db");

            if (!dbFile.exists()) {
                if (dbFile.createNewFile()) {
                    logger.info("Created database file");
                } else {
                    logger.error("Unable to create a database file");
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        config.setJdbcUrl("jdbc:sqlite:apudb.db");
        config.setConnectionTestQuery("SELECT 1");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setPoolName("HikariPool 1");
        ds = new HikariDataSource(config);
        try (final Statement statement = getConnection().createStatement()) {

            // language=SQLite
            statement.execute("CREATE TABLE IF NOT EXISTS guild_settings (guildId VARCHAR(20) PRIMARY KEY);");
            statement.execute("CREATE TABLE IF NOT EXISTS metadata(id INTEGER PRIMARY KEY AUTOINCREMENT, datatype STRING NOT NULL, value);");
            statement.execute("INSERT or IGNORE INTO metadata(id, datatype, value) VALUES (1, 'Database_Schema', '" + values.database_revision + "')");
            statement.execute("INSERT OR IGNORE INTO metadata(id, datatype, value) VALUES (2, 'initialv_version',  '" + values.version + "')");
            statement.execute("INSERT OR IGNORE INTO metadata(id, datatype, value) VALUES (3, 'initialv_release_status', '" + values.release_status + "')");
            logger.info("Database Tables Initialized");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
