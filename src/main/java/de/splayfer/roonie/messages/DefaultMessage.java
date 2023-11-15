package de.splayfer.roonie.messages;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.util.Arrays;
import java.util.HashMap;

public class DefaultMessage {

    private static int errorColor = 0xc01c34;
    private static int successColor = 0x3aa65b;
    private static String errorEmoji = "❗";
    private static String successEmoji = "✅";

    private String type;
    private String title;
    private String description;
    private MessageEmbed.Field[] fields;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MessageEmbed.Field[] getFields() {
        return fields;
    }

    public void setFields(MessageEmbed.Field[] fields) {
        this.fields = fields;
    }

    public DefaultMessage(String type, String title, String description, MessageEmbed.Field... fields) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.fields = fields;
    }

    public DefaultMessage(String type, String title, String description) {
        this.type = type;
        this.title = title;
        this.description = description;
    }

    public DefaultMessage(String type, String title) {
        this.type = type;
        this.title = title;
    }

    public static MessageEmbed error(String title, String description, MessageEmbed.Field... fields) {
        return new DefaultMessage("error", title, description, fields).load();
    }

    public static MessageEmbed error(String title, String description) {
        return new DefaultMessage("error", title, description).load();
    }

    public static MessageEmbed error(String title) {
        return new DefaultMessage("error", title).load();
    }

    public static MessageEmbed success(String title, String description, MessageEmbed.Field... fields) {
        return new DefaultMessage("success", title, description, fields).load();
    }

    public static MessageEmbed success(String title, String description) {
        return new DefaultMessage("success", title, description).load();
    }

    public static MessageEmbed success(String title) {
        return new DefaultMessage("success", title).load();
    }

    private MessageEmbed load() {

        int color;
        String emoji;
        switch (type) {
            case "error" -> {
                color = errorColor;
                emoji = errorEmoji;
            }
            case "success" -> {
                color = successColor;
                emoji = successEmoji;
            }
            default -> {
                color = 0x2e3137;
                emoji = "";
            }
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(color);
        embedBuilder.setTitle(emoji + " **" + title + "**");

        if (description != null) {
            embedBuilder.setDescription(description);
        }
        if (fields != null) {
            for (MessageEmbed.Field f : fields) {
                embedBuilder.addField(f);
            }
        }

        return embedBuilder.build();
    }


}
