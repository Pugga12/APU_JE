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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class kickUser extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(kickUser.class);

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getName().equals("kick")) {
            User user = event.getOption("user").getAsUser();
            Member member = event.getOption("user").getAsMember();
            logger.debug("Valid command received");
            EmbedBuilder ebd = new EmbedBuilder();
            event.deferReply(false).queue();
            if (!event.getMember().hasPermission(Permission.KICK_MEMBERS)) {
                ebd.setTitle("Access Denied (E0403)", "https://www.addypug.com/support/apu/common-error-codes#h.1wr3d2aa2r7t");
                ebd.addField("Insufficient Permissions", "You do not have the permission to perform this command\nRequires Permission: Kick User (Code P415)", true);
                ebd.setColor(Color.blue);
                ebd.setFooter("Please do not file a issue for this error. It will be closed");
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
            } else {
                Member selfMember = event.getGuild().getSelfMember();
                if (!selfMember.hasPermission(Permission.KICK_MEMBERS)) {
                    ebd.setTitle("Access Denied (E0403)", "https://www.addypug.com/support/apu/common-error-codes#h.1wr3d2aa2r7t");
                    ebd.addField("Insufficient Permissions", "I do not have enough permissions to kick this member\nRequired Permission: Kick User (Code P415-2)", true);
                    ebd.setColor(Color.blue);
                    ebd.setFooter("Please do not file a issue for this error. It will be closed");
                    return;
                }
                if (member != null && !selfMember.canInteract(member)) {
                    ebd.setTitle("Access Denied (E0403)", "https://www.addypug.com/support/apu/common-error-codes#h.1wr3d2aa2r7t");
                    ebd.addField("Cannot Interact With This User", "I cannot interact with this user\n User is too powerful for me to kick (Code P416)", true);
                    ebd.setColor(Color.blue);
                    ebd.setFooter("Please do not file a issue for this error. It will be closed");
                    event.getHook().editOriginalEmbeds(ebd.build()).queue();
                    return;
                }
                ebd.setTitle("Action Completed!");
                ebd.addField("A user was kicked successfully", user.getAsMention() + " was kicked from the server", true);
                ebd.setColor(Color.red);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                event.getGuild().kick(member).queue();
            }
        }
    }
}

