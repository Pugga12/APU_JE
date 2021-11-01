package com.addypug.apu.commands.adminutils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.ChannelManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class setSlowmode extends ListenerAdapter {
    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (!event.isFromGuild()) return;

        if (event.getName().equals("sl")) {
            event.deferReply(true).queue();

            Integer secs = Integer.parseInt(event.getOption("secs").getAsString());

            ChannelManager manager = event.getGuildChannel().getManager();
            TextChannel textChannel = event.getTextChannel();
            EmbedBuilder ebd = new EmbedBuilder();

            if (!event.getMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                ebd.setTitle("Access Forbidden (E0403)");
                ebd.setDescription("You don't have the `Manage Channels` permission.\nThis permission is required to change the slowmode");
                ebd.setColor(Color.blue);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }

            if (!event.getGuild().getSelfMember().hasPermission(Permission.MANAGE_CHANNEL)) {
                ebd.setTitle("Access Forbidden (E0403)");
                ebd.setDescription("I don't have the `Manage Channels` permission.\nThis permission is required to change the slowmode\n");
                ebd.setColor(Color.blue);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }

            if (secs > textChannel.MAX_SLOWMODE | secs < 0) {
                ebd.setTitle("Argument Out Of Bounds (E0452)");
                ebd.setDescription("New slowmode time must not be greater than 21600 or less than 0");
                ebd.setColor(Color.blue);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }

            ebd.setColor(Color.green);
            ebd.setTitle("Slowmode Updated for This Channel");
            if (secs != 0) {
                ebd.setDescription("Set to " + secs);
            } else {
                ebd.setDescription("Slowmode Disabled");
            }

            manager.setSlowmode(secs).queue();
            event.getHook().editOriginalEmbeds(ebd.build()).queue();
        }
    }
}
