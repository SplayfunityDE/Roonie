package de.splayfer.roonie.modules.library;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.utils.SlashCommandManager;
import de.splayfer.roonie.utils.enums.Guilds;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class LibraryManager implements SlashCommandManager {

    MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    @Override
    public SlashCommandData[] slashCommands() {
        return new SlashCommandData[]{
                Commands.slash("banner", "\uD83D\uDDBC\uFE0F │ Verwalte die Banner der Bibliothek")
                        .addSubcommands(
                                new SubcommandData("add", "➕ │ Füge einen Banner hinzu")
                                        .addOption(OptionType.STRING, "category", "\uD83D\uDCC2 │ Weise dem Banner eine Kategorie zu")
                                        .addOption(OptionType.STRING, "url", "\uD83D\uDD17 │ Gib den Link zu dem Banner an"),
                                new SubcommandData("remove", "➖ │ Entferne einen Banner")
                                        .addOption(OptionType.STRING, "url", "\uD83D\uDD17 │ Gib den Link zu dem Banner an"))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("template", "\uD83D\uDCDC │ Verwalte die Server-Vorlagen der Bibliothek")
                        .addSubcommands(
                                new SubcommandData("add", "➕ │ Füge eine Server-Vorlage hinzu!")
                                        .addOption(OptionType.STRING, "category", "\uD83D\uDCC2 │ Weise der Template eine Kategorie zu!")
                                        .addOption(OptionType.STRING, "url", "\uD83D\uDD17 │ Gib den Link der Template an"),
                                new SubcommandData("remove", "➖ │ Entferne eine Server-Vorlage")
                                        .addOption(OptionType.STRING, "url", "\uD83D\uDD17 │ Gib den Link der Template an"))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        };
    }

    public void addTemplate(String category, String url) {
        mongoDB.insert("templates", new Document()
                .append("category", category)
                .append("url", url));
    }

    public void removeTemplate(String url) {
        mongoDB.drop("templates", "url", url);

    }

    public List<String> getTemplatesByCategory(String category) {
        List<String> list = new ArrayList<>();
        mongoDB.find("templates", "category", category).forEach(document -> list.add(document.getString("url")));
        return list;
    }

    public boolean existsTemplate(String message) {
        return mongoDB.exists("templates", "url", message);
    }


    public void addBanner(String category, String url) {
        mongoDB.insert("banner", new Document()
                .append("category", category)
                .append("url", url));
    }

    public void removeBanner(String url) {
        mongoDB.drop("banner", "url", url);
    }

    public List<String> getBannerByCategory(String category) {
        List<String> list = new ArrayList<>();
        mongoDB.find("banner", "category", category).forEach(document -> list.add(document.getString("url")));
        return list;
    }

    public boolean existsBanner(String message) {
        return mongoDB.exists("banner", "url", message);
    }

    public boolean existsBannerCategory (String category) {
        return mongoDB.exists("banner", "category", category);
    }
}
