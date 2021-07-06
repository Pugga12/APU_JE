package com.addypug.apu.fn.adminutils;

import com.addypug.apu.data.dbsubst.guildDb;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.SQLException;

public class banUser extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(banUser.class);

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getGuild() == null) return;
        try {
            guildDb.createServerRow(event.getGuild().getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (event.getName().equals("ban")) {
            User user = event.getOption("user").getAsUser();
            Member member = event.getOption("user").getAsMember();
            Integer deldaysint = Integer.parseInt(event.getOption("deldays").getAsString());
            logger.debug("Valid message received");
            EmbedBuilder ebd = new EmbedBuilder();
            event.deferReply().queue();
            if (!event.getMember().hasPermission(Permission.BAN_MEMBERS)) {
                ebd.setTitle("Error: Access Is Denied");
                ebd.addField("Insufficient Permissions", "You do not have the permission to perform this command\nRequires Permission: Ban User (Code P415)", true);
                ebd.setColor(Color.blue);
                ebd.setFooter("Please do not file a issue for this error. It will be closed");
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
            } else {
                Member selfMember = event.getGuild().getSelfMember();
                if (!selfMember.hasPermission(Permission.BAN_MEMBERS)) {
                    ebd.setTitle("Error: Access Is Denied");
                    ebd.addField("Insufficient Permissions", "I do not have enough permissions to ban this member\nRequired Permissions: Ban User (Code P415-2)", true);
                    ebd.setColor(Color.blue);
                    ebd.setFooter("Please do not file a issue for this error. It will be closed");
                    return;
                }
                if (member != null && !selfMember.canInteract(member)) {
                    ebd.addField("Cannot Interact With This User", "I do not have the permission to interact with this user\n User is too powerful for me to ban (Code P416)", true);
                    ebd.setColor(Color.blue);
                    ebd.setFooter("Please do not file a issue for this error. It will be closed");
                    event.getHook().editOriginalEmbeds(ebd.build()).queue();
                    return;
                }
                Integer ddoverflow = deldaysint - 7;
                if (deldaysint > 7) {
                    ebd.setTitle("Error: Argument Over Maximum Value");
                    ebd.setColor(Color.blue);
                    ebd.addField("Argument 'deldays' is > 7", "deldays is " + ddoverflow + " days greater than the maximum value of 7 days (Code P7)", true);
                    ebd.setFooter("Please do not file a issue for this error. It will be closed");
                    event.getHook().editOriginalEmbeds(ebd.build()).queue();
                    return;
                }
                ebd.setTitle("Action Completed!");
                ebd.addField("A user was banned successfully", user.getAsMention() + " was banned\nAll messages sent within " + deldaysint + " days were deleted", true);
                ebd.setColor(Color.red);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                event.getGuild().ban(user, deldaysint).queue();
            }
        }
    }
}

