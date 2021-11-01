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
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;

public class skip extends ListenerAdapter {
    @Override
    public void onSlashCommand(@Nonnull SlashCommandEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getName().equals("skip")) {
            event.deferReply(false).queue();
            final Member selfMember = event.getGuild().getSelfMember();
            final GuildVoiceState selfVoiceState = selfMember.getVoiceState();
            EmbedBuilder ebd = new EmbedBuilder();
            if (!selfVoiceState.inVoiceChannel()) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Skip", "I am not connected to a voice channel\nConnect to a VC and run /join-vc, then try again", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            final Member member = event.getMember();
            final GuildVoiceState memberVoiceState = member.getVoiceState();
            if (!memberVoiceState.inVoiceChannel()) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Skip", "You must be in a VC", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Skip", "You must be in the same VC as me", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final AudioPlayer audioPlayer = musicManager.audioPlayer;

            if (audioPlayer.getPlayingTrack() == null) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Skip", "No track is currently playing. Add a track with /play", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            if (musicManager.scheduler.queue.size() <= 1 | musicManager.scheduler.queue.isEmpty()) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Skip", "This is the last song in the queue or the queue is empty, so it cannot be skipped", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            musicManager.scheduler.nextTrack();
            ebd.setColor(Color.green);
            ebd.addField("Skipped", "Skipped to the next track", true);
            event.getHook().editOriginalEmbeds(ebd.build()).queue();
        }
    }
}
