package de.splayfer.roonie.utils;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class CommandManager {

    public static boolean checkCommand(SlashCommandInteraction interaction, String name, Param... parameters) {
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
