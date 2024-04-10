package de.splayfer.roonie.modules.music.commands;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.modules.music.MusicController;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ShuffelCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("shuffle")) {
            MusicController controller = Roonie.playerManager.getController(event.getGuild().getIdLong());
            controller.getQueue().shuffel();
        }
    }
}
