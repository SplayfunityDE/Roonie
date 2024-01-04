package de.splayfer.roonie.modules.giveaway;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.modules.level.LevelManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.bson.Document;

import java.util.*;

public class Giveaway {

    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    private static HashMap<Member, Giveaway> giveaways = new HashMap<>();

    private MessageChannel channel;
    private String prize;
    private Long duration;
    private List<String> requirement;
    private Integer amount;
    private Message message;
    private List<Long> entrys;

    public String getPicture() {
        return picture;
    }

    private String picture;

    public MessageChannel getChannel() {
        return channel;
    }

    public String getPrize() {
        return prize;
    }

    public Long getDuration() {
        return duration;
    }

    public List<String> getRequirement() {
        return requirement;
    }

    public Integer getAmount() {
        return amount;
    }

    public Message getMessage() {
        return message;
    }
    public List<Long> getEntrys() {
        return entrys;
    }

    public void setChannel(MessageChannel channel) {
        this.channel = channel;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public void setRequirement(List<String> requirement) {
        this.requirement = requirement;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    public void setEntrys(List<Long> entrys) {
        this.entrys = entrys;
    }

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
        giveaways.put(member, giveaway);
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
        int step = 1;
        for (Object t : keySet()) {
            if (t != null) {
                step++;
            } else {
                break;
            }
        }
        return step;
    }

    public static int getStep(Member member) {
        return giveaways.get(member).getStep();
    }

    public static Giveaway getFromMember(Member member) {
        return giveaways.get(member);
    }

    public static boolean existsGiveaway(Member member) {
        return giveaways.containsKey(member);
    }

    public void insertToMongoDB() {
        if (entrys == null)
            entrys = new ArrayList<>();
        mongoDB.insert("giveaway", new Document()
                .append("channel", channel.getIdLong())
                .append("prize", prize)
                .append("duration", duration)
                .append("requirement", requirement)
                .append("amount", amount)
                .append("picture", picture)
                .append("message", message.getIdLong())
                .append("entrys", entrys));
        for (Member m : giveaways.keySet())
            if (giveaways.get(m).equals(this))
                giveaways.remove(m);
    }

    public void removeFromMongoDB() {
        mongoDB.drop("giveaway", "message", message.getIdLong());
        for (Member m : giveaways.keySet())
            if (giveaways.get(m).equals(Giveaway.getFromMessage(message)))
                giveaways.remove(m);
    }

    public static boolean isGiveaway(Message message) {
        return mongoDB.exists("giveaway", "message", message.getIdLong());
    }

    public static Giveaway getFromMessage(Message message) {
        return getFromDocument(Objects.requireNonNull(mongoDB.find("giveaway", "message", message.getIdLong()).first()));
    }

    public void addEntry(Member member) {
        entrys.add(member.getIdLong());
        mongoDB.updateLine("giveaway", "message", message.getIdLong(), "entrys", entrys);
    }

    public void removeEntry(Member member) {
        entrys.remove(member.getIdLong());
        mongoDB.updateLine("giveaway", "message", message.getIdLong(), "entrys", entrys);
    }

    public static List<Giveaway> getAllGiveaways() {
        List<Giveaway> gwList = new ArrayList<>();
        mongoDB.findAll("giveaway").forEach(document -> gwList.add(getFromDocument(document)));
        return gwList;
    }

    public static Giveaway getFromDocument(Document document) {
        TextChannel channel = Roonie.mainGuild.getTextChannelById(document.getLong("channel"));
        assert channel != null;
        return new Giveaway(channel, document.getString("prize"), document.getLong("duration"), document.getList("requirement", String.class), document.getInteger("amount"), document.getString("picture"), channel.getHistory().getMessageById(document.getLong("message")), document.getList("entrys", Long.class));
    }

    public static Giveaway findGiveaway(Message message) {
        return getFromDocument(mongoDB.find("giveaway", new Document("message", message.getIdLong())).first());
    }

    public boolean checkRequirement(Member member) {
        return switch (requirement.get(0)) {
            case "role" -> member.getRoles().contains(member.getGuild().getRoleById(requirement.get(1)));
            case "level" -> LevelManager.getLevel(member) >= Integer.parseInt(requirement.get(1));
            default -> true;
        };
    }
}