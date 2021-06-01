package com.notaddypug.apu.core;

import com.notaddypug.apu.functions.WishYouLuckCmdListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandUpdateAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.notaddypug.apu.wrap.Meta;
import java.awt.*;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class botcore extends ListenerAdapter {
    public static void main(String[] arguments) throws Exception
    {
        System.out.println("Build Info: Version " + Meta.version + " (" + Meta.stability + ", Built on JDA "  + JDAInfo.VERSION + ") on branch " + Meta.branch);
        System.out.println(Meta.stability_msg);
        Logger logger = LoggerFactory.getLogger(botcore.class);
        logger.info("Instance is now launching! Due to sharding, loading may take a while!");
        JDABuilder shardBuilder = JDABuilder.createDefault(CfgHandler.get("token"));
        shardBuilder.addEventListeners(new botcore());
        shardBuilder.addEventListeners(new WishYouLuckCmdListener());
        shardBuilder.setActivity(Activity.playing("Type / to see available Commands | APU Experimental " + Meta.version));
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
        cmds.addCommands(
                new CommandData("runegg", "Makes the bot run easter egg code")
                        .addOptions(new OptionData(STRING, "egg", "The code to activate the egg")
                                .setRequired(true))
        );
        cmds.queue();
    }
    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getGuild() == null) return;
        if (event.getName().equals("info")) {
                EmbedBuilder ebd = new EmbedBuilder();
                ebd.setColor(Color.red);
                ebd.addField("About This Bot", "APU is a modular discord bot made in JDA", true);
                ebd.addField("Support", "Source Code & Updates (for experimental branch): https://github.com/Pugga12/APU_JE/tree/experimental_features\nReport Issues: https://github.com/Pugga12/APU_JE/issues\nPrivacy Policy and Other Legal Documents: https://www.addypug.com/projects/apu/legal", true);
                ebd.setFooter("AddyPug's Utilities Experimental " + Meta.version + "\n" + Meta.stability_msg);
                event.replyEmbeds(ebd.build()).setEphemeral(false).queue();
        }
    }
}
