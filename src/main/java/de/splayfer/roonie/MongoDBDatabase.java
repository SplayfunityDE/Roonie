package de.splayfer.roonie;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.HashMap;
import java.util.List;

public class MongoDBDatabase {

    MongoClient mongoClient;
    MongoDatabase mongoDatabase;

    public MongoDBDatabase(String database) {
        connect(database);
    }

    private void connect(String database) {
        try {
            mongoClient = MongoClients.create("mongodb://useradmin:NFC_King10@116.203.79.193");
            mongoDatabase = mongoClient.getDatabase(database);
        } catch (MongoException exception) {
            System.out.println("MongoDB Connection Error: " + exception.getMessage());
        }
    }

     public void close() {
        //optional - no inpact on performance, conncetion pool managed by MongoClient
        mongoClient.close();
    }

    public FindIterable<Document> find(String collection, String key, Object value) {
        return mongoDatabase.getCollection(collection).find(Filters.eq(key, value));
    }

    public FindIterable<Document> find(String collection, Document document) {
        return mongoDatabase.getCollection(collection).find(Filters.eq(document));
    }

    public FindIterable<Document> findAll(String collection) {
        return mongoDatabase.getCollection(collection).find();
    }

    public boolean exists(String collection, String key, Object value) {
        return mongoDatabase.getCollection(collection).find(Filters.eq(key, value)).first() != null;
    }

    public boolean exists(String collection, Document document) {
        return mongoDatabase.getCollection(collection).find(Filters.eq(document)).first() != null;
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

    public void updateLine(String collection, String sourceKey, Object sourceValue, String key, Object newValue){
        mongoDatabase.getCollection(collection).updateOne(new Document(sourceKey, sourceValue), Updates.set(key, newValue), new UpdateOptions().upsert(true));
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

    public FindIterable<Document> top(String collection, String key, int amount) {
        return mongoDatabase.getCollection(collection).find().sort(new BasicDBObject(key, -1)).limit(3);
    }

}
