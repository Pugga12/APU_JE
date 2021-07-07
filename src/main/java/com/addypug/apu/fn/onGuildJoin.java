package com.addypug.apu.fn;

import com.addypug.apu.data.dbsubst.guildDb;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class onGuildJoin extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(onGuildJoin.class);
    public void onGuildJoin(GuildJoinEvent event) {
        try {
            guildDb.createServerRow(event.getGuild().getId());
        } catch (SQLException e) {
           logger.error(e.getMessage());
        }
    }
}
