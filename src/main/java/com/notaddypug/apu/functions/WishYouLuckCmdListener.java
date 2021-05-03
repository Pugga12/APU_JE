package com.notaddypug.apu.functions;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class WishYouLuckCmdListener extends ListenerAdapter{
    Logger logger = LoggerFactory.getLogger(WishYouLuckCmdListener.class);
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        Message message = event.getMessage();
        String content = message.getContentRaw();
        if (content.equals("apu!secrets wishyouluck"))
        {
            MessageChannel channel = event.getChannel();
            channel.sendMessage("@here https://www.youtube.com/watch?v=jERshGaLVLU WISH YOU LUCK").queue();
            logger.info("Command 'WishYouLuck' executed with parent command 'secrets'");
        }
    }


}
