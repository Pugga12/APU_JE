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
package com.addypug.apu.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

/*
 * Permission Checker
 * Checks the permission levels of users
 */
public class check_my_permissions extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(check_my_permissions.class);

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getGuild() == null) return;
        if (event.getName().equals("check-my-permissions")) {
            logger.debug("Valid command received");
            event.deferReply(false).queue();
            Boolean avail_ban = event.getMember().hasPermission(Permission.BAN_MEMBERS);
            Boolean avail_K = event.getMember().hasPermission(Permission.KICK_MEMBERS);
            EmbedBuilder ebd = new EmbedBuilder();
            ebd.setColor(Color.red);
            ebd.setTitle("Permission Checker - 2.0");
            ebd.addField("Usable Commands", "info: Usable By All Users\nban: " + avail_ban + "\nunban: " + avail_ban + "\nkick: " + avail_K, true);

            event.getHook().editOriginalEmbeds(ebd.build()).queue();
        }
    }
}

