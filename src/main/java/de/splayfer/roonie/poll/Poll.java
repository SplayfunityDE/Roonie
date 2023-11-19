package de.splayfer.roonie.poll;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.*;

public class Poll {

    static HashMap<Member, Poll> polls = new HashMap<>();

    private MessageChannel channel;
    private String topic;
    private String description;
    private Long duration;
    private String style;
    private Message message;
    private Button[] buttons;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public MessageChannel getChannel() {
        return channel;
    }

    public void setChannel(MessageChannel channel) {
        this.channel = channel;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Button[] getButtons() {
        return buttons;
    }

    public void setButtons(Button[] buttons) {
        this.buttons = buttons;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Poll poll = (Poll) o;
        return Objects.equals(channel, poll.channel) && Objects.equals(topic, poll.topic) && Objects.equals(description, poll.description) && Objects.equals(duration, poll.duration) && Objects.equals(style, poll.style) && Arrays.equals(buttons, poll.buttons) && Objects.equals(message, poll.message);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(channel, topic, description, duration, style, message);
        result = 31 * result + Arrays.hashCode(buttons);
        return result;
    }

    @Override
    public String toString() {
        return "Poll{" +
                "channel=" + channel +
                ", topic='" + topic + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", style='" + style + '\'' +
                ", message=" + message +
                ", buttons=" + Arrays.toString(buttons) +
                '}';
    }

    public Poll(MessageChannel channel, String topic, String description, Long duration, String style, Message message, Button... buttons) {

        this.channel = channel;
        this.topic = topic;
        this.description = description;
        this.duration = duration;
        this.style = style;
        this.message = message;
        this.buttons = buttons;

    }

    public <T> List<T> keySet() {

        return new ArrayList<>(){{

            add((T) channel);
            add((T) topic);
            add((T) description);
            add((T) duration);
            add((T) style);
            add((T) message);
            add((T) buttons);

        }};

    }

    public int getStep() {

        Poll poll = new Poll(channel, topic, description, duration, style, message, buttons);
        int step = 1;

        for (Object t : poll.keySet()) {
            if (t != null) {
                step++;
            } else {
                break;
            }
        }

        return step;

    }

    public static int getStep(Member member) {

        Poll poll = Poll.getFromMember(member);
        int step = 1;
        for (Object t : poll.keySet()) {
            if (t != null) {
                step++;
            } else {
                break;
            }
        }

        return step;

    }

    public static Poll getFromMember(Member member) {

        return polls.get(member);
    }

    public static Poll create(Member member) {

        Poll poll = new Poll(null, null, null, null, null, null);
        polls.put(member, poll);

        return poll;

    }

    public static boolean existsPoll(Member member) {

        return polls.containsKey(member);

    }

}
