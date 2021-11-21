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
package com.addypug.apu.internal.lavaplayer;

import com.addypug.apu.commands.music.queue;
import com.addypug.apu.data.Constants;
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager {
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager() {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    public void loadAndPlaysc(@Nonnull SlashCommandEvent slashcommand, String url) {
        final GuildMusicManager musicManager = this.getMusicManager(slashcommand.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, url, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
                queue queuecommand = new queue();
                EmbedBuilder ebd = new EmbedBuilder();
                ebd.addField("Added To Queue", track.getInfo().title + "\n" + track.getInfo().author, true);
                ebd.setColor(Color.green);
                ebd.setFooter(track.getInfo().uri + " | Duration: " + queuecommand.formatTime(track.getDuration()) + " | " + musicManager.scheduler.queue.size() + " track(s) in queue total");
                slashcommand.getHook().editOriginalEmbeds(ebd.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                final List<AudioTrack> tracks = playlist.getTracks();

                EmbedBuilder ebd = new EmbedBuilder();
                ebd.addField("Playlist Added To Queue", "Added " + tracks.size() + " tracks to the queue from playlist " + playlist.getName(), true);
                for (final AudioTrack track : tracks) {
                    musicManager.scheduler.queue(track);
                }
                ebd.setColor(Color.green);
                ebd.setFooter(url + " | " + (musicManager.scheduler.queue.size() + 1) + " track(s) in queue total");
                slashcommand.getHook().editOriginalEmbeds(ebd.build()).queue();
            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException exception) {
                if (exception.getMessage().contains(Constants.E1429_Trip_String)) {
                    EmbedBuilder ebd = new EmbedBuilder();
                    ebd.setColor(Color.blue);
                    ebd.setTitle("IP Banned by Youtube (E1429)", "https://www.addypug.com/support/apu/common-error-codes#h.63rvrcz38v");
                    ebd.setDescription(Constants.E1429_Trip_String);
                    slashcommand.getHook().editOriginalEmbeds(ebd.build()).queue();
                } else{
                    EmbedBuilder ebd = new EmbedBuilder();
                    ebd.setColor(Color.blue);
                    ebd.addField("Failed To Load Track", "Failed to load track at " + url, true);
                    ebd.setFooter("Try again or submit a issue at https://github.com/Pugga12/APU_JE/issues");
                    slashcommand.getHook().editOriginalEmbeds(ebd.build()).queue();
                }
            }
        });
    }


    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
