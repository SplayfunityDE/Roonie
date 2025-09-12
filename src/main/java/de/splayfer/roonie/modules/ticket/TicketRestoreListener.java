package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;

import java.util.HashMap;

public class TicketRestoreListener {

    public static void restoreTickets() {
        HashMap<Ticket, String> list = Ticket.getAllTicketsWithId();
        for (Ticket ticket : list.keySet()) {
            //check for member left - if ticket is unclaimed
            if (ticket.getSupporter() == null && !Roonie.mainGuild.getMembers().contains(ticket.getCreator())) {
                if (ticket.getChannel() != null) {
                    ticket.close("Server verlassen");
                } else {
                    ticket.prune(list.get(ticket));
                }
            }
        }
    }
}