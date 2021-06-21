package com.addypug.apu.core;


import com.addypug.apu.fn.banUser;
import com.addypug.apu.fn.infocmd;
import com.addypug.apu.fn.kickUser;
import com.addypug.apu.fn.unbanUser;
import com.addypug.apu.wrap.Meta;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class core {
    public static void main(String[] arguments) throws Exception {
        Logger logger = LoggerFactory.getLogger(core.class);
        System.out.println("Build Info: Version " + Meta.release_status + " " + Meta.version + "_" + Meta.build + " (" + Meta.stability + ", Built on JDA " + JDAInfo.VERSION + ") @ branch " + Meta.branch);
        System.out.println(Meta.stability_msg);
        logger.info("Instance is now launching! Due to sharding, loading may take a while!");
        String token = CfgHandler.valString("token");
        JDABuilder shardBuilder = JDABuilder.createDefault(token);
        shardBuilder.addEventListeners(new infocmd());
        shardBuilder.addEventListeners(new banUser());
        shardBuilder.addEventListeners(new unbanUser());
        shardBuilder.addEventListeners(new kickUser());
        shardBuilder.setActivity(Activity.playing("Type / to see available Commands | APU Experimental " + Meta.version));
        Integer shardinteger = CfgHandler.valInt("shardint");
        logger.info("Beginning Sharding! Shards to initialize: " + shardinteger);
        for (int i = 0; i < shardinteger; i++)
            shardBuilder.useSharding(i, shardinteger)
                    .build();
        logger.info("Sharding complete!");
        CommandListUpdateAction cmds = shardBuilder.build().updateCommands();
        cmds.addCommands(
                new CommandData("info", "Get info about this bot")
        );
        cmds.addCommands(
                new CommandData("ban", "Ban a user")
                    .addOptions(new OptionData(USER, "user", "The User To Ban")
                        .setRequired(true))
        );
        cmds.addCommands(
                new CommandData("unban", "Unban a user")
                    .addOptions(new OptionData(USER, "user", "The user to unban. Must be a ID")
                        .setRequired(true))
        );
        cmds.addCommands(
                new CommandData("kick", "Kick a user")
                        .addOptions(new OptionData(USER, "user", "The user to kick")
                                .setRequired(true))
        );
        logger.info("Attempting to synchronize slash commands");
        cmds.queue();
    }
}