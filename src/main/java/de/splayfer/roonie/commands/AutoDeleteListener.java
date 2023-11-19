package de.splayfer.roonie.commands;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.config.Config;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class AutoDeleteListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromGuild() && Config.isConfigChannel(event.getChannel(), "commands")) {
            if (!event.getMessage().isEphemeral()) {
                event.getMessage().delete().queueAfter(25, TimeUnit.SECONDS);
            }
        }
    }

    public static void checkCommandMessages() {
        TextChannel channel = Roonie.mainGuild.getTextChannelById(Config.getConfigChannelId("commands"));
        if (!channel.getLatestMessageId().equals(Config.getConfigMessageId("commands"))) {
            MessageHistory history = channel.getHistoryAfter(Config.getConfigMessageId("commands"), 100).complete();
            List<Message> messages = history.getRetrievedHistory();
            channel.purgeMessages(messages);
        }
    }
}
