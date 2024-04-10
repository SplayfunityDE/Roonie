package de.splayfer.roonie.modules.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.modules.music.MusicController;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class ResumeCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("resume")) {
            MusicController controller = Roonie.playerManager.getController(event.getGuild().getIdLong());
            AudioManager manager = event.getGuild().getAudioManager();
            AudioPlayer player = controller.getPlayer();
            player.setPaused(false);
        }
    }
}
