package de.splayfer.roonie.modules.minigames;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.config.Config;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DeleteListener extends ListenerAdapter {

    public void onMessageReceived (MessageReceivedEvent event) {
        if (event.getGuild().equals(Roonie.mainGuild))
            if (Config.isConfigChannel(event.getChannel(), "minigames"))
                if (event.getMessage().getContentRaw().equals("\\uD83C\\uDFB2│game"))
                    event.getMessage().delete().queue();
    }
}
