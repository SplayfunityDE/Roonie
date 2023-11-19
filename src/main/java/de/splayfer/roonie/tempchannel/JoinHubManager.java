package de.splayfer.roonie.tempchannel;

import de.splayfer.roonie.MongoDBDatabase;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.bson.Document;

public class JoinHubManager {

    static MongoDBDatabase mongoDB = new MongoDBDatabase("splayfunity");

    public static void createJoinHub(VoiceChannel channel, Member creator) {
        mongoDB.insert("voicehubs", new Document().append("channel", channel.getIdLong()).append("creator", creator.getIdLong()));
    }

    public static void removeJoinHub(VoiceChannel channel) {
        mongoDB.drop("voicehubs", "channel", channel.getIdLong());

    }

    public static boolean existesJoinHub(long channelId) {
        return mongoDB.exists("voicehubs", "channel", channelId);
    }
}