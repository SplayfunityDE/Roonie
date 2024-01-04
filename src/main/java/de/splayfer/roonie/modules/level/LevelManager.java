package de.splayfer.roonie.modules.level;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.CommandManager;
import de.splayfer.roonie.utils.enums.Guilds;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.bson.Document;

public class LevelManager extends ListenerAdapter {

    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    public static void init() {
        Roonie.builder.addEventListeners(new LevelListener(), new LevelInfoCommand(), new RankCommand(), new LevelCommand(), new XpCommand());

        CommandManager.addCommands(Guilds.MAIN,
                Commands.slash("rank", "\uD83D\uDCCB │ Zeigt dir deinen aktuellen Rank an!")
                        .addOption(OptionType.USER, "nutzer", "Wähle einen bestimmten Nutzer!", false),
                Commands.slash("levels", "✨ │ Schau dir unsere Level-Vorteile an!"),
                Commands.slash("level", "⚙ │ Verwalte die Level eines Nutzers!")
                        .addSubcommands(new SubcommandData("add", "➕ │ Füge dem Nutzer eine bestimmte Anzahl von Leveln hinzu!")
                                        .addOption(OptionType.USER, "nutzer", "\uD83D\uDC65 │ Nutzer, dessen Level du verwalten möchtest!", true)
                                        .addOption(OptionType.INTEGER, "anzahl", "\uD83D\uDCD1 │ Anzahl der zu verwaltenden Level!", true),
                                new SubcommandData("remove", "➖ │ Entferne dem Nutzer eine bestimmte Anzahl von Leveln!")
                                        .addOption(OptionType.USER, "nutzer", "\uD83D\uDC65 │ Nutzer, dessen Level du verwalten möchtest!", true)
                                        .addOption(OptionType.INTEGER, "anzahl", "\uD83D\uDCD1 │ Anzahl der zu verwaltenden Level!", true),
                                new SubcommandData("set", "\uD83D\uDCC3 │ Setze dem Nutzer die Anzahl seiner Level!")
                                        .addOption(OptionType.USER, "nutzer", "\uD83D\uDC65 │ Nutzer, dessen Level du verwalten möchtest!", true)
                                        .addOption(OptionType.INTEGER, "anzahl", "\uD83D\uDCD1 │ Anzahl der zu verwaltenden Level!", true))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("xp", "⚙ │ Verwalte die Xp eines Nutzers!")
                        .addSubcommands(new SubcommandData("add", "➕ │ Füge dem Nutzer eine bestimmte Anzahl an Xp hinzu!")
                                        .addOption(OptionType.USER, "nutzer", "\uD83D\uDC65 │ Nutzer, dessen Xp du verwalten möchtest!", true)
                                        .addOption(OptionType.INTEGER, "anzahl", "\uD83D\uDCD1 │ Anzahl der zu verwaltenden Xp!", true),
                                new SubcommandData("remove", "➖ │ Entferne dem Nutzer eine bestimmte Anzahl an Xp!")
                                        .addOption(OptionType.USER, "nutzer", "\uD83D\uDC65 │ Nutzer, dessen Xp du verwalten möchtest!", true)
                                        .addOption(OptionType.INTEGER, "anzahl", "\uD83D\uDCD1 │ Anzahl der zu verwaltenden Xp!", true),
                                new SubcommandData("set", "\uD83D\uDCC3 │ Setze dem Nutzer die Anzahl seiner Xp!")
                                        .addOption(OptionType.USER, "nutzer", "\uD83D\uDC65 │ Nutzer, dessen Xp du verwalten möchtest!", true)
                                        .addOption(OptionType.INTEGER, "anzahl", "\uD83D\uDCD1 │ Anzahl der zu verwaltenden Xp!", true))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));

        LevelListener.checkVoiceMembers();
    }

    public static void addXpToUser(Member member, int amount) {
        insertUser(member);
        while (amount >= getLevelStep(getLevel(member))) {
            amount -= getLevelStep(getLevel(member));
            addLevelToUser(member, 1);
        }
        mongoDB.updateLine("level", new Document("guildMember", member.getIdLong()), "xp", getXp(member) + amount);
    }

    public static void removeXpFromUser(Member member, int amount) {
        insertUser(member);
        if (amount > getXp(member))
            amount = getXp(member);
        mongoDB.updateLine("level", new Document("guildMember", member.getIdLong()), "xp", getXp(member) - amount);
    }

    public static void setXp(Member member, int amount) {
        insertUser(member);
        mongoDB.updateLine("level", new Document("guildMember", member.getIdLong()), "xp", amount);
    }

    public static void addLevelToUser(Member member, int amount) {
        insertUser(member);
        mongoDB.updateLine("level", new Document("guildMember", member.getIdLong()), "level", getLevel(member) + amount);
    }

    public static void removeLevelFromUser(Member member, int amount) {
        insertUser(member);
        if (amount > getLevel(member))
            amount = getLevel(member);
        mongoDB.updateLine("level", new Document("guildMember", member.getIdLong()), "level", getLevel(member) - amount);
    }

    public static void setLevel(Member member, int amount) {
        insertUser(member);
        mongoDB.updateLine("level", new Document("guildMember", member.getIdLong()), "level", amount);
    }

    public static int getLevel(Member member) {
        if (existsUser(member))
            return mongoDB.find("level", "guildMember", member.getIdLong()).first().getInteger("level");
        else
            return 0;
    }

    public static int getXp(Member member) {
        insertUser(member);
        return mongoDB.find("level", "guildMember", member.getIdLong()).first().getInteger("xp");
    }

    public static boolean existsUser(Member member){
        return mongoDB.exists("level", "guildMember", member.getIdLong());
    }

    public static void insertUser(Member member) {
        if (!existsUser(member))
            mongoDB.insert("level", new Document()
                    .append("guildMember", member.getIdLong())
                    .append("level", 0)
                    .append("xp", 0));
    }

    public static int getLevelStep(int level) {
        return mongoDB.find("levelsteps", "level", level).first().getInteger("xp");
    }
}