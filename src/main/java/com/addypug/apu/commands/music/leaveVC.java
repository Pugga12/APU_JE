package com.addypug.apu.commands.music;

import com.addypug.apu.lavaplayer.GuildMusicManager;
import com.addypug.apu.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nonnull;
import java.awt.*;

public class leaveVC extends ListenerAdapter {
    @Override
    public void onSlashCommand(@Nonnull SlashCommandEvent event) {
        if (event.getName().equals("leave-vc")) {
            event.deferReply(false).queue();
            final Member selfMember = event.getGuild().getSelfMember();
            final GuildVoiceState selfVoiceState = selfMember.getVoiceState();
            EmbedBuilder ebd = new EmbedBuilder();
            if (!selfVoiceState.inVoiceChannel()) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Disconnect", "I am not connected to a voice channel", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            final Member member = event.getMember();
            final GuildVoiceState memberVoiceState = member.getVoiceState();
            if (!memberVoiceState.inVoiceChannel()) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Disconnect", "You must be in a VC", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                ebd.setColor(Color.blue);
                ebd.addField("Unable To Disconnect", "You must be in the same VC as me", true);
                event.getHook().editOriginalEmbeds(ebd.build()).queue();
                return;
            }
            final AudioManager audioManager = event.getGuild().getAudioManager();
            final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(event.getGuild());

            musicManager.scheduler.player.stopTrack();
            musicManager.scheduler.queue.clear();

            audioManager.closeAudioConnection();

            ebd.setColor(Color.green);
            ebd.addField("I left the voice channel", "Cleaned the queue and left the voice channel", true);
            event.getHook().editOriginalEmbeds(ebd.build()).queue();

        }
    }
}
