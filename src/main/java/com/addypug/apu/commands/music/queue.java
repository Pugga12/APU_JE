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
package com.addypug.apu.commands.music;

import com.addypug.apu.lavaplayer.GuildMusicManager;
import com.addypug.apu.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class queue extends ListenerAdapter {
    Logger logger = LoggerFactory.getLogger(queue.class);

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getName().equals("queue")) {
            event.deferReply(false).queue();
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;
            EmbedBuilder ebd = new EmbedBuilder();
            if (queue.isEmpty()) {
                ebd.setColor(Color.green);
                ebd.addField("Queue", "The queue is currently empty.\nAdd tracks to the queue with /play", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            final MessageChannel channel = event.getChannel();
            final int queueSize = Math.min(queue.size(), 5);
            final List<AudioTrack> trackList = new ArrayList<>(queue);
            StringBuilder StringBuilder = new StringBuilder();
            final AudioPlayer audioPlayer = musicManager.audioPlayer;
            logger.info("Making queue...");
            for (int i = 0; i < queueSize; i++) {
                final AudioTrack track = trackList.get(i);
                final AudioTrackInfo info = track.getInfo();

                StringBuilder
                        .append("#")
                        .append(i + 1)
                        .append(" `")
                        .append(info.title)
                        .append(" by ")
                        .append(info.author)
                        .append(" [")
                        .append(formatTime(track.getDuration()))
                        .append("]`\n");
            }
            String body = StringBuilder.toString();
            logger.debug("Finished filling queue: (size: " + queue.size() + ")\n" + body);
            ebd.setColor(Color.green);
            ebd.setFooter(trackList.size() + " track(s) in queue | " + queueSize + " shown | Repeating: " + musicManager.scheduler.isRepeating + " | Volume: " + audioPlayer.getVolume() + "%");
            ebd.addField("Queue", body, true);
            event.getHook().editOriginalEmbeds(ebd.build()).queue();
        }
    }

    public String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
