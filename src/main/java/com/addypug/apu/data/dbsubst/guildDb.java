package com.addypug.apu.data.dbsubst;

import java.sql.SQLException;

public class guildDb {
    /**
     * Enters a server into the guild_settings table
     * @param id The ID of the server
     * @throws SQLException
     */
    public static void createServerRow(String id) throws SQLException {
        SQLiteDataSource.getConnection().createStatement().execute("INSERT or IGNORE into guild_settings(guildId) VALUES('" + id + "')");
    }
}