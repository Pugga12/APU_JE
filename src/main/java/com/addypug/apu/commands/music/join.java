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

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nonnull;
import java.awt.*;

public class join extends ListenerAdapter {
    @Override
    public void onSlashCommand(@Nonnull SlashCommandEvent event) {
        if (!event.isFromGuild()) return;
        if (event.getName().equals("join-vc")) {
            event.deferReply(false).queue();
            final TextChannel channel = event.getTextChannel();
            final Member selfMember = event.getGuild().getSelfMember();
            final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

            EmbedBuilder ebd = new EmbedBuilder();
            if (selfVoiceState.inVoiceChannel()) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Join Voice Channel", "I am already in a VC", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            final Member member = event.getMember();
            final GuildVoiceState memberVoiceState = member.getVoiceState();
            if (!memberVoiceState.inVoiceChannel()) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Join Voice Channel", "You must be in a VC", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final VoiceChannel memberChannel = memberVoiceState.getChannel();
            audioManager.openAudioConnection(memberChannel);
            ebd.setColor(Color.green);
            ebd.addField("Connecting To VC", "Now connecting to " + memberChannel.getName(), true);
            event.getHook().editOriginalEmbeds(ebd.build()).queue();
        }
    }
}
