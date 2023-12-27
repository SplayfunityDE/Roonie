package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.utils.DefaultMessage;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TicketListener extends ListenerAdapter {

    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getSelectMenu().getId().equals("support.create")) {
            int type = 0;
            switch (event.getValues().get(0)) {
                case "question":
                    type = 1;
                    break;
                case "bug":
                    type = 2;
                    break;
                case "report":
                    type = 3;
                    break;
            }
            event.replyEmbeds(DefaultMessage.success("Ticket erfolgreich erstellt", "Dein Ticket wurde erfolgreich in dem Kanal " + Ticket.create(event.getMember(), type).getChannel().getAsMention() + " erstellt!")).queue();
        }
    }

    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getButton().getId().startsWith("button.")) {
            switch (event.getButton().getId().split("\\.")[1]) {
                case "claim":
                    Ticket ticket = Ticket.getFromChannel(event.getMessage().getEmbeds().get(0).getFooter().getText().split(" ")[1]);
                    ticket.getChannel().addThreadMember(event.getMember()).queue();
                    ticket.setSupporter(event.getMember());
                    ticket.updateMongoDB();
                    break
            }
        }
    }
}