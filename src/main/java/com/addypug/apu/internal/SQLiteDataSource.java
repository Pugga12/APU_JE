/*
  Copyright © 2021 NotAddyPug
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.addypug.apu.internal;

import com.addypug.apu.data.Constants;
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
            final File dbFile = new File("database.sqlite3");

            if (!dbFile.exists()) {
                if (dbFile.createNewFile()) {
                    logger.info("Created database file");
                } else {
                    logger.error("Unable to create a database file");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        config.setJdbcUrl("jdbc:sqlite:database.sqlite3");
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
            statement.execute("INSERT or IGNORE INTO metadata(id, datatype, value) VALUES (1, 'Database_Schema', '" + Constants.database_revision + "')");
            statement.execute("INSERT OR IGNORE INTO metadata(id, datatype, value) VALUES (2, 'initialv_version',  '" + Constants.version + "')");
            statement.execute("INSERT OR IGNORE INTO metadata(id, datatype, value) VALUES (3, 'initialv_release_status', '" + Constants.release_status + "')");
            logger.info("Database Tables Initialized");
        } catch (SQLException e) {
            logger.error(e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
