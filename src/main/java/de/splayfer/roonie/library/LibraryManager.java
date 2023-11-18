package de.splayfer.roonie.library;

import de.splayfer.roonie.MongoDBDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class LibraryManager {

    static MongoDBDatabase mongoDB = new MongoDBDatabase("splayfunity");

    public static void addTemplate(String category, String url) {
        mongoDB.insert("templates", new Document()
                .append("category", category)
                .append("url", url));
    }

    public static void removeTemplate(String url) {
        mongoDB.drop("templates", "url", url);

    }

    public static List<String> getTemplatesByCategory(String category) {
        List<String> list = new ArrayList<>();
        mongoDB.find("templates", "category", category).forEach(document -> list.add(document.getString("url")));
        return list;
    }

    public static boolean existsTemplate(String message) {
        return mongoDB.exists("templates", "url", message);
    }


    public static void addBanner(String category, String url) {
        mongoDB.insert("banner", new Document()
                .append("category", category)
                .append("url", url));
    }

    public static void removeBanner(String url) {
        mongoDB.drop("banner", "url", url);
    }

    public static List<String> getBannerByCategory(String category) {
        List<String> list = new ArrayList<>();
        mongoDB.find("banner", "category", category).forEach(document -> list.add(document.getString("url")));
        return list;
    }

    public static boolean existsBanner(String message) {
        return mongoDB.exists("banner", "url", message);
    }

    public static boolean existsBannerCategory (String category) {
        return mongoDB.exists("banner", "category", category);
    }

}
