package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.Roonie;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/ticket")
public class TicketController {

    @DeleteMapping("/{id}")
    public void closeTicket(@PathVariable String id, @RequestParam String reason) {
        Ticket.getFromChannel(id).close(reason);
    }

    @PutMapping("/{id}")
    public void updateTicket(@PathVariable String id, @RequestBody Map<String, String> body) {
        System.out.println(body.toString());
        Ticket ticket = Ticket.getFromChannel(id);
        if (body.containsKey("supporter")) {
            if (body.get("supporter") != "") {
                ticket.setSupporter(Roonie.mainGuild.getMemberById(body.get("supporter")));
            } else {
                ticket.setSupporter(null);
            }
        }
        ticket.updateMongoDB();
    }
}
