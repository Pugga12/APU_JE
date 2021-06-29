/*
 * Permission Checker
 * Checks the permission levels of users
 */
package com.addypug.apu.fn;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class check_my_permissions extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(check_my_permissions.class);
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getGuild() == null) return;
        if (event.getName().equals("check-my-permissions")) {
            logger.debug("Valid command received");
            event.deferReply().queue();
            Boolean avail_ban = event.getMember().hasPermission(Permission.BAN_MEMBERS);
            Boolean avail_K = event.getMember().hasPermission(Permission.KICK_MEMBERS);
            EmbedBuilder ebd = new EmbedBuilder();
            ebd.setColor(Color.red);
            ebd.setTitle("Permission Checker - 2.0");
            ebd.addField("Usable Commands", "info: Usable By All Users\nban: " + avail_ban +"\nunban: " + avail_ban + "\nkick: " + avail_K , true);

            event.getHook().editOriginalEmbeds(ebd.build()).queue();
        }
    }
}

