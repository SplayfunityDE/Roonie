package de.splayfer.roonie.economy;

import de.splayfer.roonie.MySQLDatabase;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;

import java.util.Map;

public class EconomyManager {

    @Getter
    private static MySQLDatabase database = new MySQLDatabase("splayfunity");


    public static void addMoneyToUser(Member member, int amount) {
        insertUser(member);
        getDatabase().update("UPDATE MoneyStats SET amount = amount + ? WHERE guildMember = ?", amount, member.getId());
    }

    public static void removeMoneyFromUser(Member member, int amount) {
        insertUser(member);
        getDatabase().update("UPDATE MoneyStats SET amount = amount - ? WHERE guildMember = ?", amount, member.getId());

    }

    public static int getMoney(Member member) {
        insertUser(member);
        return getDatabase().select(Integer.class, "SELECT amount FROM MoneyStats WHERE guildMember = ?", "amount", member.getId());
    }

    public static Map<Integer, String> top(int top) {

        //SELECT * FROM Kontostand ORDER BY amount DESC LIMIT 3

        return getDatabase().top("SELECT * FROM MoneyStats ORDER BY amount DESC LIMIT " + top, "amount");
    }

    public static boolean existsUser(Member member){
        //insertUser(member);
        return getDatabase().existsEntry("MoneyStats", "guildMember = ?", member.getId());
    }

    public static void insertUser(Member member) {

        if(!existsUser(member))
            getDatabase().insert("MoneyStats", new String[]{"guildMember"}, member.getId());

    }

}
