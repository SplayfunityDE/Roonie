package de.splayfer.roonie;

import de.splayfer.roonie.general.AutoComplete;

import java.io.File;
import java.io.IOException;

public class FileSystem {

    //folders

    public static File dataFolder;
    public static File cacheFolder;

    //files

    public static File GiveawayStats;
    public static File UmfrageButtonLog;
    public static File UmfrageLog;
    public static File NitroGamesLog;
    public static File EmbedStats;
    public static File GameLog;
    public static File StatsLog;
    public static File AutoComplete;

    public static void loadFileSystem() throws IOException {

        //initializing objects

        dataFolder = new File(System.getProperty("user.dir") + File.separator + "RoonieData");
        GiveawayStats = new File(dataFolder.getAbsolutePath() + File.separator + "GiveawayStats.yml");
        UmfrageButtonLog = new File(dataFolder.getAbsolutePath() + File.separator + "UmfrageButtonLog.yml");
        UmfrageLog = new File(dataFolder.getAbsolutePath() + File.separator + "UmfrageLog.yml");
        cacheFolder = new File(dataFolder.getAbsolutePath() + File.separator + "cache");
        NitroGamesLog = new File(dataFolder.getAbsolutePath() + File.separator + "NitroGames.yml");
        EmbedStats = new File(dataFolder.getAbsolutePath() + File.separator + "EmbedStats.yml");
        GameLog = new File(dataFolder.getAbsolutePath() + File.separator + "GameLog.yml");
        StatsLog = new File(dataFolder.getAbsolutePath() + File.separator + "StatsLog.yml");
        AutoComplete = new File(dataFolder.getAbsolutePath() + File.separator + "AutoComplete.yml");

        //checking folders

        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        if (!GiveawayStats.exists()) {
            GiveawayStats.createNewFile();
        }
        if (!UmfrageButtonLog.exists()) {
            UmfrageButtonLog.createNewFile();
        }
        if (!UmfrageLog.exists()) {
            UmfrageLog.createNewFile();
        }
        if (!cacheFolder.exists()) {
            cacheFolder.mkdir();
        }
        if (!NitroGamesLog.exists()) {
            NitroGamesLog.createNewFile();
        }
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        if (!EmbedStats.exists()) {
            EmbedStats.createNewFile();
        }
        if (!GameLog.exists()) {
            GameLog.createNewFile();
        }
        if (!StatsLog.exists()) {
            StatsLog.createNewFile();
        }
        if (!AutoComplete.exists()) {
            AutoComplete.createNewFile();
        }


    }

}
