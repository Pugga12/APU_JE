/*
  Copyright 2021 NotAddyPug

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.addypug.apu.data.dbsubst;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class guildDb {
    /**
     * Enters a server into the guild_settings table
     * @param id The ID of the guild
     */

    public static void createServerRow(String id) throws SQLException {
        Connection connection = SQLiteDataSource.getConnection();
        String query = "INSERT or IGNORE into guild_settings(guildId) values(?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, id);
        statement.executeUpdate();
    }
}