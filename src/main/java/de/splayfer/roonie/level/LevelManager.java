package de.splayfer.roonie.level;

import de.splayfer.roonie.MySQLDatabase;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class LevelManager extends ListenerAdapter {

    @Getter
    private static MySQLDatabase database = new MySQLDatabase("splayfunity");

    public static void addXpToUser(Member member, int amount) {
        insertUser(member);
        getDatabase().update("UPDATE LevelStats SET xp = xp + ? WHERE guildMember = ?", amount, member.getId());
    }

    public static void removeXpFromUser(Member member, int amount) {
        insertUser(member);
        getDatabase().update("UPDATE LevelStats SET xp = xp - ? WHERE guildMember = ?", amount, member.getId());
    }

    public static void setXp(Member member, int amount) {
        insertUser(member);
        getDatabase().update("UPDATE LevelStats SET xp = ? WHERE guildMember = ?", amount, member.getId());
    }

    public static void addLevelToUser(Member member, int amount) {
        insertUser(member);
        getDatabase().update("UPDATE LevelStats SET level = level + ? WHERE guildMember = ?", amount, member.getId());
    }

    public static void removeLevelFromUser(Member member, int amount) {
        insertUser(member);
        getDatabase().update("UPDATE LevelStats SET level = level - ? WHERE guildMember = ?", amount, member.getId());
    }

    public static void setLevel(Member member, int amount) {
        insertUser(member);
        getDatabase().update("UPDATE LevelStats SET level = ? WHERE guildMember = ?", amount, member.getId());
    }

    public static int getLevel(Member member) {
        insertUser(member);
        return getDatabase().select(Integer.class, "SELECT level FROM LevelStats WHERE guildMember = ?", "level", member.getId());
    }

    public static int getXp(Member member) {
        insertUser(member);
        return getDatabase().select(Integer.class, "SELECT xp FROM LevelStats WHERE guildMember = ?", "xp", member.getId());

    }

    public static boolean existsUser(Member member) {
        //insertUser(member);
        return getDatabase().existsEntry("LevelStats", "guildMember = ?", member.getId());
    }

    public static void insertUser(Member member) {

        if (!existsUser(member))
            getDatabase().insert("LevelStats", new String[]{"guildMember"}, member.getId());


    }

    public static int getLevelStep(int level) {

        return getDatabase().select(Integer.class, "SELECT xp FROM LevelSteps WHERE level = ?", "xp", level);
    }

}
