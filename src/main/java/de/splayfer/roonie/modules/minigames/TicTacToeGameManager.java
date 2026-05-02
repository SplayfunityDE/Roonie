package de.splayfer.roonie.modules.minigames;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.bson.Document;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class TicTacToeGameManager {

    private final Roonie roonie;
    private final MongoDBDatabase mongoDB;

    public TicTacToeGameManager(@Lazy Roonie roonie) {
        this.roonie = roonie;
        this.mongoDB = MongoDBDatabase.getDatabase("minigames");
    }

    public TicTacToeGame getFromDocument(Document document) {
        HashMap<Integer, String> map = new HashMap<>();
        document.getList("fields", String.class).forEach(field -> map.put(Integer.parseInt(field.split(":")[0]), field.split(":")[1]));
        return new TicTacToeGame(roonie.getMainGuild().getThreadChannelById(document.getLong("channel")), document.getString("status"), document.getString("type"), roonie.getMainGuild().getMemberById(document.getLong("player1")), roonie.getMainGuild().getMemberById(document.getLong("player2")), document.getInteger("turn"), map);
    }

    public TicTacToeGame getFromMongoDB(ThreadChannel channel) {
        return getFromDocument(mongoDB.find("tictactoe", "channel", channel.getIdLong()).first());
    }

    public void insertToMongoDB(TicTacToeGame ticTacToeGame) {
        Document document = new Document()
                .append("channel", ticTacToeGame.getChannel().getIdLong())
                .append("status", ticTacToeGame.getStatus())
                .append("type", ticTacToeGame.getType())
                .append("player1", ticTacToeGame.getPlayer1().getIdLong())
                .append("player2", ticTacToeGame.getPlayer2().getIdLong())
                .append("turn", ticTacToeGame.getTurn());
        List<String> list = new ArrayList<>();
        ticTacToeGame.getFields().forEach((num, symbol) -> list.add(num + ":" + symbol));
        document.append("fields", list);
        if (mongoDB.exists("tictactoe", "channel", ticTacToeGame.getChannel().getIdLong()))
            mongoDB.update(
                    "tictactoe", mongoDB.find("tictactoe", "channel", ticTacToeGame.getChannel().getIdLong()).first(), document);
        else
            mongoDB.insert("tictactoe", document);
    }

    public void removeFromMongoDB(TicTacToeGame ticTacToeGame) {
        mongoDB.drop("tictactoe", new Document("channel", ticTacToeGame.getChannel().getIdLong()));
    }

    public boolean checkPlayer(Member member) {
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

    public boolean isGameChannel(MessageChannelUnion channel) {
        if (channel.getType().equals(ChannelType.GUILD_PRIVATE_THREAD))
            return mongoDB.exists("tictactoe", "channel", channel.getIdLong());
        return false;
    }

    public TicTacToeGame getFromMember(Member member) {
        for (Document doc : mongoDB.findAll("tictactoe"))
            if (doc.getLong("player1").equals(member.getIdLong()) || doc.getLong("player2").equals(member.getIdLong()))
                return getFromDocument(doc);
        return null;
    }
}
