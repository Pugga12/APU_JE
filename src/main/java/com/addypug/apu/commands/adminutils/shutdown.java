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
import com.addypug.apu.dbsubst.SQLiteDataSource;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.SQLException;


public class shutdown extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(shutdown.class);

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getName().equals("shutdown")) {
            event.deferReply(true).queue();
            EmbedBuilder ebd = new EmbedBuilder();
            if (!event.getUser().getId().equals(CfgHandler.valString("bot_owner"))) {
                ebd.setColor(Color.red);
                ebd.setTitle("Access Denied (E0403", "https://www.addypug.com/support/apu/common-error-codes#h.1wr3d2aa2r7t");
                ebd.setDescription("You do not have permission to use this command.\nMake sure you are specified as owner in the .env file");
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            logger.info("Closing Database Connection");
            ebd.setColor(Color.green);
            ebd.setTitle("Confirmation Required");
            ebd.setDescription("Are you sure you want to shutdown the bot?\nThis will close all connections and shutdown the bot.");
            event.getHook().sendMessageEmbeds(ebd.build()).addActionRow(Button.danger("shutdownYes", Emoji.fromUnicode("U+2705"))).queue();
        }
    }

    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        if (event.getComponentId().equals("shutdownYes")) {
            logger.info("Closing Database Connection");
            try {
                SQLiteDataSource.getConnection().close();
            } catch (SQLException e) {
                logger.error(e.getMessage());
            }
            event.getJDA().shutdown();
        }
    }
}
