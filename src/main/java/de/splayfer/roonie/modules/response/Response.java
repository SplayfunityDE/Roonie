package de.splayfer.roonie.modules.response;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bson.Document;

public class Response {

    @Getter
    @Setter
    private String message;
    @Getter
    private User creator;
    @Getter
    @Setter
    private String type;
    @Getter
    @Setter
    private String value;

    public Response(String message, User creator, String type, String value) {
        this.message = message;
        this.creator = creator;
        this.type = type;
        this.value = value;
    }

    public Document getDocument() {
        return new Document()
                .append("message", message)
                .append("creator", creator.getIdLong())
                .append("type", type)
                .append("value", value);
    }

    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    public static void create(String message, User creator, String type, String value) {
        mongoDB.insert("response", new Response(message, creator, type, value).getDocument());
    }

    public static void removeResponse(String message) {
        mongoDB.drop("response", "message", message);
    }

    public static Response getResponse(String message) {
        Document doc = mongoDB.find("response", "message", message).first();
        return new Response(doc.getString("message"), Roonie.shardMan.getUserById(doc.getLong("creator")), doc.getString("type"), doc.getString("value"));
    }

    public static boolean existsResponse(String message){
        return mongoDB.exists("response", "message", message);
    }

    public static boolean existsCreator(long member) {
        return mongoDB.exists("response", "creator", member);
    }
}
