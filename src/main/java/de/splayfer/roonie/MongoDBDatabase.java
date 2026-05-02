package de.splayfer.roonie;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MongoDBDatabase {

    @Value("${mongo.host}")
    private String mongo_host;
    @Value("${mongo.username}")
    private String mongo_username;
    @Value("${mongo.password}")
    private String mongo_password;

    private MongoDatabase mongoDatabase;
    private static MongoClient mongoClient;

    public static MongoDBDatabase getDatabase(String name) {
        return new MongoDBDatabase(mongoClient.getDatabase(name));
    }

    @PostConstruct
    public void connect() {
        try {
            mongoClient = MongoClients.create("mongodb://" + mongo_username + ":" + mongo_password + "@" + mongo_host);
            System.out.println("Connected to MongoDB Server");
        } catch (MongoException exception) {
            System.out.println("MongoDB Connection Error: " + exception.getMessage());
        }
    }

    @PreDestroy
    public void close() {
        mongoClient.close();
    }

    public MongoDBDatabase() {
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