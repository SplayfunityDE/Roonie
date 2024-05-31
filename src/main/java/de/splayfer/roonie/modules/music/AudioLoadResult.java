package de.splayfer.roonie.modules.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.splayfer.roonie.utils.DefaultMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class AudioLoadResult implements AudioLoadResultHandler {

    private MusicController controller;
    private String uri;
    private SlashCommandInteractionEvent event;

    public AudioLoadResult(MusicController controller, String uri, SlashCommandInteractionEvent event) {
        this.controller = controller;
        this.uri = uri;
        this.event = event;
    }
    @Override
    public void trackLoaded(AudioTrack track) {
        String thumbnailUrl = "https://img.youtube.com/vi/" + track.getIdentifier() + "/mqdefault.jpg";
        if (controller.getQueue().getQueuelist().isEmpty())
            event.replyEmbeds(DefaultMessage.success("Erfolgreich gestartet", "Der von dir angegebene Inhalt wird nun abgespielt", thumbnailUrl, new MessageEmbed.Field("Name", track.getInfo().title, false))).setEphemeral(true).queue();
        controller.getQueue().addTrackToQueue(track);
        //controller.getPlayer().playTrack(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        Queue queue = controller.getQueue();

        if(uri.startsWith("ytsearch: ")) { //single yt videos are always loaded as playlist (idk why)
            AudioTrack track = playlist.getTracks().get(0);
            String thumbnailUrl = "https://img.youtube.com/vi/" + track.getIdentifier() + "/mqdefault.jpg";
            if (controller.getQueue().getQueuelist().isEmpty())
                event.replyEmbeds(DefaultMessage.success("Erfolgreich gestartet", "Der Inhalt wird nun abgespielt", thumbnailUrl, new MessageEmbed.Field("Name", track.getInfo().title, false))).setEphemeral(true).queue();
            else
                event.replyEmbeds(DefaultMessage.success("Erfolgreich hinzugefügt", "Der Inhalt befindet sich nun in der Warteschlange", thumbnailUrl, new MessageEmbed.Field("Name", track.getInfo().title, false))).setEphemeral(true).queue();
            queue.addTrackToQueue(track);
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
