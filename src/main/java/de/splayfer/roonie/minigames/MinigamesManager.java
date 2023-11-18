package de.splayfer.roonie.minigames;

import de.splayfer.roonie.MongoDBDatabase;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;

public class MinigamesManager {

    static MongoDBDatabase mongoDB = new MongoDBDatabase("minigames");

    public static void addMatchToMember(Member member, String game) {
        insertStats(member, game);
        mongoDB.updateLine(game, "guildMember", member.getIdLong(), "matches", getMatches(member, game) + 1);
    }

    public static void addWinToMember(Member member, String game) {
        insertStats(member, game);
        mongoDB.updateLine(game, "guildMember", member.getIdLong(), "wins", getWins(member, game) + 1);
    }

    public static int getMatches(Member member, String game) {
        if (existsStats(member, game))
            return mongoDB.find(game, "guildMember", member.getIdLong()).first().getInteger("matches");
        else
            return 0;
    }

    public static int getWins(Member member, String game) {
        if (existsStats(member, game))
            return mongoDB.find(game, "guildMember", member.getIdLong()).first().getInteger("wins");
        else
            return 0;
    }

    public static boolean existsStats(Member member, String game) {
        return mongoDB.exists(game, "guildMember", member.getIdLong());
    }

    public static void insertStats(Member member, String game) {
        if (!existsStats(member, game))
            mongoDB.insert(game, new Document()
                    .append("guildMember", member.getIdLong())
                    .append("matches", 0)
                    .append("wins", 0));
    }
}
