package de.splayfer.roonie;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MongoDBDatabase {

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    public MongoDBDatabase() {
        connect();
    }

    private void connect() {
        try {
            mongoClient = MongoClients.create("mongodb://useradmin:NFC_King10@116.203.79.193");
            mongoDatabase = mongoClient.getDatabase("splayfunity");
        } catch (MongoException exception) {
            System.out.println("MongoDB Connection Error: " + exception.getMessage());
        }
    }

     public void close() {
        //optional - no inpact on performance, conncetion pool managed by MongoClient
        mongoClient.close();
    }

    public FindIterable<Document> find(String collection, String key, Object value) {
        return mongoDatabase.getCollection(collection).find(new Document(key, value));
    }

    public FindIterable<Document> find(String collection, Document document) {
        return mongoDatabase.getCollection(collection).find(document);
    }

    public boolean exists(String collection, String key, Object value) {
        if (mongoDatabase.getCollection(collection).find(new Document(key, value)).first() != null)
            return true;
        else
            return false;
    }

    public boolean exists(String collection, Document document) {
        if (mongoDatabase.getCollection(collection).find(document).first() != null)
            return true;
        else
            return false;
    }

    public void insert(String collection, Document document) {
        mongoDatabase.getCollection(collection).insertOne(document);
    }

    public void insertMultiple(String collection, List<Document> documents) {
        mongoDatabase.getCollection(collection).insertMany(documents);
    }

    public void updateLine(String collection, Document source, String key, Object newValue) {
        mongoDatabase.getCollection(collection).updateOne(source, Updates.set(key, newValue), new UpdateOptions().upsert(true));
    }

    public void update (String collection, Document source, Document replace) {
        mongoDatabase.getCollection(collection).updateOne(source, replace, new UpdateOptions().upsert(true));
    }

    public void drop(String collection, String key, Object value) {
        mongoDatabase.getCollection(collection).deleteOne(new Document(key, value));
    }

    public void drop(String collection, Document document) {
        mongoDatabase.getCollection(collection).deleteOne(document);
    }

}
