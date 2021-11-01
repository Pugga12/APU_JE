/**
 * Copyright © 2021 NotAddyPug
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.addypug.apu.commands.adminutils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class unbanUser extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(unbanUser.class);

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.isFromGuild()) return;

        if (event.getName().equals("unban")) {
            User user = event.getOption("user").getAsUser();
            Member member = event.getOption("user").getAsMember();
            logger.debug("Valid command received");
            EmbedBuilder ebd = new EmbedBuilder();
            event.deferReply(false).queue();
            if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
                ebd.setTitle("Access Denied (E0403)", "https://www.addypug.com/support/apu/common-error-codes#h.1wr3d2aa2r7t");
                ebd.addField("Insufficient Permissions", "You do not have the permission to perform this command\nRequires Permission: Ban User (Code P415)", true);
                ebd.setColor(Color.blue);
                ebd.setFooter("Please do not file a issue for this error. It will be closed");
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
            } else {
                Member selfMember = event.getGuild().getSelfMember();
                if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {
                    ebd.setTitle("Access Denied (E0403)", "https://www.addypug.com/support/apu/common-error-codes#h.1wr3d2aa2r7t");
                    ebd.addField("Insufficient Permissions", "I do not have the permission to unban this member\nRequired Permission: Ban User (Code P415-2)", true);
                    ebd.setColor(Color.blue);
                    ebd.setFooter("Please do not file a issue for this error. It will be closed");
                    return;
                }
                ebd.setTitle("Action Completed!");
                ebd.addField("A user was unbanned successfully", user.getAsMention() + " was unbanned", true);
                ebd.setColor(Color.red);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                event.getGuild().unban(user).queue();
            }
        }
    }
}

