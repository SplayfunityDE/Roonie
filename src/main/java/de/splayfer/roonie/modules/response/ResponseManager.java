package de.splayfer.roonie.modules.response;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.SlashCommandManager;
import de.splayfer.roonie.utils.enums.Guilds;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.bson.Document;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
public class ResponseManager implements SlashCommandManager {

    private final Roonie roonie;
    private final MongoDBDatabase mongoDB;

    public ResponseManager(@Lazy Roonie roonie) {
        this.roonie = roonie;
        this.mongoDB = MongoDBDatabase.getDatabase("splayfunity");
    }
    @Override
    public SlashCommandData[] slashCommands() {
        return new SlashCommandData[]{
                Commands.slash("response", "\uD83D\uDCDC   Verwalte automatisierte Reaktionen des Bots")
                        .addSubcommands(new SubcommandData("add", "\uD83D\uDCDC │ Füge automatisierte Reaktionen hinzu")
                                        .addOption(OptionType.STRING, "nachricht", "\uD83D\uDCE2 │ Nachricht, auf die der Bot reagieren soll", true),
                                new SubcommandData("remove", "\uD83D\uDCDC │ Entferne automatisierte Reaktionen")
                                        .addOption(OptionType.STRING, "nachricht", "\uD83D\uDCE2 │ Nachricht, auf die der Bot reagieren soll", true))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        };
    }

    public Response getResponse(String message) {
        Document doc = mongoDB.find("response", "message", message).first();
        return new Response(doc.getString("message"), roonie.getShardMan().getUserById(doc.getLong("creator")), doc.getString("type"), doc.getString("value"));
    }

    public void create(String message, User creator, String type, String value) {
        mongoDB.insert("response", new Response(message, creator, type, value).getDocument());
    }

    public void removeResponse(String message) {
        mongoDB.drop("response", "message", message);
    }

    public boolean existsResponse(String message){
        return mongoDB.exists("response", "message", message);
    }

    public boolean existsCreator(long member) {
        return mongoDB.exists("response", "creator", member);
    }
}