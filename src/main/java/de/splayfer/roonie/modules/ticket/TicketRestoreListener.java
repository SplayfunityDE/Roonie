package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.enums.Channels;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

public class TicketRestoreListener {

    public static void restoreTickets() {
        for (Ticket ticket : Ticket.getAllTickets()) {
            //check for threads & post
            if (!(Roonie.mainGuild.getThreadChannels().contains(ticket.getChannel()) && Roonie.mainGuild.getForumChannelById(Channels.TICKETFORUM.getId()).getThreadChannels().contains(ticket.getPost()))) {
                ticket.delete();
                System.out.println("Ticket " + " gelöscht");
            } else if (!Roonie.mainGuild.getThreadChannels().contains(ticket.getChannel())) {
                //check only for threads
                ticket.delete();
                System.out.println("Ticket " + " wegen fehlendem Channel gelöscht");
            } else if (!Roonie.mainGuild.getForumChannelById(Channels.TICKETFORUM.getId()).getThreadChannels().contains(ticket.getPost())) {
                //check only for post
                ticket.setPost(Roonie.mainGuild.getForumChannelById(Channels.TICKETFORUM.getId()).createForumPost(Ticket.typeSymbol.get(ticket.getType()) + "-" + ticket.getCreator().getEffectiveName(), MessageCreateData.fromContent("\u200E")).complete().getThreadChannel());
                ticket.getPost().sendMessageEmbeds(ticket.getPostEmbed()).queue();
                ticket.updateMongoDB();
                System.out.println("Ticket " + " wegen fehlendem Post wiederhergestellt");
            }
            //check for member left - if ticket is unclaimed
            if (ticket.getSupporter() == null && !Roonie.mainGuild.getMembers().contains(ticket.getCreator())) {
                ticket.close("Server verlassen");
                System.out.println("Server verlassen");
            }
        }
    }
}