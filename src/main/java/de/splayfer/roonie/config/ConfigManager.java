package de.splayfer.roonie.config;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.SlashCommandManager;
import de.splayfer.roonie.utils.enums.Guilds;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConfigManager implements SlashCommandManager {

    private final Roonie roonie;
    MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    @Override
    public SlashCommandData[] slashCommands() {
        return new SlashCommandData[]{
                Commands.slash("setup", "\uD83D\uDEE0️ │ Sende verwaltungsrelevante Nachrichten mithilfe dieses Commands")
                        .addOption(OptionType.STRING, "kategorie", "\uD83C\uDFF7️ │ Kategorie, über welche du Nachrichten versenden möchtest!", true, true)
                        .addOption(OptionType.INTEGER, "id", "⚙️ │ Id, der von dir verwendeten Kategorie (muss eine Ganzzahl sein)", false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        };
    }

    public MessageChannel getConfigChannel(String identifier) {
        return roonie.getMainGuild().getTextChannelById(mongoDB.find("config", "identifier", identifier).first().getLong("channel"));
    }

}
