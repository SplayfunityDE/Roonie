package de.splayfer.roonie.modules.management.commands;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.config.Config;
import de.splayfer.roonie.config.ConfigManager;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class AutoDeleteListener extends ListenerAdapter {

    private final Roonie roonie;
    private final ConfigManager configManager;
    private final Config config;

    public AutoDeleteListener(@Lazy Roonie roonie, ConfigManager configManager, Config config) {
        this.roonie = roonie;
        this.configManager = configManager;
        this.config = config;
    }

    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.isFromGuild() && config.isConfigChannel(event.getChannel(), "commands")) {
            if (!event.getMessage().isEphemeral()) {
                event.getMessage().delete().queueAfter(25, TimeUnit.SECONDS);
            }
        }
    }
    @Scheduled(initialDelay = 5, fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
    public void checkCommandMessages() {
        TextChannel channel = roonie.getMainGuild().getTextChannelById(configManager.getConfigChannel("commands").getId());
        if (channel.getLatestMessageIdLong() != config.getConfigMessageId("commands")) {
            MessageHistory history = channel.getHistoryAfter(config.getConfigMessageId("commands"), 100).complete();
            List<Message> messages = history.getRetrievedHistory();
            channel.purgeMessages(messages);
        }
    }
}
