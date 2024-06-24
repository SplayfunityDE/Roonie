package de.splayfer.roonie.modules.music;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.modules.music.commands.*;
import de.splayfer.roonie.modules.poll.PollCreateCommand;
import de.splayfer.roonie.modules.poll.PollEnterListener;
import de.splayfer.roonie.utils.CommandManager;
import de.splayfer.roonie.utils.enums.Guilds;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class MusicManager {

    public static void init() {
        Roonie.builder.addEventListeners(new PlayCommand(), new StopCommand(), new ResumeCommand(), new SkipCommand(), new PauseCommand(), new ShuffelCommand(), new LeaveListener());

        CommandManager.addCommands(Guilds.MAIN,
                Commands.slash("play", "▶ │ Spiele Inhalte von YouTube, Spotify, Soundcloud, Twitch und vielem mehr")
                        .addOption(OptionType.STRING, "inhalt", "� │ Inhalt, welchen du wiedergeben möchtest", true)
                        .addOption(OptionType.BOOLEAN, "warteschlange", "� │ Gib, ob der Inhalt in die Warteschlange hinzugeügt werden soll", false),
                Commands.slash("pause", "⏸ │ Pausiert den aktuell gespielten Inhalt"),
                Commands.slash("resume", "⏯ │ Setzt den aktuellen Inhalt fort"),
                Commands.slash("stop", "⏹ │ Stoppt den aktuell gespielten Inhalt"),
                Commands.slash("skip", "⏯ │ Übersprint den aktuellen Titel der Warteschleife"),
                Commands.slash("shuffel", "� │ Würfelt die aktuellen Titel der Warteschleifen durcheinander"));
    }

}
