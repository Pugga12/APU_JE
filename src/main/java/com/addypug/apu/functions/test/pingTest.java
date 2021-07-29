package com.addypug.apu.functions.test;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

/**
 * The ping system makes sure the core of the bot is working
 */
public class pingTest extends ListenerAdapter {
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (event.getMessage().getContentRaw().equals("apu!test")) {
            EmbedBuilder ebd = new EmbedBuilder();
            ebd.setTitle("If you see this message, the test was successful");
            event.getMessage().replyEmbeds(ebd.build()).queue();
        }
    }
}
