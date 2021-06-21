package com.addypug.apu.fn;

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
        if (event.getGuild() == null) return;
        if (event.getName().equals("unban")) {
            User user = event.getOption("user").getAsUser();
            Member member = event.getOption("user").getAsMember();
            logger.debug("Valid command received");
            EmbedBuilder ebd = new EmbedBuilder();
            event.deferReply().queue();
            if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
                ebd.addField("Access Is Denied", "You do not have the permission to perform this command\nRequires Permission: Ban User (Code P415)", true);
                ebd.setColor(Color.blue);
                ebd.setFooter("Please do not file a issue for this error. It will be closed");
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
            } else {
                Member selfMember = event.getGuild().getSelfMember();
                if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {
                    ebd.addField("Access Is Denied", "I do not have the permission to unban this member\nI Require Permission: Ban User (Code P415-2)", true);
                    ebd.setColor(Color.blue);
                    ebd.setFooter("Please do not file a issue for this error");
                    return;
                }

                ebd.addField("A user was unbanned successfully", user.getAsMention() +  " was unbanned", true);
                ebd.setColor(Color.red);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                event.getGuild().unban(user);
            }
        }
    }
}

