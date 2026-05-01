package de.splayfer.roonie.config;

import de.splayfer.roonie.MongoDBDatabase;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.Channel;
import org.bson.Document;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

@Component
@DependsOn({"mongoDBDatabase"})
public class Config {

    private final MongoDBDatabase mongoDB;

    public Config() {
        this.mongoDB = MongoDBDatabase.getDatabase("splayfunity");
    }

    public void setConfigChannel(String identifier, Channel channel, Message message) {
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

    public void setConfigChannel(String identifier, Channel channel) {
        if (!mongoDB.exists("config", "identifier", identifier))
            mongoDB.insert("config", new Document()
                    .append("identifier", identifier)
                    .append("channel", channel.getIdLong()));
        else
            mongoDB.updateLine("config", "identifier", identifier, "channel", channel.getIdLong());
    }

    public void removeConfigChannel(String identifier) {
        mongoDB.drop("config", "identifier", identifier);
    }

    public boolean isConfigChannel(Channel channel, String identifier) {
        return mongoDB.exists("config", new Document().append("identifier", identifier).append("channel", channel.getIdLong()));
    }

    public long getConfigMessageId(String identifier) {
        return mongoDB.find("config", "identifier", identifier).first().getLong("message");
    }

    public boolean existsConfig(String identifier) {
        return mongoDB.exists("config", "identifier", identifier);
    }
}