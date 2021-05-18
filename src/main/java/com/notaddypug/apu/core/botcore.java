package com.notaddypug.apu.core;

import com.notaddypug.apu.functions.CmdHelpCmdListener;
import com.notaddypug.apu.functions.WishYouLuckCmdListener;
import com.notaddypug.apu.functions.games.gameSelect_error;
import com.notaddypug.apu.functions.info;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class botcore {

    public static void main(String[] arguments) throws Exception
    {
        Logger logger = LoggerFactory.getLogger(botcore.class);
        logger.info("Instance is now launching! Due to sharding, loading may take a while!");
        JDABuilder shardBuilder = JDABuilder.createDefault(CfgHandler.get("token"));
        shardBuilder.addEventListeners(new CmdHelpCmdListener());
        shardBuilder.addEventListeners(new WishYouLuckCmdListener());
        shardBuilder.addEventListeners(new gameSelect_error());
        shardBuilder.addEventListeners(new info());
        shardBuilder.setActivity(Activity.playing("apu!help for help"));
        int shardinteger = Integer.parseInt(CfgHandler.get("shardint"));
        logger.info("Beginning Sharding! Shards to initialize: " + shardinteger);
        for (int i = 0; i < shardinteger; i++)

            shardBuilder.useSharding(i, shardinteger)
                    .build();
        logger.info("Sharding complete!");
    }

}
