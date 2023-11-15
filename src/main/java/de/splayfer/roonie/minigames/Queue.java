package de.splayfer.roonie.minigames;

import de.splayfer.roonie.FileSystem;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

public class Queue {

    protected static YamlConfiguration yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

    public static boolean queue = false;

    public static boolean checkForGame() {

        yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

        boolean check = false;

        if (!yml.getKeys(false).isEmpty()) {

            for (String s : yml.getKeys(false)) {

                if (yml.get(s + ".type").equals("queue")) {

                    if (yml.get(s + ".status").equals("waiting")) {

                        check = true;

                    }
                }

            }

        }

        return check;

    }

    public static String getQueueGame() {

        yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

        String gameID = "";

        if (!yml.getKeys(false).isEmpty()) {

            for (String s : yml.getKeys(false)) {

                if (yml.get(s + ".type").equals("queue")) {

                    if (yml.get(s + ".status").equals("waiting")) {

                        gameID = s;

                    }
                }

            }

        }

        return gameID;

    }
}
