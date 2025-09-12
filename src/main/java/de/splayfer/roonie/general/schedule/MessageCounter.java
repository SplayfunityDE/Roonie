package de.splayfer.roonie.general.schedule;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.enums.Channels;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;

public class MessageCounter {

    protected static TextChannel mainChat;

    public static void chatCounterUpdate() {
        CompletableFuture.supplyAsync(() -> mainChat.getHistory().getRetrievedHistory().size()).thenAccept(count -> {
            mainChat.getManager().setTopic("Öffentlicher <a:chat:879356542791598160> Chat von SPLΛYFUNITY. Aktuell gesendete Nachrichten: ** " + count + "**").queue();
        });
    }
}