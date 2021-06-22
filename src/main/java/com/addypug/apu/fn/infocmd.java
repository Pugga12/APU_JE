package com.addypug.apu.fn;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class infocmd extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(infocmd.class);
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getGuild() == null) return;
        if (event.getName().equals("info")) {
            logger.debug("Valid command received");
            event.deferReply().queue();
            EmbedBuilder ebd = new EmbedBuilder();
            ebd.setColor(Color.red);
            ebd.addField("About This Bot", "APU is a modular discord bot made in JDA", true);
            ebd.addField("Support", "Source Code & Updates: https://github.com/Pugga12/APU_JE/\nReport Issues: https://github.com/Pugga12/APU_JE/issues\nPrivacy Policy and Other Legal Documents: https://www.addypug.com/projects/apu/legal", true);
            event.getHook().editOriginalEmbeds(ebd.build()).queue();
            logger.debug("Submitted response");
        }
    }
}
