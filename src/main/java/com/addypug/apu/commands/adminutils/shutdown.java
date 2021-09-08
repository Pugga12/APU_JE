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
package com.addypug.apu.commands.adminutils;

import com.addypug.apu.data.CfgHandler;
import com.addypug.apu.data.dbsubst.SQLiteDataSource;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.SQLException;

public class shutdown extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(shutdown.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getMessage().getContentRaw().equals("dev!shutdown")) {
            if (event.getAuthor().getId().equals(CfgHandler.valString("bot_owner"))) {
                EmbedBuilder ebd = new EmbedBuilder();
                ebd.setColor(Color.green);
                ebd.addField("Bot is shutting down", "Shutdown command was sent", true);
                event.getMessage().getChannel().sendMessageEmbeds(ebd.build()).queue();
                logger.info("Closing Database Connection");
                try {
                    SQLiteDataSource.getConnection().close();
                } catch (SQLException e) {
                    logger.error(e.getMessage());
                }
                logger.info("Disconnecting from WebSocket");
                event.getJDA().shutdown();
            }
        }
    }
}
