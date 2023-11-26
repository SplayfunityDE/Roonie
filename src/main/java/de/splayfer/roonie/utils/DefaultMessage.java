package de.splayfer.roonie.utils;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public class DefaultMessage {

    private static final String errorEmoji = ":no_entry_sign:";
    private static final String successEmoji = "✅";

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

    public static List<MessageEmbed> error(String title, String description, MessageEmbed.Field... fields) {
        return new DefaultMessage("error", title, description, fields).load();
    }

    public static List<MessageEmbed> error(String title, String description) {
        return new DefaultMessage("error", title, description).load();
    }

    public static List<MessageEmbed> error(String title) {
        return new DefaultMessage("error", title).load();
    }

    public static List<MessageEmbed> success(String title, String description, MessageEmbed.Field... fields) {
        return new DefaultMessage("success", title, description, fields).load();
    }

    public static List<MessageEmbed> success(String title, String description) {
        return new DefaultMessage("success", title, description).load();
    }

    public static List<MessageEmbed> success(String title) {
        return new DefaultMessage("success", title).load();
    }

    public static List<MessageEmbed> success2(String title, String description, MessageEmbed.Field... fields) {
        return new DefaultMessage("success", title, description, fields).load();
    }

    public static List<MessageEmbed> success2(String title, String description) {
        return new DefaultMessage("success", title, description).load();
    }

    public static List<MessageEmbed> success2(String title) {
        return new DefaultMessage("success", title).load();
    }

    private List<MessageEmbed> load() {
        EmbedBuilder bannerEmbed = new EmbedBuilder();
        EmbedBuilder reply = new EmbedBuilder();
        reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

        int color;
        String emoji = null;
        switch (type) {
            case "error" -> {
                bannerEmbed.setColor(0xed4245);
                bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");
                reply.setColor(0xed4245);
                emoji = errorEmoji;
            }
            case "success" -> {
                bannerEmbed.setColor(0x43b480);
                bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");
                reply.setColor(0x43b480);
                emoji = successEmoji;
            }

            case "success2" -> {
                bannerEmbed.setColor(0x43b480);
                bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914598873719263282/banner_erfolg2.png");
                reply.setColor(0x43b480);
                emoji = successEmoji;
            }
        }
        reply.setTitle(emoji + " **" + title.toUpperCase() + "**");
        if (description != null) {
            reply.setDescription("> " + description);
        }
        if (fields != null) {
            for (MessageEmbed.Field f : fields) {
                reply.addField(f);
            }
        }
        return List.of(bannerEmbed.build(), reply.build());
    }

}