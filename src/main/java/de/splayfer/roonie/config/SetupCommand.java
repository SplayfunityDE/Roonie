package de.splayfer.roonie.config;

import de.splayfer.roonie.modules.booster.BoosterSetupCommand;
import de.splayfer.roonie.modules.library.LibrarySetupCommand;
import de.splayfer.roonie.modules.library.NitrogamesSetupCommand;
import de.splayfer.roonie.modules.minigames.GameSelector;
import de.splayfer.roonie.modules.minigames.MinigamesManager;
import de.splayfer.roonie.modules.minigames.MinigamesSetupCommand;
import de.splayfer.roonie.modules.ticket.TicketSetupCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SetupCommand extends ListenerAdapter {

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {
        if (event.getName().equals("setup")) {
            switch (event.getOption("kategorie").getAsString()) {
                case "commands" -> de.splayfer.roonie.modules.management.commands.SetupCommand.setup(event);
                case "nitrogames" -> NitrogamesSetupCommand.setup(event);
                case "library" -> LibrarySetupCommand.setup(event);
                case "ticket" -> TicketSetupCommand.setup(event);
                case "booster" -> BoosterSetupCommand.setup(event);
                case "minigames" -> MinigamesSetupCommand.setup(event);
            }
            event.reply("Setup für `" + event.getOption("kategorie").getAsString() + "` erfolgreich ausgeführt!").setEphemeral(true).queue();
        }
    }

}
