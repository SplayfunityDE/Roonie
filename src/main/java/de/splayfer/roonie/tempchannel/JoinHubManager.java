package de.splayfer.roonie.tempchannel;

import de.splayfer.roonie.MySQLDatabase;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class JoinHubManager {

    @Getter
    private static MySQLDatabase database = new MySQLDatabase("splayfunity");

    public static void createJoinHub(VoiceChannel channel, Member creator) {

        getDatabase().insert("JoinHubs", new String[]{"channel", "creator"}, channel.getId(), creator.getId());
    }

    public static void removeJoinHub(VoiceChannel channel) {

        getDatabase().update("DELETE FROM JoinHubs WHERE channel = ?", channel.getId());

    }

    public static boolean existesJoinHub(String channelId) {
        return getDatabase().existsEntry("JoinHubs", "channel = ?", channelId);
    }

}