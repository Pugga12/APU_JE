package com.addypug.apu.fn;

import com.addypug.apu.wrap.Meta;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class banUser extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(banUser.class);

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getGuild() == null) return;
        if (event.getName().equals("ban")) {
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
                    ebd.addField("Access Is Denied", "I do not have the permission to ban this member\nI Require Permission: Ban User (Code P415-2)", true);
                    ebd.setColor(Color.blue);
                    ebd.setFooter("Please do not file a issue for this error");
                    return;
                }
                if (member != null && !selfMember.canInteract(member)) {
                    ebd.addField("Access Is Denied", "I do not have the permission to ban this member\n User is too powerful for me to ban (Code P416)", true);
                    ebd.setColor(Color.blue);
                    ebd.setFooter("Please do not file a issue for this error. It will be closed");
                    event.getHook().editOriginalEmbeds(ebd.build()).queue();
                    return;
                }

                ebd.addField("A user was banned successfully", user.getAsMention() +  " was banned", true);
                ebd.setColor(Color.red);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                event.getGuild().ban(user, 1).queue();
            }
        }
    }
}

