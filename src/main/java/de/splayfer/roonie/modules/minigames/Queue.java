package de.splayfer.roonie.modules.minigames;

import de.splayfer.roonie.MongoDBDatabase;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Queue {

    private final TicTacToeGameManager ticTacToeGameManager;

    MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("minigames");

    public boolean checkForGame() {
            for (Document doc : mongoDB.findAll("tictactoe"))
                if (doc.getString("type").equals("queue"))
                    if (doc.getString("status").equals("waiting"))
                        return true;
        return false;
    }

    public TicTacToeGame getQueueGame() {
        for (Document doc : mongoDB.findAll("tictactoe"))
            if (doc.getString("status").equals("waiting"))
                return ticTacToeGameManager.getFromDocument(doc);
        return null;
    }
}
