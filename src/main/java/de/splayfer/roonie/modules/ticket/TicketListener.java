package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.DefaultMessage;
import de.splayfer.roonie.utils.enums.Embeds;
import de.splayfer.roonie.utils.enums.Roles;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class TicketListener extends ListenerAdapter {

    HashMap<Long, Message> messageCache = new HashMap<>();

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
            event.replyEmbeds(DefaultMessage.success("Ticket erfolgreich erstellt", "Dein Ticket wurde erfolgreich in dem Kanal " + Ticket.create(event.getMember(), type).getChannel().getAsMention() + " erstellt!")).setEphemeral(true).queue();
            event.editSelectMenu(event.getSelectMenu()).queue();
        }
    }

    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getButton().getId().startsWith("ticket.")) {
            Ticket ticket;
            switch (event.getButton().getId().split("\\.")[1]) {
                case "claim":
                    ticket = Ticket.getFromPost(event.getChannelIdLong());
                    if (ticket.getSupporter() == null) {
                        ticket.getChannel().addThreadMember(event.getMember()).queue();
                        ticket.setSupporter(event.getMember());
                        ticket.updateMongoDB();
                        event.replyEmbeds(DefaultMessage.success("Ticket erfolgreich geclaimt", "Du bearbeitest nun das Ticket von " + ticket.getCreator().getAsMention())).setActionRow(Button.success("link", "Schau es dir jetzt an!").withUrl(ticket.getChannel().getJumpUrl()).withEmoji(Emoji.fromFormatted("\uD83D\uDD0E"))).setEphemeral(true).queue();
                        event.editButton(Button.secondary("ticket.claim", "Bereits geclaimt").withEmoji(Emoji.fromFormatted("\uD83D\uDD10"))).queue();
                    } else if (ticket.getSupporter().equals(event.getMember())) {
                        InteractionHook hook = event.replyEmbeds(DefaultMessage.error("Ticket bereits geclaimt", "Das angegebene Ticket wird bereits von dir geclaimt!")).setActionRow(Button.danger("ticket.unclaim", "Claim aufheben").withEmoji(Emoji.fromFormatted("\uD83D\uDD13"))).setEphemeral(true).complete();
                        messageCache.put(hook.retrieveOriginal().complete().getIdLong(), event.getMessage());
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (messageCache.get(hook.retrieveOriginal().complete().getIdLong()) != null)
                                    messageCache.remove(hook.retrieveOriginal().complete().getIdLong());
                            }
                        }, 1000 * 60 * 14);
                    } else if (event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                        InteractionHook hook = event.replyEmbeds(DefaultMessage.error("Ticket bereits geclaimt", "Das angegebene Ticket wurde bereits von " + ticket.getSupporter().getAsMention() + " geclaimt!", new MessageEmbed.Field("\uD83D\uDEAB Rechte entziehen", "Als Administrator kannst du einem Teammitglied die Claim-Rechte temporär entziehen, klicke hierfür auf den Button unter dieser Nachricht!", false))).setActionRow(Button.secondary("ticket.unclaim", "Claim-Rechte entziehen").withEmoji(Emoji.fromFormatted("\uD83D\uDEAB"))).setEphemeral(true).complete();
                        messageCache.put(hook.retrieveOriginal().complete().getIdLong(), event.getMessage());
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                if (messageCache.get(hook.retrieveOriginal().complete().getIdLong()) != null)
                                    messageCache.remove(hook.retrieveOriginal().complete().getIdLong());
                            }
                        }, 1000 * 60 * 14);
                    }
                        break;
                case "unclaim":
                    ticket = Ticket.getFromPost(event.getChannelIdLong());
                    event.getMessage().delete().queue();
                    ticket.setSupporter(null);
                    ticket.updateMongoDB();
                    event.replyEmbeds(DefaultMessage.success2("Erfolgreich freigegeben", "Das angegebene Ticket ist nun wieder zur Bearbeitung freigegeben!")).setEphemeral(true).queue();
                    messageCache.get(event.getMessageIdLong()).editMessageComponents(ActionRow.of(Button.primary("ticket.claim", "Ticket claimen").withEmoji(Emoji.fromFormatted("\uD83D\uDD12")))).queue();
                    messageCache.remove(event.getMessageIdLong());
                    break;
                case "close":
                    ticket = Ticket.getFromChannel(event.getChannelIdLong());
                    if (ticket.getSupporter() != null)
                        if (!(event.getMember().equals(ticket.getSupporter()) || event.getMember().hasPermission(Permission.ADMINISTRATOR))) {
                            event.replyEmbeds(DefaultMessage.error("Ticket wird gerade bearbeitet", "Du kannst dieses Ticket nicht schließen, weil es gerade von " + ticket.getSupporter().getAsMention() + " bearbeitet wird!")).setEphemeral(true).queue();
                            return;
                        }
                        else
                        if (!(Roles.TEAM_EXTENDS.hasAnyRoles(Roonie.mainGuild, event.getMember()) || event.getMember().hasPermission(Permission.ADMINISTRATOR) || event.getMember().equals(ticket.getCreator()))) {
                            event.replyEmbeds(DefaultMessage.error("Keine Rechte", "Du besitzt nicht die Berechtigungen, dieses Ticket zu schließen!")).setEphemeral(true).queue();
                            return;
                        }
                    event.replyModal(Modal.create("ticket.close", "Ticket schließen")
                            .addComponents(
                                    ActionRow.of(TextInput.create("reason", "Grund", TextInputStyle.SHORT)
                                            .setPlaceholder("(Optional) Gib einen Grund an")
                                            .setRequiredRange(1, 200)
                                            .setRequired(false)
                                            .build()))
                            .build()).queue();
                    break;
            }
        }
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getChannel().getId().equals("908795138623537232"))
            if (event.getAuthor().getIdLong() == Roonie.shardMan.getSelfUser().getIdLong() && event.getMessage().getEmbeds().isEmpty())
                event.getMessage().delete().queue();
    }

    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().startsWith("ticket.")) {
            switch (event.getModalId().split("\\.")[1]) {
                case "close":
                    String reason;
                    if (!event.getValue("reason").getAsString().isEmpty())
                        reason = event.getValue("reason").getAsString();
                    else
                        reason = "`KEINER`";
                    event.deferEdit().queue();
                    Ticket.getFromChannel(event.getChannelIdLong()).close(reason);
                    break;
            }
        }
    }
}