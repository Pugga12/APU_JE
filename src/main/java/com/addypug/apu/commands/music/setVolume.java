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
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.awt.*;

public class setVolume extends ListenerAdapter {
    @Override
    public void onSlashCommand(@Nonnull SlashCommandEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getName().equals("volume")) {
            event.deferReply(false).queue();
            Integer volume = Integer.parseInt(event.getOption("percentage").toString());
            final Member selfMember = event.getGuild().getSelfMember();
            final GuildVoiceState selfVoiceState = selfMember.getVoiceState();
            EmbedBuilder ebd = new EmbedBuilder();
            if (!selfVoiceState.inVoiceChannel()) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Change Volume", "I am not connected to a voice channel\nConnect to a VC and run /join-vc, then try again", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            final Member member = event.getMember();
            final GuildVoiceState memberVoiceState = member.getVoiceState();
            if (!memberVoiceState.inVoiceChannel()) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Change Volume", "You must be in a VC", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Change Volume", "You must be in the same VC as me", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());
            final AudioPlayer audioPlayer = musicManager.audioPlayer;

            if (volume > 100) {
                ebd.setColor(Color.blue);
                ebd.addField("Volume Greater than Max", "The max volume is 100%. Please set the volume to 100% or lower", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            } else if (volume < 0) {
                ebd.setColor(Color.blue);
                ebd.addField("Volume Too Low", "You set a volume that is below the minimum of 0%. Please set the volume to 0% or greater", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            ebd.setColor(Color.blue);
            ebd.addField("Volume Set", "Volume was set to " + volume + "%", true);
            event.getHook().editOriginalEmbeds(ebd.build()).queue();
            audioPlayer.setVolume(volume);
        }
    }
}