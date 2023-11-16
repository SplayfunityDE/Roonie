package de.splayfer.roonie.minigames;

import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;

public class MinigamesManager {

    @Getter
    private static MySQLDatabase database = new MySQLDatabase("splayfunity");

    public static void addMatchToMember(Member member, String game) {
        insertStats(member, game);
        getDatabase().update("UPDATE " + game + "Stats SET matches = matches + ? WHERE guildMember = ?", 1, member.getId());

    }

    public static void addWinToMember(Member member, String game) {
        insertStats(member, game);
        getDatabase().update("UPDATE " + game + "Stats SET wins = wins + ? WHERE guildMember = ?", 1, member.getId());

    }

    public static int getMatches(Member member, String game) {
        insertStats(member, game);
        return getDatabase().select(Integer.class, "SELECT matches FROM " + game + "Stats WHERE guildMember = ?", "matches", member.getId());

    }

    public static int getWins(Member member, String game) {
        insertStats(member, game);
        return getDatabase().select(Integer.class, "SELECT wins FROM " + game + "Stats WHERE guildMember = ?", "wins", member.getId());

    }

    public static boolean existsStats(Member member, String game) {

        return getDatabase().existsEntry(game + "Stats", "guildMember = ?", member.getId());
    }

    public static void insertStats(Member member, String game) {

        if (!existsStats(member, game))
            getDatabase().insert(game + "Stats", new String[]{"guildMember"}, member.getId());


    }

}
