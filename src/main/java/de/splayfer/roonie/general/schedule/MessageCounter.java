package de.splayfer.roonie.general.schedule;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@Component
public class MessageCounter {

    protected static TextChannel mainChat;

    @Scheduled(initialDelay = 1, fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void chatCounterUpdate() {
        CompletableFuture.supplyAsync(() -> mainChat.getHistory().getRetrievedHistory().size()).thenAccept(count -> {
            mainChat.getManager().setTopic("Öffentlicher <a:chat:879356542791598160> Chat von SPLΛYFUNITY. Aktuell gesendete Nachrichten: ** " + count + "**").queue();
        });
    }
}