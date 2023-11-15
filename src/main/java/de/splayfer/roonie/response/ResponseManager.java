package de.splayfer.roonie.response;

import de.splayfer.roonie.MySQLDatabase;
import lombok.Getter;
import net.dv8tion.jda.api.entities.Member;

public class ResponseManager {

    public record Response (String message, Member creator, String type, String value) {}

    @Getter
    private static MySQLDatabase database = new MySQLDatabase("splayfunity");


    public static void createResponse(String message, Member creator, String type, String value) {

        getDatabase().insert("ResponseLog", new String[]{"message", "creator", "type", "value"}, message, creator.getId(), type, value);
    }

    public static void removeResponse(String message) {

        getDatabase().update("DELETE FROM ResponseLog WHERE message = ?", message);

    }

    public static Response getResponse(String message) {

        return getDatabase().selectResponse("SELECT message, creator, type, value FROM ResponseLog WHERE message = ?", message);
    }

    public static boolean existsResponse(String message){

        return getDatabase().existsEntry("ResponseLog", "message = ?", message);
    }


}
