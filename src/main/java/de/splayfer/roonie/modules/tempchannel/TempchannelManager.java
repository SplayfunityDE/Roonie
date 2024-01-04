package de.splayfer.roonie.modules.tempchannel;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import org.bson.Document;

public class TempchannelManager {

    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    public static void init() {
        Roonie.builder.addEventListeners(new CreateJoinHubCommand(), new RemoveJoinHubCommand(), new ChannelListener(), new ControlListener());
    }

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