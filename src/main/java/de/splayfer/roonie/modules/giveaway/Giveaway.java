package de.splayfer.roonie.modules.giveaway;

import de.splayfer.roonie.MongoDBDatabase;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

import java.util.*;

public class Giveaway {

    MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    @Getter
    @Setter
    private MessageChannel channel;
    @Getter
    @Setter
    private String prize;
    @Getter
    @Setter
    private Long duration;
    @Getter
    @Setter
    private List<String> requirement;
    @Getter
    @Setter
    private Integer amount;
    @Getter
    @Setter
    private Message message;
    @Getter
    @Setter
    private List<Long> entrys;
    @Getter
    @Setter
    private String picture;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Giveaway giveaway = (Giveaway) o;
        return duration == giveaway.duration && amount == giveaway.amount && Objects.equals(channel, giveaway.channel) && Objects.equals(prize, giveaway.prize) && Objects.equals(requirement, giveaway.requirement) && Objects.equals(picture, giveaway.picture) && Objects.equals(message, giveaway.message) && Objects.equals(entrys, giveaway.entrys);
    }

    @Override
    public String toString() {
        return "Giveaway{" +
                "channel=" + channel +
                ", prize='" + prize + '\'' +
                ", duration=" + duration +
                ", requirement=" + requirement +
                ", amount=" + amount +
                ", message=" + message +
                ", entrys=" + entrys +
                ", picture='" + picture + '\'' +
                '}';
    }

    public Giveaway(MessageChannel channel, String prize, Long duration, List<String> requirement, Integer amount, String picture, Message message, List<Long> entrys) {
        this.channel = channel;
        this.prize = prize;
        this.duration  = duration;
        this.requirement = requirement;
        this.amount = amount;
        this.picture = picture;
        this.message = message;
        this.entrys = entrys;
    }

    public static Giveaway create(Member member) {
        Giveaway giveaway = new Giveaway(null, null, null, null, null, null, null, null);
        GiveawayManager.giveaways.put(member, giveaway);
        return giveaway;
    }

    public List<?> keySet() {
        return new ArrayList<>(){{
            add(channel);
            add(prize);
            add(duration);
            add(requirement);
            add(amount);
            add(picture);
            add(message);
            add(entrys);
        }};
    }

    public int getStep() {
        return 0;
    }

    public static int getStep(Member member) {
        return GiveawayManager.giveaways.get(member).getStep();
    }

    public static Giveaway getFromMember(Member member) {
        return GiveawayManager.giveaways.get(member);
    }

    public static boolean existsGiveaway(Member member) {
        return GiveawayManager.giveaways.containsKey(member);
    }

    public void addEntry(Member member) {
        entrys.add(member.getIdLong());
        mongoDB.updateLine("giveaway", "message", message.getIdLong(), "entrys", entrys);
    }

    public void removeEntry(Member member) {
        entrys.remove(member.getIdLong());
        mongoDB.updateLine("giveaway", "message", message.getIdLong(), "entrys", entrys);
    }
}