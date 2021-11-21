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

import com.addypug.apu.internal.lavaplayer.GuildMusicManager;
import com.addypug.apu.internal.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;

public class repeat extends ListenerAdapter {
    @Override
    public void onSlashCommand(@Nonnull SlashCommandEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getName().equals("repeat")) {
            event.deferReply(false).queue();
            final Member selfMember = event.getGuild().getSelfMember();
            final GuildVoiceState selfVoiceState = selfMember.getVoiceState();
            EmbedBuilder ebd = new EmbedBuilder();
            if (!selfVoiceState.inVoiceChannel()) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Repeat", "I am not connected to a voice channel\nConnect to a VC and run /join-vc, then try again", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            final Member member = event.getMember();
            final GuildVoiceState memberVoiceState = member.getVoiceState();
            if (!memberVoiceState.inVoiceChannel()) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Repeat", "You must be in a VC", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Repeat", "You must be in the same VC as me", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final Boolean newRepeating = !musicManager.scheduler.isRepeating;
            final AudioTrack currentTrack = musicManager.audioPlayer.getPlayingTrack();
            musicManager.scheduler.isRepeating = newRepeating;
            ebd.setColor(Color.blue);
            if (newRepeating) {
                ebd.addField("Playback Settings Changed", "Now repeating the current track", true);
            } else {
                ebd.addField("Playback Settings Changed", "No longer repeating the current track", true);
            }
            event.getHook().editOriginalEmbeds(ebd.build()).queue();
        }
    }
}
