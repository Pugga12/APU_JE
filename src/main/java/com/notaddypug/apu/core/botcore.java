package com.notaddypug.apu.core;

import com.notaddypug.apu.functions.WishYouLuckCmdListener;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class botcore {

    public static void main(String[] arguments) throws Exception
    {
        Logger logger = LoggerFactory.getLogger(botcore.class);
        logger.info("Instance is now launching! Due to sharding, loading may take a while!");
        JDABuilder shardBuilder = JDABuilder.createDefault(CfgHandler.get("token"));
        shardBuilder.addEventListeners(new WishYouLuckCmdListener());
        shardBuilder.setActivity(Activity.playing("apu!help for help | Experimental Build"));
        CommandUpdateAction cmds = shardBuilder.build().updateCommands();
        cmds.addCommands(
                new CommandData("info", "Get info about this bot"));
        cmds.queue();
        logger.info("Beginning Sharding!");
        for (int i = 0; i < 3; i++)

            shardBuilder.useSharding(i, 3)
                    .build();
        logger.info("Sharding complete!");
    }

}
