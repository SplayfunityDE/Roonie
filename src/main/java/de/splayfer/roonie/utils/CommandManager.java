package de.splayfer.roonie.utils;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.enums.Guilds;
import de.splayfer.roonie.utils.enums.Param;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.CommandInteraction;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CommandManager {

    private static HashMap<Long, List<CommandData>> commands = new HashMap<>();

    public static void addCommands(CommandData... data) {
        addCommands(Guilds.GLOBAL, data);
    }

    public static void addCommands(Guilds guild, CommandData... data) {
        if (!commands.containsKey(guild.getId()))
            commands.put(guild.getId(), new ArrayList<>());
        for (CommandData d : data)
            commands.get(guild.getId()).add(d);
    }
    public static void initCommands(JDA jda) {
        List<CommandData> list = new ArrayList<>();
        for (Long id : commands.keySet()) {
            list.addAll(commands.get(id));
            /*
            if (id == -1) {
                jda.updateCommands().addCommands(commands.get(id)).queue();
            } else {
                Guild g = jda.getGuildById(id);
                g.updateCommands().addCommands(commands.get(id)).queue();
            }
             */
            jda.updateCommands().addCommands(list).queue();
        }
    }

    public static boolean checkCommand(CommandInteraction interaction, String name, Param... parameters) {
        if(interaction.getName().equals(name)) {
            for(Param p : parameters) {
                switch(p) {
                    case GUILD_ONLY -> {if(!interaction.isFromGuild()) {
                        interaction.reply("Dieser Command ist nur in Gilden verfügbar!").queue();
                        return false;
                    }}
                    case MAIN_GUILD_ONLY -> {if(!interaction.getGuild().equals(Roonie.mainGuild)) {
                        interaction.reply("Dieser Command kann nur auf dem **Splayfunity Discord-Server** ausgeführt werden!").setEphemeral(true).queue();
                        return false;
                    }}
                }
            }
        }
        return false;
    }
}