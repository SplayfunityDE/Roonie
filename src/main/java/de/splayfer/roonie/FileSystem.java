package de.splayfer.roonie;

import de.splayfer.roonie.general.AutoComplete;

import java.io.File;
import java.io.IOException;

public class FileSystem {

    //folders
    public static File dataFolder;
    public static File cacheFolder;

    //files
    public static File GameLog;

    public static void loadFileSystem() throws IOException {

        //initializing objects
        dataFolder = new File(System.getProperty("user.dir") + File.separator + "RoonieData");
        GameLog = new File(dataFolder.getAbsolutePath() + File.separator + "GameLog.yml");

        //checking folders
        if (!dataFolder.exists()) {
            dataFolder.mkdir();
        }
        if (!GameLog.exists()) {
            GameLog.createNewFile();
        }
    }
}
