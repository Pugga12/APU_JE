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

import com.addypug.apu.data.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;

public class status extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(status.class);

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (event.getName().equals("status")) {
            logger.debug("Valid command received");
            event.deferReply(false).queue();
            EmbedBuilder ebd = new EmbedBuilder();
            ebd.setTitle("Status");
            RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
            final Runtime runtime = Runtime.getRuntime();
            OperatingSystemMXBean OSMX = ManagementFactory.getOperatingSystemMXBean();
            ebd.setDescription("Version " + Constants.release_status + " " + Constants.version + " (Build " + Constants.build + ")\n[GitHub](https://github.com/Pugga12/APU_JE)\nTop.gg (N/A)\nVote (N/A)\n[Latest Release](https://github.com/Pugga12/APU_JE/releases/latest)" );
            ebd.addField("Memory", convert(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " / " + convert(Runtime.getRuntime().totalMemory()), false);
            ebd.setColor(Color.green);
            event.getHook().editOriginalEmbeds(ebd.build()).queue();
        }
    }
    public String convert(long bytes) {
        String result = "";
        if (bytes >= 1000000) {
            result = bytes / 1000000 + " mb";
        } else if (bytes >= 1000000000) {
            result = bytes / 1000000000 + " gb";
        } else {
            result = bytes + " b";
        }
        return result;
    }
}
