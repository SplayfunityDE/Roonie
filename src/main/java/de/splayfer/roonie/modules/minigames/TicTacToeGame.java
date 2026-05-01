
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