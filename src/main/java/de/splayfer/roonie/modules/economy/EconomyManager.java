package de.splayfer.roonie.modules.economy;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.CommandManager;
import de.splayfer.roonie.utils.enums.Guilds;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import org.bson.Document;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class EconomyManager {

    static MongoDBDatabase mongoDB = new MongoDBDatabase("splayfunity");

    public static void init() {
        Roonie.builder.addEventListeners(new CoinBomb(), new MoneyCommand(), new DailyCommand());

        CommandManager.addCommand(Guilds.MAIN,
                Commands.slash("money", "\uD83D\uDCB3 │ Zeigt dir den aktuellen Kontostand an!")
                        .addOption(OptionType.USER, "nutzer", "\uD83D\uDC65 │ Nutzer, welchen du anzeigen möchtest!", false));
        CommandManager.addCommand(Guilds.MAIN,
                Commands.slash("leaderboard", "\uD83D\uDCCA │ Zeigt dir die Rangliste mit den aktuell besten Casino Spielern an!"));
        CommandManager.addCommand(Guilds.MAIN,
                Commands.slash("daily", "\uD83D\uDCC5 │ Hole dir deine tägliche Menge an Coins!"));
    }

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

    public static Map<Integer, Long> top(int top) {
        LinkedHashMap<Integer, Long> list = new LinkedHashMap<>();
        mongoDB.top("money", "amount", top).forEach(document -> list.put(document.getInteger("amount"), document.getLong("guildMember")));
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
