package de.splayfer.roonie.modules.minigames;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.context.annotation.Lazy;

public class RestartChecker {

    private final Roonie roonie;

    public RestartChecker(@Lazy Roonie roonie) {
        this.roonie = roonie;
    }

    public void checkForRemainingGames(MongoDBDatabase mongoDB) {

        for (Document doc : mongoDB.getCollections()) {
            String collection;
            //case: channel doesnt exist anymore
            if (roonie.getMainGuild().getThreadChannelById(doc.getLong("channel")) == null)
                mongoDB.drop(mongoDB.getCollectionName(doc), doc);
            //case: player left the server
            if (roonie.getMainGuild().getMemberById(doc.getLong("player1")) == null || roonie.getMainGuild().getMemberById(doc.getLong("player2")) == null) {
                mongoDB.drop(mongoDB.getCollectionName(doc), doc);
            }
        }
    }
}