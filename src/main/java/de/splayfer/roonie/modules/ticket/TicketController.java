package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.Roonie;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/ticket")
public class TicketController {

    private final Roonie roonie;
    private final TicketManager ticketManager;

    @DeleteMapping("/{id}")
    public void closeTicket(@PathVariable String id, @RequestParam String reason) {
        ticketManager.getFromChannel(id).close(reason);
    }

    @PatchMapping("/{id}")
    public void updateTicket(@PathVariable String id, @RequestBody Map<String, String> body) {
        Ticket ticket = ticketManager.getFromChannel(id);
        if (body.containsKey("supporter")) {
            if (body.get("supporter") != "") {
                ticket.setSupporter(roonie.getMainGuild().getMemberById(body.get("supporter")));
            } else {
                ticket.setSupporter(null);
            }
        }
        ticket.updateMongoDB();
    }
}
