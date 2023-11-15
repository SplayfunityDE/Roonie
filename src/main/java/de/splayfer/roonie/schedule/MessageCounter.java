package de.splayfer.roonie.schedule;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class MessageCounter {

    protected static TextChannel mainChat;

    public static void chatCounterUpdate() {

        try {
            mainChat = Roonie.shardMan.awaitReady().getTextChannelById("883278317753626655");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {

                CompletableFuture<List<Message>> list2 = mainChat.getIterableHistory().takeAsync(1000000).thenApply(list -> list.stream().collect(Collectors.toList()));

                int messages = list2.getNow(list2.join()).size();

                mainChat.getManager().setTopic("Öffentlicher <a:chat:879356542791598160> Chat von SPLΛYFUNITY. Aktuell gesendete Nachrichten: **" + messages + "**").queue();

            }
        }, 300000, 300000);

    }

}