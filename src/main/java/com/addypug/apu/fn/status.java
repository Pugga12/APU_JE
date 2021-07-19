package com.addypug.apu.fn;

import com.addypug.apu.data.dbsubst.guildDb;
import com.addypug.apu.data.values;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

public class status extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(status.class);
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("status")) {
            logger.debug("Valid command received");
            event.deferReply().queue();
            EmbedBuilder ebd = new EmbedBuilder();
            ebd.setColor(Color.red);
            ebd.setTitle("Status");
            ebd.addField("Build Info", "APU " + values.release_status + " " + values.version + "\n" + values.stability + "\nBuilt on JDA " + JDAInfo.VERSION + "\n" + event.getJDA().getShardInfo(), true);
            RuntimeMXBean runtimeMX = ManagementFactory.getRuntimeMXBean();
            long uptime = runtimeMX.getUptime();
            long uptimeInSeconds =  uptime / 1000;
            long numberOfMinutes = uptimeInSeconds / 60;
            long numberOfHours = numberOfMinutes / 60;
            long numberOfDays = numberOfHours / 24;
            if (uptimeInSeconds > 60) {
                uptimeInSeconds = uptimeInSeconds - 60;
            }
            logger.debug("Made Uptime Calculations: " + numberOfDays + " days, " + numberOfHours + " hours, " + numberOfMinutes + " minutes, " + uptimeInSeconds + " seconds\n(" + uptime + " ms)");
            ebd.addField("Uptime", numberOfDays + " days, " + numberOfHours + " hours, " + numberOfMinutes + " minutes, " + uptimeInSeconds + " seconds\n(" + uptime + " ms)", true);
            event.getHook().sendMessageEmbeds(ebd.build()).queue();
            logger.debug("Submitted response");
        }
    }
}
