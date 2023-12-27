package de.splayfer.roonie.modules.poll;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.CommandManager;
import de.splayfer.roonie.utils.enums.Guilds;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

public class PollManager {

    public static void init() {
        Roonie.builder.addEventListeners(new PollCreateCommand(), new PollEnterListener());

        CommandManager.addCommands(Guilds.MAIN,
                Commands.slash("umfrage", "\uD83D\uDCCA │ Verwaltet die gesamten Umfragen des Servers!")
                        .addSubcommands(new SubcommandData("create", "➕ │ Erstelle eine neue Umfrage!"))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));
    }
}
