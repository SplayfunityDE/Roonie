package de.splayfer.roonie.general.schedule;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.enums.Channels;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class BotCounter {

    protected static Guild guild;
    protected static List<Member> botList;

    public static void botCounterUpdate() {
        botList = new ArrayList<>();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                for (Member m: guild.getMembers())
                    if (m.getUser().isBot())
                        botList.add(m);
                int botcounter = botList.size();
                Channels.COMMANDCHAT.getTextChannel(Roonie.mainGuild).getManager().setTopic("Hier kannst du auf alle <:bot:893865039344316446> Bots von SPLΛYFUNITY zugreifen und sie bedienen! Aktuelle Bots: **" + botcounter + "**").queue();
            }
        }, 300000, 10000);
    }
}