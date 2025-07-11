package de.splayfer.roonie.modules.poll;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bson.Document;

import java.util.*;

public class Poll {

    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");
    static HashMap<Member, Poll> polls = new HashMap<>();

    @Getter
    @Setter
    private MessageChannel channel;
    @Getter
    @Setter
    private String topic;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private Message message;
    @Getter
    @Setter
    private Button[] buttons;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poll poll = (Poll) o;
        return Objects.equals(channel, poll.channel) && Objects.equals(topic, poll.topic) && Objects.equals(description, poll.description) && Arrays.equals(buttons, poll.buttons) && Objects.equals(message, poll.message);
    }

    @Override
    public String toString() {
        return "Poll{" +
                "channel=" + channel +
                ", topic='" + topic + '\'' +
                ", description='" + description + '\'' +
                ", message=" + message +
                ", buttons=" + Arrays.toString(buttons) +
                '}';
    }

    public Poll(MessageChannel channel, String topic, String description, Message message, Button... buttons) {
        this.channel = channel;
        this.topic = topic;
        this.description = description;
        this.message = message;
        this.buttons = buttons;
    }

    public <T> List<T> keySet() {
        return new ArrayList<>(){{
            add((T) channel);
            add((T) topic);
            add((T) description);
            add((T) message);
            add((T) buttons);
        }};
    }

    public int getStep() {
        int step = 1;
        for (Object t : this.keySet()) {
            if (t != null)
                step++;
            else
                break;
        }
        return step;
    }

    public static Poll getFromMember(Member member) {
        return polls.get(member);
    }

    public static Poll create(Member member) {
        Poll poll = new Poll(null, null, null, null);
        polls.put(member, poll);
        return poll;
    }

    public void insertToMongoDB() {
        List<String> values = new ArrayList<>();
        message.getButtons().forEach(button -> values.add(button.getId()));
        mongoDB.insert("poll", new Document()
                .append("channel", channel.getIdLong())
                .append("topic", topic)
                .append("description", description)
                .append("message", message.getIdLong())
                .append("buttons", values));
        Document document = new Document().append("channel", channel.getIdLong()).append("message", message.getIdLong());
        List<Long> emptyList = new ArrayList<>();
        for (String id : values)
            document.append(id, emptyList);
        mongoDB.insert("pollEntrys", document);
        for (Member m : polls.keySet())
            if (polls.get(m).equals(this))
                polls.remove(m);
    }

    public static boolean isPoll(MessageChannel channel, Message message) {
        return mongoDB.exists("poll", new Document().append("channel", channel.getIdLong()).append("message", message.getIdLong()));
    }

    public static Poll getFromMongoBD(MessageChannel channel, Message message) {
        Document document = mongoDB.find("poll", new Document().append("channel", channel.getIdLong()).append("message", message.getIdLong())).first();
        MessageChannel msgChannel = (MessageChannel) Roonie.mainGuild.getGuildChannelById(document.getLong("channel"));
        Message msg = msgChannel.retrieveMessageById(document.getLong("message")).complete();
        return new Poll(msgChannel, document.getString("topic"), document.getString("description"), msg, message.getActionRows().get(0).getButtons().toArray(new Button[message.getActionRows().get(0).getButtons().size()]));
    }

    public boolean hasClicked(Member member, String buttonId) {
        for (long id : mongoDB.find("pollEntrys", new Document().append("channel", channel.getIdLong()).append("message", message.getIdLong())).first().getList(buttonId, Long.class))
            if (member.getIdLong() == id)
                return true;
        return false;
    }

    public boolean hasVoted(Member member) {
        Document document = mongoDB.find("pollEntrys", new Document().append("channel", channel.getIdLong()).append("message", message.getIdLong())).first();
        for (String id : document.keySet())
            if (id.startsWith("button"))
                if (document.getList(id, Long.class).contains(member.getIdLong()))
                    return true;
        return false;
    }

    public String getVote(Member member) {
        Document document = mongoDB.find("pollEntrys", new Document().append("channel", channel.getIdLong()).append("message", message.getIdLong())).first();
        for (String id : document.keySet())
            if (id.startsWith("button"))
                if (document.getList(id, Long.class).contains(member.getIdLong()))
                    return id;
        return null;
    }
    
    public void updateVote(Member member, String buttonId, VoteAction action) {
        Document document = mongoDB.find("pollEntrys", new Document().append("channel", channel.getIdLong()).append("message", message.getIdLong())).first();
        List<Long> list = document.getList(buttonId, Long.class);
        switch(action) {
            case VOTE -> {list.add(member.getIdLong());}
            case UNVOTE -> {list.remove(member.getIdLong());}
        }
        mongoDB.updateLine("pollEntrys", new Document().append("channel", channel.getIdLong()).append("message", message.getIdLong()), buttonId, list);
        List<Button> buttons = new ArrayList<>();
        for (String id : document.keySet())
            if (message.getButtonById(id) != null)
                buttons.add(message.getButtonById(id).withLabel(message.getButtonById(id).getLabel().substring(0, message.getButtonById(id).getLabel().length() - 2) + document.getList(id, Long.class).size() + ")"));
        message.editMessageComponents(ActionRow.of(buttons)).queue();
    }
    
}

enum VoteAction {
    VOTE,
    UNVOTE;
}