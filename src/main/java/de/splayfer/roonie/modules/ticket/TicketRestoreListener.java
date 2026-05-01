package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.Properties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class TicketRestoreListener {

    private final Properties properties;
    private final Roonie roonie;
    private final TicketManager ticketManager;

    @PostConstruct
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