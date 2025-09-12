package de.splayfer.roonie.modules.music;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LeaveListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if (event.getChannelLeft() != null && event.getChannelLeft().equals(Roonie.mainGuild.getAudioManager().getConnectedChannel())) {
            if (Roonie.mainGuild.getAudioManager().getConnectedChannel().asVoiceChannel().getMembers().size() == 1) {
                Roonie.playerManager.getController(event.getGuild().getIdLong()).getPlayer().stopTrack();
                Roonie.mainGuild.getAudioManager().closeAudioConnection();
            }
        }
    }
}
