package de.splayfer.roonie.general.schedule;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.enums.Channels;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.Timer;
import java.util.TimerTask;

public class MessageCounter {

    protected static TextChannel mainChat;

    public static void chatCounterUpdate() {
        mainChat = Channels.MAINCHAT.getTextChannel(Roonie.mainGuild);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mainChat.getManager().setTopic("Öffentlicher <a:chat:879356542791598160> Chat von SPLΛYFUNITY. Aktuell gesendete Nachrichten: **" + mainChat.getIterableHistory().stream().count() + "**").queue();
            }
        }, 300000, 300000);

    }

}