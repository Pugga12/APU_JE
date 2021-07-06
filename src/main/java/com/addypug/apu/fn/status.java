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
        if (event.getGuild() == null) return;
        try {
            guildDb.createServerRow(event.getGuild().getId());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (event.getName().equals("status")) {
            logger.debug("Valid command received");
            event.deferReply().queue();
            EmbedBuilder ebd = new EmbedBuilder();
            ebd.setColor(Color.red);
            ebd.setTitle("Status");
            ebd.addField("Build Info", "APU " + values.release_status + " " + values.version + "\nBuild: " + values.build + "\n" + values.stability + "\nBuilt on JDA " + JDAInfo.VERSION, true);
            RuntimeMXBean runtimeMX = ManagementFactory.getRuntimeMXBean();
            long uptime = runtimeMX.getUptime();
            long uptimeInSeconds = TimeUnit.MILLISECONDS.toSeconds(uptime);
            long numberOfMinutes = TimeUnit.SECONDS.toMinutes(uptimeInSeconds);
            long numberOfHours = TimeUnit.MINUTES.toHours(numberOfMinutes);
            long numberOfDays = TimeUnit.HOURS.toDays(numberOfMinutes);
            logger.debug("Made Uptime Calculations: " + numberOfDays + "d " + numberOfHours + "h " + numberOfMinutes + "m " + uptimeInSeconds + "s\n(" + uptime + " ms)");
            ebd.addField("Uptime", numberOfDays + "d " + numberOfHours + "h " + numberOfMinutes + "m " + uptimeInSeconds + "s\n(" + uptime + " ms)", true);
            event.getHook().editOriginalEmbeds(ebd.build()).queue();
            logger.debug("Submitted response");
        }
    }
}
