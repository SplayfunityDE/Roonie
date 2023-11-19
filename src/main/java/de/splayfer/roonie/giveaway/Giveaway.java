package de.splayfer.roonie.giveaway;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.bson.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Giveaway {

    static MongoDBDatabase mongoDB = new MongoDBDatabase("splayfunity");

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

    public List<?> keySet() {

        return new ArrayList<>(){{

            add(channel);
            add(prize);
            add(duration);
            add(timeFormat);
            add(requirement);
            add(amount);
            add(picture);
            add(message);

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

    public void insertToMySQL() {

        String req = (String) requirement.keySet().toArray()[0];
        String val = requirement.get(req);

        mongoDB.insert("giveaway", new Document()
                .append("channel", channel.getIdLong())
                .append("prize", prize)
                .append("duration", duration)
                .append("timeFormat", timeFormat)
                .append("requirement", req)
                .append("value", val)
                .append("amount", amount)
                .append("picture", picture)
                .append("message", message.getIdLong()));
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
        return selectGiveaway(Objects.requireNonNull(mongoDB.find("giveaway", "message", message.getIdLong()).first()));
    }

    public static List<String> getEntrys(Message message) {
        List<String> list = new ArrayList<>();
        mongoDB.find("giveawayEntrs", "message", message.getIdLong()).forEach(document -> list.add(String.valueOf(document.getLong("message"))));
        return list;

    }

    public List<String> getEntrys() {
        List<String> list = new ArrayList<>();
        for (Document doc : mongoDB.find("giveaway", new Document("message", message.getIdLong())))
            list.add(doc.getLong("guildMember").toString());
        return list;
    }

    public static void addEntry(Message message, Member member) {
        mongoDB.insert("giveawayEntrys", new Document().append("message", message.getIdLong()).append("guildMember", member.getIdLong()));

    }

    public static void removeEntry(Message message, Member member) {
        mongoDB.drop("giveawayEntrys",new Document().append("message", message.getId()).append("guildMember", member.getId()));
    }

    public static List<Giveaway> getAllGiveaways() {
        List<Document> list = new ArrayList<>();
        List<Giveaway> gwList = new ArrayList<>();
        mongoDB.findAll("giveaway").forEach(list::add);
        list.forEach(document -> gwList.add(selectGiveaway(document)));
        return gwList;
    }

    public void delete(Member member) {

        giveaways.remove(member);
    }

    public static Giveaway selectGiveaway(Document document) {
        TextChannel channel = Roonie.mainGuild.getTextChannelById(document.getLong("channel"));
        assert channel != null;
        return new Giveaway(channel, document.getString("prize"), document.getLong("duration"), document.getString("timeFormat"), new HashMap<>(){{put(document.getString("requirement"), document.getString("value"));}}, document.getInteger("amount"), document.getString("picture"), channel.getHistory().getMessageById(document.getLong("message")));
    }

}