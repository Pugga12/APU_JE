package com.addypug.apu.commands.adminutils;

import com.addypug.apu.internal.guildDb;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.sql.SQLException;

public class warnUser extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(warnUser.class);

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getName().equals("warn")) {
            event.deferReply(false).queue();
            User user = event.getOption("user").getAsUser();
            String reason = event.getOption("reason").getAsString();
            EmbedBuilder guild_ebd = new EmbedBuilder();
            if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                guild_ebd.setTitle("Access Denied (E0403)", "https://www.addypug.com/support/apu/common-error-codes#h.1wr3d2aa2r7t");
                guild_ebd.setColor(Color.red);
                guild_ebd.setDescription("You must have the `MANAGE_SERVER` permission to perform this command");
                event.getHook().editOriginalEmbeds(guild_ebd.build()).queue();
                return;
            }

            try {
                guildDb.addWarn(user.getId(), event.getGuild().getId(), reason);
            } catch (SQLException e) {
                logger.error(e.getMessage());
                guild_ebd.setTitle("Unexpected Internal Error (E0500)", "https://www.addypug.com/support/apu/common-error-codes#h.o44a4kh0r07h");
                guild_ebd.setDescription("An unexpected internal error has occurred\nContact the bot operator");
                event.getHook().editOriginalEmbeds(guild_ebd.build()).queue();
                return;
            }
            guild_ebd.setTitle("Warn Successful");
            guild_ebd.setDescription(user.getAsMention() + " was successfully warned\nThey will be notified via DMs shortly");
            guild_ebd.setColor(Color.green);
            guild_ebd.setFooter("Reason: " + reason);
            event.getHook().editOriginalEmbeds(guild_ebd.build()).queue();

            EmbedBuilder private_ebd = new EmbedBuilder();
            private_ebd.setTitle("Warned in " + event.getGuild().getName());
            if (event.getGuild().getIconUrl() != null) {
                private_ebd.setImage(event.getGuild().getIconUrl());
            }
            private_ebd.setColor(Color.red);
            private_ebd.setDescription("Reason: " + reason);
            user.openPrivateChannel()
                    .flatMap(privateChannel -> privateChannel.sendMessageEmbeds(private_ebd.build()))
                    .queue();
        }
    }
}
