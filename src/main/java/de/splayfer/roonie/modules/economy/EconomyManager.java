package de.splayfer.roonie.modules.economy;

import de.splayfer.roonie.MongoDBDatabase;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;

import java.util.HashMap;
import java.util.Map;

public class EconomyManager {

    static MongoDBDatabase mongoDB = new MongoDBDatabase("splayfunity");

    public static void addMoneyToUser(Member member, int amount) {
        insertUser(member);
        mongoDB.updateLine("money", new Document("guildMember", member.getIdLong()), "amount", getMoney(member) + amount);
    }

    public static void removeMoneyFromUser(Member member, int amount) {
        insertUser(member);
        if (amount > getMoney(member))
            amount = getMoney(member);
        mongoDB.updateLine("money", new Document("guildMember", member.getIdLong()), "amount", getMoney(member) - amount);

    }

    public static int getMoney(Member member) {
        if (existsUser(member))
            return mongoDB.find("money", "guildMember", member.getIdLong()).first().getInteger("amount");
        else
            return 0;
    }

    public static void setMoney(Member member, int amount) {
        insertUser(member);
        mongoDB.updateLine("money", new Document("guildMember", member.getIdLong()), "amount", amount);
    }

    public static Map<Integer, String> top(int top) {
        HashMap<Integer, String> list = new HashMap<>();
        mongoDB.top("money", "amount", 3).forEach(document -> list.put(document.getInteger("amount"), String.valueOf(document.getLong("guildMember"))));
        return list;
    }

    public static boolean existsUser(Member member){
        return mongoDB.exists("money", "guildMember", member.getIdLong());
    }

    public static void insertUser(Member member) {
        if(!existsUser(member))
            mongoDB.insert("money", new Document()
                    .append("guildMember", member.getIdLong())
                    .append("amount", 0));
    }

}
