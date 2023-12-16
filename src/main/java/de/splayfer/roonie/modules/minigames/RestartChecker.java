package de.splayfer.roonie.modules.minigames;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import org.bson.Document;

public class RestartChecker {

    public static void checkForRemainingGames(MongoDBDatabase mongoDB) {

        for (Document doc : mongoDB.getCollections()) {
            String collection;
            //case: channel doesnt exist anymore
            if (Roonie.mainGuild.getThreadChannelById(doc.getLong("channel")) == null)
                mongoDB.drop(mongoDB.getCollectionName(doc), doc);
            //case: player left the server
            if (Roonie.mainGuild.getMemberById(doc.getLong("player1")) == null || Roonie.mainGuild.getMemberById(doc.getLong("player2")) == null) {
                mongoDB.drop(mongoDB.getCollectionName(doc), doc);
            }
        }
    }

}
