
package de.splayfer.roonie.modules.minigames;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.bson.Document;

import java.util.*;

public class TicTacToeGame {

    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("minigames");

    @Getter
    @Setter
    private ThreadChannel channel;
    @Getter
    @Setter
    private String status;
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private Member player1;
    @Getter
    @Setter
    private Member player2;
    @Getter
    @Setter
    private int turn;
    @Getter
    @Setter
    private HashMap<Integer, String> fields;


    public TicTacToeGame(ThreadChannel channel, String status, String type, Member player1, Member player2, int turn, HashMap<Integer, String> fields) {
        this.channel = channel;
        this.status = status;
        this.type = type;
        this.player1 = player1;
        this.player2 = player2;
        this.turn = turn;
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicTacToeGame that = (TicTacToeGame) o;
        return turn == that.turn && Objects.equals(channel, that.channel) && Objects.equals(status, that.status) && Objects.equals(type, that.type) && Objects.equals(player1, that.player1) && Objects.equals(player2, that.player2) && Objects.equals(fields, that.fields);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel, status, type, player1, player2, turn, fields);
    }

    public static TicTacToeGame create(ThreadChannel channel, String type, Member player1) {
        return new TicTacToeGame(channel, "waiting", type, player1, null, 1, null);
    }

    public static TicTacToeGame getFromDocument(Document document) {
        HashMap<Integer, String> map = new HashMap<>();
        document.getList("fields", String.class).forEach(field -> map.put(Integer.parseInt(field.split(":")[0]), field.split(":")[1]));
        return new TicTacToeGame(Roonie.mainGuild.getThreadChannelById(document.getLong("channel")), document.getString("status"), document.getString("type"), Roonie.mainGuild.getMemberById(document.getLong("player1")), Roonie.mainGuild.getMemberById(document.getLong("player2")), document.getInteger("turn"), map);
    }

    public static TicTacToeGame getFromMongoDB(ThreadChannel channel) {
        return getFromDocument(mongoDB.find("tictactoe", "channel", channel.getIdLong()).first());
    }

    public void insertToMongoDB() {
        Document document = new Document()
                .append("channel", channel.getIdLong())
                .append("status", status)
                .append("type", type)
                .append("player1", player1.getIdLong())
                .append("player2", player2.getIdLong())
                .append("turn", turn);
        List<String> list = new ArrayList<>();
        fields.forEach((num, symbol) -> list.add(num + ":" + symbol));
        document.append("fields", list);
        if (mongoDB.exists("tictactoe", "channel", channel.getIdLong()))
            mongoDB.update(
                    "tictactoe", mongoDB.find("tictactoe", "channel", channel.getIdLong()).first(), document);
        else
            mongoDB.insert("tictactoe", document);
    }

    public void removeFromMongoDB() {
        mongoDB.drop("tictactoe", new Document("channel", channel.getIdLong()));
    }

    public static boolean isGameChannel(MessageChannelUnion channel) {
        if (channel.getType().equals(ChannelType.GUILD_PRIVATE_THREAD))
            return mongoDB.exists("tictactoe", "channel", channel.getIdLong());
        return false;
    }

    public static boolean checkPlayer(Member member) {
        boolean check = true;
        //if player is game owner
        long playerID = member.getIdLong();
        for (Document doc : mongoDB.findAll("tictactoe")) {
            if (doc.getLong("player1").equals(playerID))
                check = false;
            else
            if (doc.containsKey("player2"))
                if (doc.getLong("player2").equals(playerID) && doc.getString("status").equals("playing") && doc.getString("type").equals("request"))
                    check = false;
        }
        return check;
    }

    public static TicTacToeGame getFromMember(Member member) {
        for (Document doc : mongoDB.findAll("tictactoe"))
            if (doc.getLong("player1").equals(member.getIdLong()) || doc.getLong("player2").equals(member.getIdLong()))
                return TicTacToeGame.getFromDocument(doc);
        return null;
    }

    public Member getMemberTurn() {
        return switch (turn) {
            case 1 -> player1;
            case 2 -> player2;
            default -> null;
        };
    }

    public void switchTurn() {
        if (turn == 1)
            turn = 2;
        else
            turn = 1;
    }
}