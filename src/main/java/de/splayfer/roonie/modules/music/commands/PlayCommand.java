package de.splayfer.roonie.modules.music.commands;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.modules.music.AudioLoadResult;
import de.splayfer.roonie.modules.music.MusicController;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand extends ListenerAdapter {

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("play")) {
            if (event.getMember().getVoiceState().inAudioChannel()) {
                MusicController controller = Roonie.playerManager.getController(event.getGuild().getIdLong());
                AudioManager manager = event.getGuild().getAudioManager();
                manager.openAudioConnection(event.getMember().getVoiceState().getChannel());

                String url = event.getOption("url").getAsString().trim();
                if(!url.startsWith("http")) {
                    url = "ytsearch: " + url;
                }
                Roonie.audioPlayerManager.loadItem(url, new AudioLoadResult(controller, url));
            }
        }
    }

}
