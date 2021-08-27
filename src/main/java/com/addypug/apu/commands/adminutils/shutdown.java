package com.addypug.apu.commands.adminutils;

import com.addypug.apu.data.CfgHandler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class shutdown extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getMessage().getContentRaw().equals("dev!shutdown")) {
            if (event.getAuthor().getId().equals(CfgHandler.valString("bot_owner"))) {
                EmbedBuilder ebd = new EmbedBuilder();
                ebd.setColor(Color.green);
                ebd.addField("Bot is shutting down", "Shutdown command was sent", true);
                event.getMessage().getChannel().sendMessageEmbeds(ebd.build()).queue();
                event.getJDA().shutdown();
            }
        }
    }
}
