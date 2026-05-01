package de.splayfer.roonie.modules.music.commands;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.modules.music.AudioLoadResult;
import de.splayfer.roonie.modules.music.MusicController;
import de.splayfer.roonie.modules.music.PlayerManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PlayCommand extends ListenerAdapter {

    private final Roonie roonie;
    private final PlayerManager playerManager;

    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("play")) {
            if (event.getMember().getVoiceState().inAudioChannel() && !roonie.getMainGuild().getAudioManager().isConnected()) {
                if (!mongoDB.find("config", "identifier", "radio").first().getLong("channel").equals(event.getChannelIdLong())) {
                    MusicController controller = playerManager.getController(event.getGuild().getIdLong());
                    AudioManager manager = event.getGuild().getAudioManager();
                    manager.openAudioConnection(event.getMember().getVoiceState().getChannel());

                    String url = event.getOption("inhalt").getAsString().trim();
                    if (!url.startsWith("http")) {
                        url = "ytsearch: " + url;
                    }
                    roonie.getAudioPlayerManager().loadItem(url, new AudioLoadResult(controller, url, event));
                }
            }
        }
    }

}
