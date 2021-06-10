package com.addypug.apu.functions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;


public class CmdHelpCmdListener extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(CmdHelpCmdListener.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        if (content.equals("apu!help")) {
            EmbedBuilder ebd = new EmbedBuilder();
            MessageChannel channel = event.getChannel();
            ebd.setTitle("APU Help");
            ebd.setColor(Color.green);
            ebd.addField("General Commands (can be used by everyone)", "apu!secrets <name>: Use a secret provided by the dev\n1 secret available\napu!help: Open the APU help menu\napu!info: Show bot info", false);
            ebd.setFooter("AddyPug's Utilities v0.2.5_121");

            channel.sendMessage(ebd.build()).queue();
            logger.info("Command 'help' executed");

        }
    }


}
