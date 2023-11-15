package de.splayfer.roonie.schedule;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BotCounter {

    protected static TextChannel commmandsChat;
    protected static Guild guild;
    protected static List<Member> botList;

    public static void botCounterUpdate() {

        try {
            guild = Roonie.shardMan.awaitReady().getGuildById("873506353551925308");
            commmandsChat = Roonie.shardMan.awaitReady().getTextChannelById("905384274214133770");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        botList = new ArrayList<>();

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {

                for (Member m: guild.getMembers()) {

                    if (m.getUser().isBot()) {

                        botList.add(m);

                    }

                }

                int botcounter = botList.size();

                commmandsChat.getManager().setTopic("Hier kannst du auf alle <:bot:893865039344316446> Bots von SPLΛYFUNITY zugreifen und sie bedienen! Aktuelle Bots: **" + botcounter + "**").queue();

                botCounterUpdate();

                t.cancel();

            }
        }, 300000);

    }
}
