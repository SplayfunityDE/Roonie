package de.splayfer.roonie.modules.library;

import de.splayfer.roonie.utils.DefaultMessage;
import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.emoji.EmojiUnion;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class TemplateListener extends ListenerAdapter {

    HashMap<String, String> initCategories() {
        return new HashMap<>() {{
            put("gaming", "<:controller:885085671579062284> Gaming");
            put("musik", "<a:music:886624918983278622> Musik");
            put("community", "<a:partner:885212849440448512> Community");
            put("content", "\uD83D\uDCFD Content Creator");
            put("galaxy", "\uD83C\uDF00 Galaxy");
            put("projekt", "<:folder:883415478700232735> Projekt");
        }};
    }

    @Override
    public void onStringSelectInteraction(@NotNull StringSelectInteractionEvent event) {
        if (event.getSelectMenu().getId().equals("servertemplates")) {
            Member member = event.getMember();
            InteractionHook hook = event.deferReply(true).complete();
            for (SelectOption selectedOption : event.getSelectedOptions()) {
                if (!sendTemplate(selectedOption, member)) {
                    event.getMessage().editMessageEmbeds(event.getMessage().getEmbeds()).setActionRow(event.getSelectMenu()).queue();
                    return;
                }
            }
            String categorysString = getCategoryString(event.getSelectedOptions());
            hook.editOriginalEmbeds(DefaultMessage.success("Vorlagen erfolgreich gesendet", "Dir wurden erfolgreich die Servervorlagen mit den folgenden Details zugesendet", new MessageEmbed.Field("<:text:886623802954498069> Kategorie(n)", categorysString, false))).queue();
            event.getMessage().editMessageEmbeds(event.getMessage().getEmbeds()).setActionRow(event.getSelectMenu()).queue();
        }
    }

    private Boolean sendTemplate(SelectOption option, Member target) {

        List<String> templates = LibraryManager.getTemplatesByCategory(option.getValue());
        StringBuilder categoryString = new StringBuilder();
        if (option.getEmoji() != null) {
            if (isCustom(option.getEmoji())) {
                categoryString.append(option.getEmoji().asCustom().getFormatted()).append(" ").append(option.getLabel());
            } else {
                categoryString.append(option.getEmoji().asUnicode().getFormatted()).append(" ").append(option.getLabel());
            }
        } else categoryString.append(option.getLabel());
        try {
            PrivateChannel privateChannel = target.getUser().openPrivateChannel().complete();
            privateChannel.sendMessageEmbeds(DefaultMessage.success("Kategorie(n) erfolgreich gesendet", "Dir wurden folgende Kategorie(n) zugesendet!", new MessageEmbed.Field(categoryString.toString(), "", false))).complete();
            StringBuilder builder = new StringBuilder();
            for (String template : templates) {
                builder.append(template).append("\n");
            }
            privateChannel.sendMessage(builder.toString()).complete();
            return true;
        } catch (ErrorResponseException ex) {
            return false;
        }

    }

    private String getCategoryString(List<SelectOption> selectOptions) {
        StringBuilder sb = new StringBuilder();
        for (SelectOption selectOption : selectOptions) {
            if (selectOption.getEmoji() != null) {
                if (isCustom(selectOption.getEmoji())) {
                    sb.append(selectOption.getEmoji().asCustom().getFormatted()).append(" ").append(selectOption.getLabel());
                } else {
                    sb.append(selectOption.getEmoji().asUnicode().getFormatted()).append(" ").append(selectOption.getLabel());
                }
            } else sb.append(selectOption.getLabel());
            sb.append("\n");
        }
        return sb.toString();
    }

    private Boolean isCustom(EmojiUnion emojiUnion) {
        try {
            emojiUnion.asCustom();
            return true;
        } catch (IllegalStateException ex) {
            return false;
        }
    }
}