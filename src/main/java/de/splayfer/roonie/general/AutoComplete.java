package de.splayfer.roonie.general;

import de.splayfer.roonie.FileSystem;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoComplete extends ListenerAdapter {

    YamlConfiguration yml = YamlConfiguration.loadConfiguration(FileSystem.AutoComplete);

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {

        if (yml.contains(event.getName(), false)) {

            String[] array = new String[yml.getStringList(event.getName()).size()];
            array = yml.getStringList(event.getName()).toArray(array);

            List<Command.Choice> options = Stream.of(array)

                    .filter(word -> word.startsWith(event.getFocusedOption().getValue())) // only display words that start with the user's current input
                    .map(word -> new Command.Choice(word, word)) // map the words to choices
                    .collect(Collectors.toList());

            event.replyChoices(options).queue();

        }

    }

}
