/*
  Copyright © 2021 NotAddyPug

     Licensed under the Apache License, Version 2.0 (the "License");
     you may not use this file except in compliance with the License.
     You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
 */
package com.addypug.apu;

import com.addypug.apu.commands.adminutils.*;
import com.addypug.apu.commands.music.*;
import com.addypug.apu.commands.status;
import com.addypug.apu.commands.test.pingTest;
import com.addypug.apu.data.CfgHandler;
import com.addypug.apu.data.Constants;
import com.addypug.apu.internal.SQLiteDataSource;
import net.dv8tion.jda.api.JDABuilder;
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
        // Makes APU run on single-core linux machines
        final int cores = Runtime.getRuntime().availableProcessors();
        if (cores <= 1) {
            System.out.println("Available Cores \"" + cores + "\", setting Parallelism Flag");
            System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism", "1");
        }
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
        logger.info("Version " + Constants.version + ", Build " + Constants.build);
        logger.info("Instance is now launching! Due to sharding, loading may take a while!");
        Float Spec = Float.parseFloat(runtimeMX.getSpecVersion());
        JDABuilder builder = JDABuilder.createDefault(CfgHandler.valString("token"));
        SQLiteDataSource.getConnection();
        builder.addEventListeners(new pingTest());
        builder.addEventListeners(new status());
        builder.addEventListeners(new banUser());
        builder.addEventListeners(new unbanUser());
        builder.addEventListeners(new kickUser());
        builder.addEventListeners(new shutdown());
        builder.addEventListeners(new join());
        builder.addEventListeners(new play());
        builder.addEventListeners(new stop());
        builder.addEventListeners(new skip());
        builder.addEventListeners(new NowPlaying());
        builder.addEventListeners(new queue());
        builder.addEventListeners(new repeat());
        builder.addEventListeners(new setVolume());
        builder.addEventListeners(new leaveVC());
        builder.addEventListeners(new warnUser());
        builder.addEventListeners(new setSlowmode());
        builder.enableCache(CacheFlag.VOICE_STATE);
        builder.setActivity(Activity.listening("fire tracks"));
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
                new CommandData("join-vc", "Makes the bot join your VC. You must be in a VC")
        );
        cmds.addCommands(
                new CommandData("play", "Play a YouTube Video")
                        .addOptions(new OptionData(STRING, "url", "The YouTube URL to play").setRequired(true))
        );
        cmds.addCommands(
                new CommandData("stop", "Stop the music and clear the queue")
        );
        cmds.addCommands(
                new CommandData("skip", "Skip the current track")
        );
        cmds.addCommands(
                new CommandData("nowplaying", "See what track is playing")
        );
        cmds.addCommands(
                new CommandData("queue", "View the current queue")
        );
        cmds.addCommands(
                new CommandData("repeat", "Repeat the current track")
        );
        cmds.addCommands(
                new CommandData("volume", "Set the volume of the music")
                        .addOptions(new OptionData(INTEGER, "percentage", "The percentage to set the volume to. Must be between 0 and 100%").setRequired(true))
        );
        cmds.addCommands(
                new CommandData("leave-vc", "Makes the bot leave the VC it is in. Also clears the queue")
        );
        cmds.addCommands(
          new CommandData("warn", "Warns a user. Also sends a message to their DMs")
                  .addOptions(new OptionData(USER, "user", "The user to warn").setRequired(true))
                  .addOptions(new OptionData(STRING, "reason", "The reason why you are warning the user").setRequired(true))
        );
        cmds.addCommands(
                new CommandData("sl", "Sets the slowmode for this channel").addOptions(new OptionData(INTEGER, "secs", "The amount to set slowmode to, in second").setRequired(true))
        );
        cmds.addCommands(new CommandData("shutdown", "A quick and dirty way to shutdown the bot. "));
        cmds.queue();
    }
}