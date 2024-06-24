package de.splayfer.roonie.modules.tempchannel;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.CommandManager;
import de.splayfer.roonie.utils.enums.Guilds;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.bson.Document;

public class TempchannelManager {

    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    public static void init() {
        Roonie.builder.addEventListeners(new JoinHubCommand(), new ChannelListener(), new ControlListener());

        CommandManager.addCommands(Guilds.MAIN,
                Commands.slash("voicehub", "⏳ │ Verwalte die Voicehubs für temporäre Sprachkanäle")
                        .addSubcommands(
                                new SubcommandData("add", "➕ │ Füge einen neuen Voicehub hinzu")
                                        .addOption(OptionType.CHANNEL, "kanal", "\uD83D\uDD09 │ Kanal, welchen du hinzufügen möchtest"),
                                new SubcommandData("remove", "➖ │ Entferne einen bestehenden Voicehub")
                                        .addOption(OptionType.CHANNEL, "kanal", "\uD83D\uDD09 │ Kanal, welchen du entfernen möchtest"))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));
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