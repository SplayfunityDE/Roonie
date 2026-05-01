package de.splayfer.roonie.modules.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.entities.Guild;

public class MusicController {

    private Guild guild;
    private AudioPlayer player;
    private Queue queue;
    private PlayerManager playerManager;

    public MusicController(Guild guild, Roonie roonie, PlayerManager playerManager) {
        this.guild = guild;
        this.player = roonie.getAudioPlayerManager().createPlayer();
        this.queue = new Queue(this);
        this.playerManager = playerManager;

        this.guild.getAudioManager().setSendingHandler(new AudioPlayerSendHandler(player));
        this.player.addListener(new TrackScheduler(playerManager, roonie));
        this.player.setVolume(10);
    }

    public Guild getGuild() {
        return guild;
    }

    public AudioPlayer getPlayer() {
        return player;
    }

    public Queue getQueue() {
        return queue;
    }
}