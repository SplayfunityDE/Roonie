package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.enums.Channels;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.HashMap;

public class TicketRestoreListener {

    public static void restoreTickets() {
        HashMap<Ticket, Long> list = Ticket.getAllTicketsWithId();
        for (Ticket ticket : list.keySet()) {
            //check for member left - if ticket is unclaimed
            if (ticket.getSupporter() == null && !Roonie.mainGuild.getMembers().contains(ticket.getCreator())) {
                if (ticket.getChannel() != null) {
                    ticket.close("Server verlassen");
                } else {
                    ticket.prune(list.get(ticket));
                }
            } else if (!Roonie.mainGuild.getThreadChannels().contains(ticket.getChannel())) {
                //check only for threads
                ticket.prune(list.get(ticket));

            } else if (!Roonie.mainGuild.getForumChannelById(Channels.TICKETFORUM.getId()).getThreadChannels().contains(ticket.getPost())) {
                //check only for post
                ticket.setPost(Roonie.mainGuild.getForumChannelById(Channels.TICKETFORUM.getId()).createForumPost(Ticket.typeSymbol.get(ticket.getType()) + "-" + ticket.getCreator().getEffectiveName(), MessageCreateData.fromContent("\u200E")).complete().getThreadChannel());
                ticket.getPost().sendMessageEmbeds(ticket.getPostEmbed()).queue();
                ticket.updateMongoDB();
            }
        }
    }
}