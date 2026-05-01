package de.splayfer.roonie.modules.music;

import de.splayfer.roonie.Roonie;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeaveListener extends ListenerAdapter {

    private final Roonie roonie;
    private final PlayerManager playerManager;

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if (event.getChannelLeft() != null && event.getChannelLeft().equals(roonie.getMainGuild().getAudioManager().getConnectedChannel())) {
            if (roonie.getMainGuild().getAudioManager().getConnectedChannel().asVoiceChannel().getMembers().size() == 1) {
                playerManager.getController(event.getGuild().getIdLong()).getPlayer().stopTrack();
                roonie.getMainGuild().getAudioManager().closeAudioConnection();
            }
        }
    }
}
