package de.splayfer.roonie.general;

import de.splayfer.roonie.MongoDBDatabase;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.Command;
import org.bson.Document;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AutoComplete extends ListenerAdapter {

    static MongoDBDatabase mongoDB = new MongoDBDatabase("splayfunity");

    @Override
    public void onCommandAutoCompleteInteraction(CommandAutoCompleteInteractionEvent event) {
        if (existsAutoComplete(event.getName())) {
            String[] array = getWords(event.getName());
            List<Command.Choice> options = Stream.of(array)
                    .filter(word -> word.startsWith(event.getFocusedOption().getValue())) // only display words that start with the user's current input
                    .map(word -> new Command.Choice(word, word)) // map the words to choices
                    .collect(Collectors.toList());
            event.replyChoices(options).queue();
        }
    }

    public boolean existsAutoComplete(String command) {
        return mongoDB.exists("autocomplete", "command", command);
    }

    public String[] getWords(String command) {
        List<String> list = mongoDB.find("autocomplete", "command", command).first().getList("words", String.class);
        return list.toArray(new String[0]);
    }

    public void createAutoComplete(String command, String[] words) {
        mongoDB.insert("autocomplete", new Document().append("command", command).append("words", words));
    }
}
