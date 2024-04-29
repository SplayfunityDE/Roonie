package de.splayfer.roonie.modules.response;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.CommandManager;
import de.splayfer.roonie.utils.enums.Guilds;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class ResponseManager {

    public static void init() {
        Roonie.builder.addEventListeners(new ResponseAddCommand(), new ResponseRemoveCommand(), new ResponseListener());

        CommandManager.addCommands(Guilds.MAIN, Commands.slash("response", "\uD83D\uDCDC   Verwalte automatisierte Reaktionen des Bots!")
                .addSubcommands(new SubcommandData("add", "\uD83D\uDCDC │ Füge automatisierte Reaktionen hinzu!")
                                .addOption(OptionType.STRING, "nachricht", "\uD83D\uDCE2 │ Nachricht, auf die der Bot reagieren soll", true),
                        new SubcommandData("remove", "\uD83D\uDCDC │ Entferne automatisierte Reaktionen!")
                                .addOption(OptionType.STRING, "nachricht", "\uD83D\uDCE2 │ Nachricht, auf die der Bot reagieren soll", true))
                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));
    }
}