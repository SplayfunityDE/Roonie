package de.splayfer.roonie.modules.library;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.CommandManager;
import de.splayfer.roonie.utils.enums.Guilds;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class LibraryManager {

    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    public static void init() {
        Roonie.builder.addEventListeners(new LibrarySetupCommand(), new BannerListener(), new BannerCommand(), new TemplateListener(), new TemplateCommand());
        Roonie.builder.addEventListeners(new NitroGamesListener(), new NitrogamesSetupCommand());

        CommandManager.addCommands(Guilds.MAIN,
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
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));
    }

    public static void addTemplate(String category, String url) {
        mongoDB.insert("templates", new Document()
                .append("category", category)
                .append("url", url));
    }

    public static void removeTemplate(String url) {
        mongoDB.drop("templates", "url", url);

    }

    public static List<String> getTemplatesByCategory(String category) {
        List<String> list = new ArrayList<>();
        mongoDB.find("templates", "category", category).forEach(document -> list.add(document.getString("url")));
        return list;
    }

    public static boolean existsTemplate(String message) {
        return mongoDB.exists("templates", "url", message);
    }


    public static void addBanner(String category, String url) {
        mongoDB.insert("banner", new Document()
                .append("category", category)
                .append("url", url));
    }

    public static void removeBanner(String url) {
        mongoDB.drop("banner", "url", url);
    }

    public static List<String> getBannerByCategory(String category) {
        List<String> list = new ArrayList<>();
        mongoDB.find("banner", "category", category).forEach(document -> list.add(document.getString("url")));
        return list;
    }

    public static boolean existsBanner(String message) {
        return mongoDB.exists("banner", "url", message);
    }

    public static boolean existsBannerCategory (String category) {
        return mongoDB.exists("banner", "category", category);
    }

}
