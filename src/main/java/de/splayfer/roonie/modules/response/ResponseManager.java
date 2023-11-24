package de.splayfer.roonie.modules.response;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;

public class ResponseManager {

    public record Response (String message, Member creator, String type, String value) {
        public Document getDocument() {
            return new Document()
                    .append("message", message)
                    .append("creator", creator.getIdLong())
                    .append("type", type)
                    .append("value", value);
        }
    }

    static MongoDBDatabase mongoDB = new MongoDBDatabase("splayfunity");

    public static void createResponse(String message, Member creator, String type, String value) {
        mongoDB.insert("response", new Response(message, creator, type, value).getDocument());
    }

    public static void removeResponse(String message) {
        mongoDB.drop("response", "message", message);
    }

    public static Response getResponse(String message) {
        Document doc = mongoDB.find("response", "message", message).first();
        return new Response(doc.getString("message"), Roonie.mainGuild.getMemberById(doc.getLong("creator")), doc.getString("type"), doc.getString("value"));
    }

    public static boolean existsResponse(String message){
        return mongoDB.exists("repsonse", "message", message);
    }
}
