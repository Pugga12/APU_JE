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

import com.addypug.apu.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nonnull;
import java.awt.*;
import java.net.URI;
import java.net.URISyntaxException;

public class play extends ListenerAdapter {
    @Override
    public void onSlashCommand(@Nonnull SlashCommandEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getName().equals("play")) {
            event.deferReply(false).queue();
            String url = event.getOption("url").getAsString();
            final Member selfMember = event.getGuild().getSelfMember();
            final GuildVoiceState selfVoiceState = selfMember.getVoiceState();
            EmbedBuilder ebd = new EmbedBuilder();
            final Member member = event.getMember();
            final GuildVoiceState memberVoiceState = member.getVoiceState();
            Boolean playconnect = false;
            if (!selfVoiceState.inVoiceChannel() && memberVoiceState.inVoiceChannel()) {
                final AudioManager audioManager = event.getGuild().getAudioManager();
                final VoiceChannel memberChannel = memberVoiceState.getChannel();
                audioManager.openAudioConnection(memberChannel);
                playconnect = true;
            }
            if (!memberVoiceState.inVoiceChannel()) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Play", "You must be in a VC", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel()) && !playconnect) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Play", "You must be in the same VC as me", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }

            PlayerManager.getInstance().loadAndPlaysc(event, url);
        }
    }

    private boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }
}
