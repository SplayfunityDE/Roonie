package de.splayfer.roonie.modules.library;

import de.splayfer.roonie.utils.DefaultMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.HashMap;
import java.util.List;

public class TemplateListener extends ListenerAdapter {

    HashMap<String, String> initCategories() {
        return new HashMap<>(){{
            put("gaming", "<:controller:885085671579062284> Gaming");
            put("musik", "<a:music:886624918983278622> Musik");
            put("community", "<a:partner:885212849440448512> Community");
            put("content", "\uD83D\uDCFD Content Creator");
            put("galaxy", "\uD83C\uDF00 Galaxy");
            put("projekt", "<:folder:883415478700232735> Projekt");
        }};
    }

    public void onStringSelectInteraction (StringSelectInteractionEvent event) {
        if (event.getSelectMenu().getId().equals("servertemplates")) {
            event.replyEmbeds(DefaultMessage.success("Vorlagen erfolgreich gesendet", "Die angeforderten Vorlagen findest du unter dieser Nachricht!")).setEphemeral(true).queue();
            for (String link : LibraryManager.getTemplatesByCategory(event.getValues().get(0)))
                event.getHook().sendMessage(link).setEphemeral(true).queue();
        }
    }
}