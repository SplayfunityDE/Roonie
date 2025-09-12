package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.Roonie;

public class TicketManager {

    public static void init() {
        Roonie.builder.addEventListeners(new TicketSetupCommand(), new TicketListener());
    }
}