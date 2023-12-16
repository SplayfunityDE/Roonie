package de.splayfer.roonie.modules.moderation;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.CommandManager;
import de.splayfer.roonie.utils.enums.Guilds;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class ModerationManager {

    public static void init() {
        Roonie.builder.addEventListeners(new ViewContextMenu());

        CommandManager.addCommands(Guilds.MAIN,
                Commands.context(Command.Type.USER, "Moderation anzeigen"),
                Commands.context(Command.Type.MESSAGE, "Moderation anzeigen"));
    }
}
