package de.splayfer.roonie.schedule;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.Timer;
import java.util.TimerTask;

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
                mainChat.getManager().setTopic("Öffentlicher <a:chat:879356542791598160> Chat von SPLΛYFUNITY. Aktuell gesendete Nachrichten: **" + mainChat.getIterableHistory().stream().count() + "**").queue();
            }
        }, 300000, 300000);

    }

}