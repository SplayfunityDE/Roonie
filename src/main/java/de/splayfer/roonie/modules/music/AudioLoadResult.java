package de.splayfer.roonie.modules.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import de.splayfer.roonie.utils.DefaultMessage;
import de.splayfer.roonie.utils.enums.Embeds;
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
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.addField("Name", track.getInfo().title, true);
        embedBuilder.setThumbnail(thumbnailUrl);
        embedBuilder.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
        if (controller.getQueue().getQueuelist().isEmpty() || controller.getPlayer().getPlayingTrack() == null) {
            embedBuilder.setColor(Embeds.BANNER_MUSIC_RESUME.getColor());
            embedBuilder.setTitle("**:white_check_mark: INHALT GESTARTET**");
            embedBuilder.setDescription("> Dir Inhalt wird nun abgespielt");
            event.replyEmbeds(Embeds.BANNER_MUSIC_RESUME, embedBuilder.build()).setEphemeral(true).queue();
        } else {
            embedBuilder.setColor(Embeds.BANNER_MUSIC_ADD.getColor());
            embedBuilder.setTitle("**:ballot_box_with_check: INHALT HINZUGEFÜGT**");
            embedBuilder.setDescription("> Dir Inhalt wurde erfolgreich zur Warteschlange hinzugefügt");
            event.replyEmbeds(Embeds.BANNER_MUSIC_ADD, embedBuilder.build()).setEphemeral(true).queue();
        }
        controller.getQueue().addTrackToQueue(track);
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        Queue queue = controller.getQueue();

        if(uri.startsWith("ytsearch: ")) { //single yt videos are always loaded as playlist (idk why)
            AudioTrack track = playlist.getTracks().getFirst();
            String thumbnailUrl = "https://img.youtube.com/vi/" + track.getIdentifier() + "/mqdefault.jpg";
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.addField("Name", track.getInfo().title, true);
            embedBuilder.setThumbnail(thumbnailUrl);
            embedBuilder.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
            System.out.println(controller.getPlayer().getPlayingTrack() == null);
            if (controller.getQueue().getQueuelist().isEmpty() && controller.getPlayer().getPlayingTrack() == null) {
                embedBuilder.setColor(Embeds.BANNER_MUSIC_RESUME.getColor());
                embedBuilder.setTitle("**:white_check_mark: INHALT GESTARTET**");
                embedBuilder.setDescription("> Dir Inhalt wird nun abgespielt");
                event.replyEmbeds(Embeds.BANNER_MUSIC_RESUME, embedBuilder.build()).setEphemeral(true).queue();
            } else {
                embedBuilder.setColor(Embeds.BANNER_MUSIC_ADD.getColor());
                embedBuilder.setTitle("**:ballot_box_with_check: INHALT HINZUGEFÜGT**");
                embedBuilder.setDescription("> Dir Inhalt wurde erfolgreich zur Warteschlange hinzugefügt");
                event.replyEmbeds(Embeds.BANNER_MUSIC_ADD, embedBuilder.build()).setEphemeral(true).queue();
            }
            queue.addTrackToQueue(track);
            return;
        }

        int added = 0;
        for(AudioTrack track : playlist.getTracks()) {
            queue.addTrackToQueue(track);
            added++;
        }
        String thumbnailUrl = "https://img.youtube.com/vi/" + playlist.getTracks().getFirst().getIdentifier() + "/mqdefault.jpg";
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Embeds.BANNER_MUSIC_ADD.getColor());
        embedBuilder.setTitle("**:ballot_box_with_check: PlAYLIST HINZUGEFÜGT**");
        embedBuilder.setDescription("> Die von dir gewählte Playlist wurde erfolgreich geladen und hinzugefügt");
        embedBuilder.addField("Playlistname", playlist.getName(), true);
        embedBuilder.addField("Anzahl der Inhalte", "**`" + added + "`**", true);
        event.replyEmbeds(Embeds.BANNER_MUSIC_ADD, embedBuilder.build()).setEphemeral(true).queue();
    }

    @Override
    public void noMatches() {
        event.replyEmbeds(DefaultMessage.error("Kein Ergebnis", "Hmm... Es scheint als konnten wir unter deinem Link/Suchbegriff nichts passendes finden", new MessageEmbed.Field(":mag_right: Dein Begriff", "**`" + uri + "`**", true)));
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        event.replyEmbeds(DefaultMessage.error("Laden fehlgeschlagen", "Anscheinend ist ein Fehler beim Laden deines Inhalts aufgetreten :( . Überprüfe deinen Suchbegriff oder ändere ihn leicht ab", new MessageEmbed.Field("<:text:877158818088386580> Fehlercode", "**`" + exception.getMessage() + "`**", true)));

    }
}
