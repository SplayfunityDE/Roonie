package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.Roonie;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class TicketRestoreListener {

    private final Roonie roonie;
    private final TicketManager ticketManager;

    public TicketRestoreListener(@Lazy Roonie roonie, TicketManager ticketManager) {
        this.roonie = roonie;
        this.ticketManager = ticketManager;
    }

    public void restoreTickets() {
        HashMap<Ticket, String> list = ticketManager.getAllTicketsWithId();
        for (Ticket ticket : list.keySet()) {
            //check for member left - if ticket is unclaimed
            if (ticket.getSupporter() == null && !roonie.getMainGuild().getMembers().contains(ticket.getCreator())) {
                if (ticket.getChannel() != null) {
                    ticket.close("Server verlassen");
                } else {
                    ticket.prune(list.get(ticket));
                }
            }
        }
    }
}