package com.addypug.apu.commands.adminutils;

import com.addypug.apu.data.dbsubst.guildDb;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.sql.SQLException;

public class warnUser extends ListenerAdapter {
    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (event.getGuild() == null) return;
        if (event.getName().equals("warn")) {
            event.deferReply(false).queue();
            User user = event.getOption("user").getAsUser();
            String reason = event.getOption("reason").getAsString();
            EmbedBuilder guild_ebd = new EmbedBuilder();
            if (!event.getMember().hasPermission(Permission.MANAGE_SERVER)) {
                guild_ebd.setTitle("Access Denied");
                guild_ebd.setColor(Color.red);
                guild_ebd.setDescription("You must have the `MANAGE_SERVER` permission to perform this command");
                event.getHook().editOriginalEmbeds(guild_ebd.build()).queue();
            }

            guild_ebd.setTitle("Warn Successful");
            guild_ebd.setDescription(user.getAsMention() + " was successfully warned\nThey will be notified via DMs shortly");
            guild_ebd.setColor(Color.green);
            guild_ebd.setFooter("Reason: " + reason);
            try {
                guildDb.addWarn(user.getId(), event.getGuild().getId(), reason);
            } catch (SQLException e) {
                e.printStackTrace();
            }
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
