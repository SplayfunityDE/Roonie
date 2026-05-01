package de.splayfer.roonie.modules.music;

import de.splayfer.roonie.utils.SlashCommandManager;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.stereotype.Component;

@Component
public class MusicManager implements SlashCommandManager {

    @Override
    public SlashCommandData[] slashCommands() {
        return new SlashCommandData[]{
                Commands.slash("play", "▶ │ Spiele Inhalte von YouTube, Spotify, Soundcloud, Twitch und vielem mehr")
                        .addOption(OptionType.STRING, "inhalt", "� │ Inhalt, welchen du wiedergeben möchtest", true)
                        .addOption(OptionType.BOOLEAN, "warteschlange", "� │ Gib, ob der Inhalt in die Warteschlange hinzugeügt werden soll", false),
                Commands.slash("pause", "⏸ │ Pausiert den aktuell gespielten Inhalt"),
                Commands.slash("resume", "⏯ │ Setzt den aktuellen Inhalt fort"),
                Commands.slash("stop", "⏹ │ Stoppt den aktuell gespielten Inhalt"),
                Commands.slash("skip", "⏯ │ Übersprint den aktuellen Titel der Warteschleife"),
                Commands.slash("shuffel", "� │ Würfelt die aktuellen Titel der Warteschleifen durcheinander")
        };
    }
}
