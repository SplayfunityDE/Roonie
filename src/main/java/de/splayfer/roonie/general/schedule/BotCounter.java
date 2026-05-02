package de.splayfer.roonie.general.schedule;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.enums.Channels;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Component
public class BotCounter {

    private final Roonie roonie;

    public BotCounter(@Lazy Roonie roonie) {
        this.roonie = roonie;
    }

    protected static List<Member> botList;

    @Scheduled(initialDelay = 1, fixedRate = 30, timeUnit = TimeUnit.MINUTES)
    public void botCounterUpdate() {
        botList = new ArrayList<>();
        for (Member m: roonie.getMainGuild().getMembers())
            if (m.getUser().isBot())
                botList.add(m);
        int botcounter = botList.size();
        Channels.COMMANDCHAT.getTextChannel( roonie.getMainGuild()).getManager().setTopic("Hier kannst du auf alle <:bot:893865039344316446> Bots von SPLΛYFUNITY zugreifen und sie bedienen! Aktuelle Bots: **" + botcounter + "**").queue();
    }
}