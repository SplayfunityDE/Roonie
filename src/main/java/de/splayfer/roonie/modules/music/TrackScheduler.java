package de.splayfer.roonie.modules.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventListener;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.AudioManager;

public class TrackScheduler extends AudioEventAdapter {

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
        Guild guild = Roonie.shardMan.getGuildById(Roonie.playerManager.getGuildByPlayerHash(player.hashCode()));

        if(endReason.mayStartNext) {
            MusicController controller = Roonie.playerManager.getController(guild.getIdLong());
            Queue queue = controller.getQueue();

            if(queue.next())
                return;
        }

        AudioManager manager = guild.getAudioManager();
        player.stopTrack();
        manager.closeAudioConnection();
    }

}
