package de.splayfer.roonie.modules.response;

import de.splayfer.roonie.utils.DefaultMessage;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ResponseRemoveCommand extends ListenerAdapter {

    private final ResponseManager responseManager;

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {
        if (event.getName().equals("response")) {
            if (event.getSubcommandName().equals("remove")) {
                String response = event.getOptionsByName("nachricht").get(0).getAsString();
                if (responseManager.existsResponse(response)) {
                    responseManager.removeResponse(response);
                    event.replyEmbeds(DefaultMessage.success("Response erfolgreich entfernt", "Du hast den Begriff erfolgreich entfernt!", new MessageEmbed.Field("<:text:886623802954498069> Gelöschter Begriff", response, false))).setEphemeral(true).queue();
                } else
                    event.replyEmbeds(DefaultMessage.error("Unbekannter Begriff", "Merkwürdig. Es scheint, als ist der von dir ausgewähte Begriff `" + response + "` noch nicht eingetragen!")).queue();
            }
        }
    }
}