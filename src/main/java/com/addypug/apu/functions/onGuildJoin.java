package com.addypug.apu.functions;

import com.addypug.apu.data.dbsubst.guildDb;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class onGuildJoin extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(onGuildJoin.class);
}
