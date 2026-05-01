package de.splayfer.roonie.modules.minigames;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.config.Config;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeleteListener extends ListenerAdapter {

    private final Roonie roonie;

    public void onMessageReceived (MessageReceivedEvent event) {
        if (event.isFromGuild()) {
            if (event.getGuild().equals(roonie.getMainGuild()))
                if (Config.isConfigChannel(event.getChannel(), "minigames"))
                    if (event.getMessage().getContentRaw().equals("\\uD83C\\uDFB2│game"))
                        event.getMessage().delete().queue();
        }
    }
}