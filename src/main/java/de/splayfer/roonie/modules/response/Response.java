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
}
