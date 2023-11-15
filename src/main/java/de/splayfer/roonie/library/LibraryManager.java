package de.splayfer.roonie.library;

import de.splayfer.roonie.MySQLDatabase;
import lombok.Getter;

import java.util.List;

public class LibraryManager {

    @Getter
    private static MySQLDatabase database = new MySQLDatabase("splayfunity");


    public static void addTemplate(String category, String url) {

        getDatabase().insert("Templates", new String[]{"category", "url"}, category, url);
    }

    public static void removeTemplate(String url) {

        getDatabase().update("DELETE FROM LibraryTemplates WHERE url = ?", url);

    }

    public static List<String> getTemplatesByCategory(String category) {

        return getDatabase().selectTemplates( "SELECT url FROM LibraryTemplates WHERE category = ?", category);
    }

    public static boolean existsTemplate(String message) {

        return getDatabase().existsEntry("LibraryTemplates", "url = ?", message);
    }


    public static void addBanner(String category, String url) {

        getDatabase().insert("LibraryBanner", new String[]{"category", "url"}, category, url);
    }

    public static void removeBanner(String url) {

        getDatabase().update("DELETE FROM LibraryBanner WHERE url = ?", url);

    }

    public static List<String> getBannerByCategory(String category) {

        return getDatabase().selectTemplates( "SELECT url FROM LibraryBanner WHERE category = ?", category);
    }

    public static boolean existsBanner(String message) {

        return getDatabase().existsEntry("LibraryBanner", "url = ?", message);
    }

    public static boolean existsBannerCategory (String category) {

        return getDatabase().existsEntry("LibraryBanner", "category = ?", category);
    }

}
