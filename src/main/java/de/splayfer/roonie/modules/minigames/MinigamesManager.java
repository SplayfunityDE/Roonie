package de.splayfer.roonie.modules.minigames;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.bson.Document;
import org.springframework.stereotype.Component;

@Component
public class MinigamesManager {

    MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("minigames");

    public void addMatchToMember(Member member, String game) {
        insertStats(member, game);
        mongoDB.updateLine(game, "guildMember", member.getIdLong(), "matches", getMatches(member, game) + 1);
    }

    public void addWinToMember(Member member, String game) {
        insertStats(member, game);
        mongoDB.updateLine(game, "guildMember", member.getIdLong(), "wins", getWins(member, game) + 1);
    }

    public int getMatches(Member member, String game) {
        if (existsStats(member, game))
            return mongoDB.find(game, "guildMember", member.getIdLong()).first().getInteger("matches");
        else
            return 0;
    }

    public int getWins(Member member, String game) {
        if (existsStats(member, game))
            return mongoDB.find(game, "guildMember", member.getIdLong()).first().getInteger("wins");
        else
            return 0;
    }

    public boolean existsStats(Member member, String game) {
        return mongoDB.exists(game, "guildMember", member.getIdLong());
    }


    public void insertStats(Member member, String game) {
        if (!existsStats(member, game))
            mongoDB.insert(game, new Document()
                    .append("guildMember", member.getIdLong())
                    .append("matches", 0)
                    .append("wins", 0));
    }
}
