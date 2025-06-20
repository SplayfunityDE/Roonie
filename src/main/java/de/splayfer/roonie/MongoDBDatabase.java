package de.splayfer.roonie;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.HashMap;
import java.util.List;

public class MongoDBDatabase {

    private MongoDatabase mongoDatabase;
    private static MongoClient mongoClient;

    public static void connect() {
        try {
            mongoClient = MongoClients.create("mongodb://" + System.getenv("MONGO_USERNAME") + ":" + System.getenv("MONGO_PASSWORD") + "@" + System.getenv("MONGO_HOST"));
            System.out.println("Connected to MongoDB Server");
        } catch (MongoException exception) {
            System.out.println("MongoDB Connection Error: " + exception.getMessage());
        }
    }

    public void close() {
        //optional - no inpact on performance, conncetion pool managed by MongoClient
        mongoClient.close();
    }

    public static MongoDBDatabase getDatabase(String name) {
        return new MongoDBDatabase(mongoClient.getDatabase(name));
    }

    public MongoDBDatabase(MongoDatabase mongoDatabase) {
        this.mongoDatabase = mongoDatabase;
    }

    public FindIterable<Document> find(String collection, String key, Object value) {
        return mongoDatabase.getCollection(collection).find(new Document(key, value));
    }

    public FindIterable<Document> find(String collection, Document document) {
        return mongoDatabase.getCollection(collection).find(document);
    }

    public FindIterable<Document> findAll(String collection) {
        return mongoDatabase.getCollection(collection).find();
    }

    public boolean exists(String collection, String key, Object value) {
        return mongoDatabase.getCollection(collection).find(new Document(key, value)).first() != null;
    }

    public boolean exists(String collection, Document document) {
        return mongoDatabase.getCollection(collection).find(document).first() != null;
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

    public void updateLine(String collection, Document source, String key, List<Object> newValue) {
        mongoDatabase.getCollection(collection).updateOne(source, Updates.set(key, newValue), new UpdateOptions().upsert(true));
    }

    public void updateLine(String collection, String sourceKey, Object sourceValue, String key, Object newValue){
        mongoDatabase.getCollection(collection).updateOne(new Document(sourceKey, sourceValue), Updates.set(key, newValue), new UpdateOptions().upsert(true));
    }

    public void update(String collection, Document source, Document replace) {
        mongoDatabase.getCollection(collection).replaceOne(source, replace);
    }

    public void drop(String collection, String key, Object value) {
        mongoDatabase.getCollection(collection).deleteOne(new Document(key, value));
    }

    public void drop(String collection, Document document) {
        mongoDatabase.getCollection(collection).deleteOne(document);
    }

    public FindIterable<Document> top(String collection, String key, int amount) {
        return mongoDatabase.getCollection(collection).find().sort(new BasicDBObject(key, -1)).limit(amount);
    }

    public ListCollectionsIterable<Document> getCollections() {
        return mongoDatabase.listCollections();
    }

    public String getCollectionName(Document document) {
        for (String s : mongoDatabase.listCollectionNames())
            if (find(s, document) != null)
                return s;
        return null;
    }

}