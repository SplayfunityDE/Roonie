package de.splayfer.roonie.config;

import de.splayfer.roonie.modules.booster.BoosterSetupCommand;
import de.splayfer.roonie.modules.library.LibrarySetupCommand;
import de.splayfer.roonie.modules.library.NitrogamesSetupCommand;
import de.splayfer.roonie.modules.management.commands.CommandSetup;
import de.splayfer.roonie.modules.minigames.MinigamesSetupCommand;
import de.splayfer.roonie.modules.ticket.TicketSetupCommand;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SetupCommand extends ListenerAdapter {

    private final Config config;

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {
        if (event.getName().equals("setup")) {
            switch (event.getOption("kategorie").getAsString()) {
                case "commands" -> CommandSetup.setup(config, event);
                case "nitrogames" -> NitrogamesSetupCommand.setup(event);
                case "library" -> LibrarySetupCommand.setup(event);
                case "ticket" -> TicketSetupCommand.setup(event);
                case "booster" -> BoosterSetupCommand.setup(config, event);
                case "minigames" -> MinigamesSetupCommand.setup(config, event);
            }
            event.reply("Setup für `" + event.getOption("kategorie").getAsString() + "` erfolgreich ausgeführt!").setEphemeral(true).queue();
        }
    }

}
