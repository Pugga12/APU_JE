package com.notaddypug.apu.functions.games;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;


public class gameSelect_error extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(gameSelect_error.class);
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        if (content.equals("apu!games")) {
            EmbedBuilder ebd = new EmbedBuilder();
            MessageChannel channel = event.getChannel();
            ebd.setTitle("Command Dropped or Under Construction");
            ebd.setColor(Color.blue);
            ebd.addField("", "This command has been dropped or is under construction (Error P807)", true);
            ebd.setFooter("Please do not report this issue to the developers\nAddyPug's Utilities Experimental (Build 1)\nThis build is experimental and may be unstable. Please tag your issues with branch:@experimental_features if a bug is found");
            channel.sendMessage(ebd.build()).queue();
            logger.warn("P807 Error triggered by user (UserEnd Error)");
        }
}
}
