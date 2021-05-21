package com.notaddypug.apu.core;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.requests.restaction.CommandUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;


public class botcore extends ListenerAdapter {
    public static final String branch = "experimental_features";
    public static final String channelType = "experimental";
    public static final String version = "e1.8 (Based on Stable 0.2.5)";
    public static final String stability_msg = "This build is experimental and may be unstable. Please tag your issues with branch:@experimental_features if a bug is found"; // this should be blank if not on experimental branch

    public static void main(String[] arguments) throws Exception
    {
        System.out.println("Build Info: Version " + version + " on branch " + branch + " (Build Channel: " + channelType + ")");
        System.out.println(stability_msg);
        Logger logger = LoggerFactory.getLogger(botcore.class);
        logger.info("Instance is now launching! Due to sharding, loading may take a while!");
        JDABuilder shardBuilder = JDABuilder.createDefault(CfgHandler.get("token"));
        shardBuilder.addEventListeners(new botcore());
        shardBuilder.setActivity(Activity.playing("Type / to see available Commands | APU Experimental e1.7"));
        int shardinteger = Integer.parseInt(CfgHandler.get("shardint"));
        logger.info("Beginning Sharding! Shards to initialize: " + shardinteger);
        for (int i = 0; i < shardinteger; i++)

            shardBuilder.useSharding(i, shardinteger)
                    .build();
        logger.info("Sharding complete!");
        CommandUpdateAction cmds = shardBuilder.build().updateCommands();
        cmds.addCommands(
                new CommandData("info", "Get info about this bot")
        );
        cmds.queue();

    }
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getGuild() == null) return;
        if (event.getName().equals("info")) {
                event.deferReply(true).queue();
                EmbedBuilder ebd = new EmbedBuilder();
                ebd.setColor(Color.red);
                ebd.addField("About This Bot", "APU is a modular discord bot made in JDA", true);
                ebd.addField("Support", "Source Code & Updates (for experimental branch): https://github.com/Pugga12/APU_JE/tree/experimental_features\nReport Issues: https://github.com/Pugga12/APU_JE/issues\nPrivacy Policy and Other Legal Documents: https://www.addypug.com/projects/apu/legal", true);
                ebd.setFooter("AddyPug's Utilities Experimental (Build e1.7)\nThis build is experimental and may be unstable. Please tag your issues with branch:@experimental_features if a bug is found");
                event.replyEmbeds(ebd.build()).setEphemeral(true).queue();
        }
    }
}
