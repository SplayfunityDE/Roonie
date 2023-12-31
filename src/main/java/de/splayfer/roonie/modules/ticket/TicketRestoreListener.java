package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.enums.Channels;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class TicketRestoreListener {

    public static void restoreTickets() {
        for (Ticket ticket : Ticket.getAllTickets()) {
            //check for threads
            if (!Roonie.mainGuild.getThreadChannels().contains(ticket.getChannel())) {
                ticket.setChannel(Roonie.mainGuild.getTextChannelById(Channels.TICKETPANEL.getId()).createThreadChannel(Ticket.typeSymbol.get(ticket.getType()) + "-" + ticket.getCreator().getEffectiveName()).complete());
                ticket.getChannel().sendMessageEmbeds(ticket.getMainEmbeds()).queue();
                ticket.updateMongoDB();
            }
            //check for post
            if (!Roonie.mainGuild.getForumChannelById(Channels.TICKETFORUM.getId()).getThreadChannels().contains(ticket.getPost())) {
                ticket.setPost(Roonie.mainGuild.getForumChannelById(Channels.TICKETFORUM.getId()).createForumPost(Ticket.typeSymbol.get(ticket.getType()) + "-" + ticket.getCreator().getEffectiveName(), MessageCreateData.fromContent("\u200E")).complete().getThreadChannel());
                ticket.getPost().sendMessageEmbeds(ticket.getPostEmbed()).queue();
                ticket.updateMongoDB();
            }
            //check for member left - if ticket is unclaimed
            if (ticket.getSupporter() == null && !Roonie.mainGuild.getMembers().contains(ticket.getCreator()))
                ticket.close("Server verlassen");
        }
    }
}