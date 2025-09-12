package de.splayfer.roonie.modules.minigames;

import de.splayfer.roonie.MongoDBDatabase;
import org.bson.Document;

public class Queue {

    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("minigames");

    public static boolean checkForGame() {
            for (Document doc : mongoDB.findAll("tictactoe"))
                if (doc.getString("type").equals("queue"))
                    if (doc.getString("status").equals("waiting"))
                        return true;
        return false;
    }

    public static TicTacToeGame getQueueGame() {
        for (Document doc : mongoDB.findAll("tictactoe"))
            if (doc.getString("status").equals("waiting"))
                return TicTacToeGame.getFromDocument(doc);
        return null;
    }
}
