/*
  Copyright © 2021 NotAddyPug
  <p>
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  <p>
  http://www.apache.org/licenses/LICENSE-2.0
  <p>
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.addypug.apu.commands;

import com.addypug.apu.ShardingTools;
import com.addypug.apu.data.values;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDAInfo;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class status extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(status.class);

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("status")) {
            logger.debug("Valid command received");
            event.deferReply(false).queue();
            EmbedBuilder ebd = new EmbedBuilder();
            ebd.setColor(Color.red);
            ebd.setTitle("Status");
            ebd.addField("Build Info", "APU " + values.release_status + " " + values.version + "\n" + "\nBuilt on JDA " + JDAInfo.VERSION + "\nShard " + ShardingTools.getNonZeroIndexedShardId(event.getJDA()) + "/" + event.getJDA().getShardInfo().getShardTotal(), true);
            RuntimeMXBean runtimeMX = ManagementFactory.getRuntimeMXBean();
            long uptime = runtimeMX.getUptime();
            long uptimeInSeconds = uptime / 1000;
            long numberOfMinutes = uptimeInSeconds / 60;
            long numberOfHours = numberOfMinutes / 60;
            long numberOfDays = numberOfHours / 24;
            if (uptimeInSeconds > 60) {
                long estSeconds = numberOfMinutes * 60;
                uptimeInSeconds = uptimeInSeconds - estSeconds;
            }
            logger.debug("Made Uptime Calculations: " + numberOfDays + " days, " + numberOfHours + " hours, " + numberOfMinutes + " minutes, " + uptimeInSeconds + " seconds\n(" + uptime + " ms)");
            ebd.addField("Uptime", numberOfDays + " days, " + numberOfHours + " hours, " + numberOfMinutes + " minutes, " + uptimeInSeconds + " seconds\n(" + uptime + " ms)", true);
            event.getHook().sendMessageEmbeds(ebd.build()).queue();
            logger.debug("Submitted response");
        }
    }
}
