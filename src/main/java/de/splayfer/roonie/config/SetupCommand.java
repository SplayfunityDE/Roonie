package de.splayfer.roonie.config;

import de.splayfer.roonie.MongoDBDatabase;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SetupCommand extends ListenerAdapter {

    static MongoDBDatabase mongoDB = new MongoDBDatabase("splayfunity");

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {
        if (event.getName().equals("setup")) {
            switch (event.getOption("kategorie").getAsString()) {
                case "commands":
                    de.splayfer.roonie.commands.SetupCommand.setup(event);
                    break;
            }
            event.reply("Setup für `" + event.getOption("kategorie").getAsString() + "` erfolgreich ausgeführt!").setEphemeral(true).queue();
        }
    }

}
