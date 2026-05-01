package de.splayfer.roonie.modules.giveaway;

import de.splayfer.roonie.utils.DefaultMessage;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class GiveawayEnterListener extends ListenerAdapter {

    private final GiveawayManager giveawayManager;

    public void onButtonInteraction (ButtonInteractionEvent event) {
        if (Objects.requireNonNull(event.getButton().getCustomId()).equals("giveaway.enter")) {
            if (giveawayManager.isGiveaway(event.getMessage())) {
                Giveaway giveaway = giveawayManager.getFromMessage(event.getMessage());
                if (giveawayManager.checkRequirement(giveaway, event.getMember())) {
                    if (!giveaway.getEntrys().contains(event.getMember().getIdLong())) {
                        giveaway.addEntry(event.getMember());
                        event.replyEmbeds(DefaultMessage.success("Erfolgreich teilgenommen!", "Du hast erfolgreich an dem Gewinnspiel teilgenommen!", new MessageEmbed.Field("<a:wettbewerb:898566916958978078> Preis", giveaway.getPrize(), true), new MessageEmbed.Field(":clock10: Endet in", "<t:" + giveaway.getDuration() + ":R>", true))).setEphemeral(true).queue();
                    } else {
                        giveaway.removeEntry(event.getMember());
                        event.replyEmbeds(DefaultMessage.success2("Erfolgreich entfernt!", "Du hast deine Teilnahme an dem Giveaway erfolgreich entfernt!", new MessageEmbed.Field("<a:wettbewerb:898566916958978078> Preis", giveaway.getPrize(), true), new MessageEmbed.Field(":clock10: Endet in", "<t:" + giveaway.getDuration() + ":R>", true))).setEphemeral(true).queue();
                    }
                } else
                    event.replyEmbeds(DefaultMessage.error("Bedingungen nicht erfüllt!", "Du erfüllst nicht die Bedingungen um an diesem Gewinnspiel teilnehmen zu können!")).setEphemeral(true).queue();
            }
        }
    }
}
