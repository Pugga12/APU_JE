package com.addypug.apu.core;


import com.addypug.apu.wrap.Meta;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class main extends ListenerAdapter {
    public static void main(String[] arguments) throws Exception {
        Logger logger = LoggerFactory.getLogger(main.class);
        System.out.println("Build Info: Version " + Meta.version + "_" + Meta.build + " (" + Meta.stability + ", Built on JDA " + JDAInfo.VERSION + ") @ branch " + Meta.branch);
        System.out.println(Meta.stability_msg);
        logger.info("Instance is now launching! Due to sharding, loading may take a while!");
        String token = CfgHandler.valString("token");
        JDABuilder shardBuilder = JDABuilder.createDefault(token);
        shardBuilder.addEventListeners(new main());
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
                new CommandData("runegg", "Makes the bot run easter egg code")
                        .addOptions(new OptionData(STRING, "egg", "The code to activate the egg")
                                .setRequired(true))
        );
        logger.info("Attempting to synchronize slash commands");
        cmds.queue();
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        // Only accept commands from guilds
        if (event.getGuild() == null)
            return;
        switch (event.getName()) {
            case "info":
                EmbedBuilder ebd = new EmbedBuilder();
                ebd.setColor(Color.red);
                ebd.addField("About This Bot", "APU is a modular discord bot made in JDA", true);
                ebd.addField("Support", "Source Code & Updates (for experimental branch): https://github.com/Pugga12/APU_JE/tree/experimental_features\nReport Issues: https://github.com/Pugga12/APU_JE/issues\nPrivacy Policy and Other Legal Documents: https://www.addypug.com/projects/apu/legal", true);
                ebd.setFooter("AddyPug's Utilities Experimental " + Meta.version + "\n" + Meta.stability_msg);
        }
    }
}