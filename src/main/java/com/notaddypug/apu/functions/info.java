package com.notaddypug.apu.functions;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;


public class info extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(info.class);
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        if (content.equals("apu!info"))
        {
            EmbedBuilder ebd = new EmbedBuilder();
            MessageChannel channel = event.getChannel();
            ebd.setColor(Color.red);
            ebd.addField("About This Bot", "APU is a modular discord bot made in JDA", true);
            ebd.addField("Support", "Source Code & Updates: https://github.com/Pugga12/APU\nReport Issues: https://github.com/Pugga12/APU/issues\nPrivacy Policy and Other Legal Documents: https://www.addypug.com/projects/apu/legal", true);
            ebd.setFooter("AddyPug's Utilities v1.2.5_101");

            channel.sendMessage(ebd.build()).queue();
            logger.info("Command 'info' executed");

    }
}
}
