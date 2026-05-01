package de.splayfer.roonie.modules.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import de.splayfer.roonie.Roonie;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public class TrackScheduler extends AudioEventAdapter {

    private final PlayerManager playerManager;
    private final Roonie roonie;

    @Override
    public void onPlayerPause(AudioPlayer player) {

    }

    @Override
    public void onPlayerResume(AudioPlayer player) {

    }

    @Override
    public void onTrackStart(AudioPlayer player, AudioTrack track) {
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.name().equals("FINISHED")) {
            Guild guild = roonie.getShardMan().getGuildById(playerManager.getGuildByPlayerHash(player.hashCode()));

            if (endReason.mayStartNext) {
                MusicController controller = playerManager.getController(guild.getIdLong());
                Queue queue = controller.getQueue();

                if (queue.next())
                    return;
            }

            AudioManager manager = guild.getAudioManager();
            player.stopTrack();
            manager.closeAudioConnection();
        }
    }
}