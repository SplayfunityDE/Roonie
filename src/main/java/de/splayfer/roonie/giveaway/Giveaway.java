package de.splayfer.roonie.giveaway;

import de.splayfer.roonie.MongoDBDatabase;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Giveaway {

    static MongoDBDatabase mongoDB = new MongoDBDatabase();

    private static HashMap<Member, Giveaway> giveaways = new HashMap<>();

    private MessageChannel channel;
    private String prize;
    private Long duration;
    private String timeFormat;
    private HashMap<String, String> requirement;
    private Integer amount;
    private Message message;

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

    public String getTimeFormat() {
        return timeFormat;
    }

    public HashMap<String, String> getRequirement() {
        return requirement;
    }

    public Integer getAmount() {
        return amount;
    }

    public Message getMessage() {
        return message;
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

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public void setRequirement(HashMap<String, String> requirement) {
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

    @Override
    public String toString() {
        return "Giveaway{" +
                "channel=" + channel +
                ", prize='" + prize + '\'' +
                ", duration=" + duration +
                ", timeFormat='" + timeFormat + '\'' +
                ", requirement=" + requirement +
                ", amount=" + amount +
                ", picture='" + picture + '\'' +
                ", message=" + message +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Giveaway giveaway = (Giveaway) o;
        return duration == giveaway.duration && amount == giveaway.amount && Objects.equals(channel, giveaway.channel) && Objects.equals(prize, giveaway.prize) && Objects.equals(timeFormat, giveaway.timeFormat) && Objects.equals(requirement, giveaway.requirement) && Objects.equals(picture, giveaway.picture) && Objects.equals(message, giveaway.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel, prize, duration, timeFormat, requirement, amount, picture);
    }

    public Giveaway(MessageChannel channel, String prize, Long duration, String timeFormat, HashMap<String, String> requirement, Integer amount, String picture, Message message) {

        this.channel = channel;
        this.prize = prize;
        this.duration  = duration;
        this.timeFormat = timeFormat;
        this.requirement = requirement;
        this.amount = amount;
        this.picture = picture;
        this.message = message;

    }

    public static Giveaway create(Member member) {

        Giveaway giveaway = new Giveaway(null, null, null, null, null, null, null, null);
        giveaways.put(member, giveaway);

        return giveaway;

    }

    public <T> List<?> keySet() {

        List<?> list = new ArrayList<>(){{

            add(channel);
            add(prize);
            add(duration);
            add(timeFormat);
            add(requirement);
            add(amount);
            add(picture);
            add(message);

        }};

        return list;

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

        boolean check = false;

        if (giveaways.containsKey(member)) check = true;

        return check;

    }

    public void insertToMySQL() {

        String req = (String) requirement.keySet().toArray()[0];
        String val = requirement.get(req);

        getDatabase().insert("GiveawayStats", new String[]{"channel", "prize", "duration", "timeFormat", "requirement", "value", "amount", "picture", "message"}, channel.getId(), prize, duration, timeFormat, req, val, amount, picture, message.getId());
    }

    public void removeFromMySQl() {

        mongoDB.drop("giveaway", "message", message.getIdLong());
        mongoDB.drop("giveawayEntrys", "message", message.getIdLong());

        for (Member m : giveaways.keySet()) {
            if (giveaways.get(m).equals(Giveaway.getFromMessage(message))) {
                giveaways.remove(m);
            }
        }

    }

    public static boolean isGiveaway(Message message) {
        return mongoDB.exists("giveaway", "message", message.getIdLong());
    }

    public static Giveaway getFromMessage(Message message) {

        return getDatabase().selectGiveaway("SELECT channel, prize, duration, timeFormat, requirement, value, amount, picture, message FROM GiveawayStats WHERE message = ?", message.getId());

    }

    public static List<String> getEntrys(Message message) {

        return getDatabase().getGiveawayEntrys( "SELECT guildMember FROM GiveawayEntrys WHERE message = ?", message.getId());

    }

    public List<String> getEntrys() {

        return getDatabase().getGiveawayEntrys( "SELECT guildMember FROM GiveawayEntrys WHERE message = ?", message.getId());

    }

    public static void addEntry(Message message, Member member) {
        mongoDB.insert("giveawayEntrys", new Document().append("message", message.getIdLong()).append("guildMember", member.getIdLong()));

    }

    public static void removeEntry(Message message, Member member) {
        mongoDB.drop("giveawayEntrys", );
        getDatabase().update("DELETE FROM GiveawayEntrys WHERE message = ? AND guildMember = ?", message.getId(), member.getId());
    }

    public static List<Giveaway> getAllGiveaways() {

        return getDatabase().selectAllGiveaways("SELECT channel, prize, duration, timeFormat, requirement, value, amount, picture, message FROM GiveawayStats");
    }

    public void delete(Member member) {

        giveaways.remove(member);
    }

}
