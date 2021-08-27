package com.addypug.apu;


import com.addypug.apu.commands.adminutils.shutdown;
import com.addypug.apu.commands.music.join;
import com.addypug.apu.data.CfgHandler;
import com.addypug.apu.data.dbsubst.SQLiteDataSource;
import com.addypug.apu.commands.adminutils.banUser;
import com.addypug.apu.commands.check_my_permissions;
import com.addypug.apu.commands.status;
import com.addypug.apu.commands.adminutils.kickUser;
import com.addypug.apu.commands.adminutils.unbanUser;
import com.addypug.apu.data.values;
import com.addypug.apu.commands.test.pingTest;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

import static net.dv8tion.jda.api.interactions.commands.OptionType.*;

public class core {
    public static void main(String[] arguments) throws Exception {
        Logger logger = LoggerFactory.getLogger(core.class);
        RuntimeMXBean runtimeMX = ManagementFactory.getRuntimeMXBean();
        OperatingSystemMXBean osMX = ManagementFactory.getOperatingSystemMXBean();
        if (runtimeMX != null && osMX != null) {
            String javaInfo = "Java " + runtimeMX.getSpecVersion() + " (" + runtimeMX.getVmName() + " " + runtimeMX.getVmVersion() + ")";
            String osInfo = null;
            if (osMX.getName().equals("Windows 10")) {
                osInfo = "Host: " + osMX.getName() + " (" + osMX.getArch() + ")";
            } else {
                osInfo = "Host: " + osMX.getName() + " " + osMX.getVersion() + " (" + osMX.getArch() + ")";
            }
            logger.info("System Info: " + javaInfo + ", " + osInfo);
        } else {
            logger.error("Unable to read the system info");
        }
        //logger.info("Build Info: Version " + values.release_status + " " + values.version + " (" + values.stability + ", Built on JDA " + JDAInfo.VERSION + ") @ branch " + values.branch);
        logger.info("Build Info: " + values.release_status + " " + values.version + " (" + values.stability + ", Built on JDA " + JDAInfo.VERSION + ") @ branch " + values.branch);
        logger.info("Instance is now launching! Due to sharding, loading may take a while!");
        Float Spec = Float.parseFloat(runtimeMX.getSpecVersion());
        JDABuilder builder = JDABuilder.createDefault(CfgHandler.valString("token"));
        SQLiteDataSource.getConnection();
        builder.addEventListeners(new pingTest());
        builder.addEventListeners(new status());
        builder.addEventListeners(new banUser());
        builder.addEventListeners(new unbanUser());
        builder.addEventListeners(new kickUser());
        builder.addEventListeners(new check_my_permissions());
        builder.addEventListeners(new shutdown());
        builder.addEventListeners(new join());
        builder.enableCache(CacheFlag.VOICE_STATE);
        builder.setActivity(Activity.playing("Type / to see available Commands | v" + values.version));
        Integer shardinteger = CfgHandler.valInt("shardint");
        logger.info("Beginning Sharding! Shards to initialize: " + shardinteger);
        for (int i = 0; i < shardinteger; i++)
            builder.useSharding(i, shardinteger)
                    .build();
        logger.info("Sharding complete!");
        CommandListUpdateAction cmds = builder.build().updateCommands();
        cmds.addCommands(
                new CommandData("status", "Get status about this bot")
        );
        cmds.addCommands(
                new CommandData("ban", "Ban a user")
                        .addOptions(new OptionData(USER, "user", "The User To Ban")
                                .setRequired(true))
                        .addOptions(new OptionData(INTEGER, "deldays", "How many days of messages should we remove. Must be no more than 7 days").setRequired(true))
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
        cmds.addCommands(
                new CommandData("check-my-permissions", "Check your ability to perform commands")
        );
        cmds.addCommands(
          new CommandData("join-vc", "Makes the bot join your VC. You must be in a VC")
        );
        //GetOnlineData.fetchUpdates(values.update_server_endpoint);
        cmds.queue();
    }
}