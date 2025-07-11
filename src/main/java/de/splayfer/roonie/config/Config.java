package de.splayfer.roonie.config;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.bson.Document;

public class Config {

    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    public static void setConfigChannel(String identifier, Channel channel, Message message) {
        if (!mongoDB.exists("config", "identifier", identifier))
            mongoDB.insert("config", new Document()
                    .append("identifier", identifier)
                    .append("channel", channel.getIdLong())
                    .append("message", message.getIdLong()));
        else {
            mongoDB.updateLine("config", "identifier", identifier, "channel", channel.getIdLong());
            mongoDB.updateLine("config", "identifier", identifier, "message", message.getIdLong());
        }
    }

    public static void setConfigChannel(String identifier, Channel channel) {
        if (!mongoDB.exists("config", "identifier", identifier))
            mongoDB.insert("config", new Document()
                    .append("identifier", identifier)
                    .append("channel", channel.getIdLong()));
        else
            mongoDB.updateLine("config", "identifier", identifier, "channel", channel.getIdLong());
    }

    public static void removeConfigChannel(String identifier) {
        mongoDB.drop("config", "identifier", identifier);
    }

    public static boolean isConfigChannel(Channel channel, String identifier) {
        return mongoDB.exists("config", new Document().append("identifier", identifier).append("channel", channel.getIdLong()));
    }

    public static MessageChannel getConfigChannel(String identifier) {
        return Roonie.mainGuild.getChannelById(MessageChannel.class, mongoDB.find("config", "identifier", identifier).first().getLong("channel"));
    }

    public static long getConfigMessageId(String identifier) {
        return mongoDB.find("config", "identifier", identifier).first().getLong("message");
    }

    public static boolean existsConfig(String identifier) {
        return mongoDB.exists("config", "identifier", identifier);
    }
}