package de.splayfer.roonie.modules.level;

import de.splayfer.roonie.MongoDBDatabase;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bson.Document;

public class LevelManager extends ListenerAdapter {

    static MongoDBDatabase mongoDB = new MongoDBDatabase("splayfunity");
    public static void addXpToUser(Member member, int amount) {
        insertUser(member);
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
