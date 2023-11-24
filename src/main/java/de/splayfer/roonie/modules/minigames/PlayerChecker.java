package de.splayfer.roonie.modules.minigames;

import de.splayfer.roonie.FileSystem;
import net.dv8tion.jda.api.entities.Member;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

public class PlayerChecker {

    protected static YamlConfiguration yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

    public static boolean checkPlayer(Member player) {

        boolean check = true;

        //if player is game owner

        String playerID = player.getId();

        try {

            for (String s : yml.getKeys(false)) {

                if (yml.getString(s + ".player1").equals(playerID)) {

                    check = false;

                } else {

                    if (yml.contains(s + ".player2")) {

                        if (yml.getString(s + ".player2").equals(playerID) && yml.getString(s + ".status").equals("playing") && yml.getString(s + ".type").equals("request")) {

                            check = false;
                        }

                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return check;

    }

}
