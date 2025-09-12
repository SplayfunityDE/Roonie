package de.splayfer.roonie.modules.giveaway;

import de.splayfer.roonie.utils.DefaultMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

public class GiveawayEnterListener extends ListenerAdapter {

    public void onButtonInteraction (ButtonInteractionEvent event) {
        if (Objects.requireNonNull(event.getButton().getId()).equals("giveaway.enter")) {
            if (Giveaway.isGiveaway(event.getMessage())) {
                Giveaway giveaway = Giveaway.getFromMessage(event.getMessage());
                if (giveaway.checkRequirement(event.getMember())) {
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
