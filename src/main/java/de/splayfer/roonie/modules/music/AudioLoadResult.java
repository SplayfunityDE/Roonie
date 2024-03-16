package de.splayfer.roonie.modules.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class AudioLoadResult implements AudioLoadResultHandler {

    private MusicController controller;
    private String uri;

    public AudioLoadResult(MusicController controller, String uri) {
        this.controller = controller;
        this.uri = uri;
    }
    @Override
    public void trackLoaded(AudioTrack track) {
        controller.getPlayer().playTrack(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        Queue queue = controller.getQueue();

        if(uri.startsWith("ytsearch: ")) {
            queue.addTrackToQueue(playlist.getTracks().get(0));
            return;
        }

        int added = 0;

        for(AudioTrack track : playlist.getTracks()) {
            queue.addTrackToQueue(track);
            added++;
        }

        EmbedBuilder builder = new EmbedBuilder().setColor(Color.decode("#8c14fc"))
                .setDescription(added + " tracks added to queue");
    }

    @Override
    public void noMatches() {

    }

    @Override
    public void loadFailed(FriendlyException exception) {

    }
}
