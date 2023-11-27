package de.splayfer.roonie.config;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.CommandManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class ConfigManager {

    public static void init() {
        Roonie.builder.addEventListeners(new SetupCommand());

        CommandManager.addCommand(Commands.slash("setup", "\uD83D\uDEE0️ │ Sende verwaltungsrelevante Nachrichten mithilfe dieses Commands")
                .addOption(OptionType.STRING, "kategorie", "\uD83C\uDFF7️ │ Kategorie, über welche du Nachrichten versenden möchtest!", true, true)
                .addOption(OptionType.INTEGER, "id", "⚙️ │ Id, der von dir verwendeten Kategorie (muss eine Ganzzahl sein)", false)
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));
    }
}
