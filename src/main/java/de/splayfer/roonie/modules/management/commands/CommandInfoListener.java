package de.splayfer.roonie.modules.management.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandInfoListener extends ListenerAdapter {

    public void onStringSelectInteraction (StringSelectInteractionEvent selectionMenuEvent) {

        if (selectionMenuEvent.getSelectMenu().getId().equals("selectCommandInfo")) {
            EmbedBuilder bannerEmbed;
            switch (selectionMenuEvent.getValues().get(0)) {

                case "levelcommands":

                    bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0x28346d);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/918506917570183258/banner_levelsystem.png");


                    EmbedBuilder lvlEmbed = new EmbedBuilder();
                    lvlEmbed.setColor(0x28346d);
                    lvlEmbed.setTitle("<:level:909085962934562896> Levelsystem Befehle");
                    lvlEmbed.setDescription("Hier findest du alle wichtigen Befehle für das Levelsystem!");
                    lvlEmbed.addField("Befehle", "`/levels` ➜ Gibt die Vorteile des Levelsystems an \n" +
                            "`/rank <Nutzer>` ➜ Zeigt deine aktuellen XP/Level \n", false);
                    lvlEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/918508681014960188/banner_border.png");

                    selectionMenuEvent.replyEmbeds(bannerEmbed.build(), lvlEmbed.build()).setEphemeral(true).queue();
                    selectionMenuEvent.getMessage().editMessageEmbeds(selectionMenuEvent.getMessage().getEmbeds()).setActionRow(selectionMenuEvent.getSelectMenu()).queue();

                    break;

                case "musiccommands":

                    bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0x28346d);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/921745969324900412/banner_musik.png");

                    EmbedBuilder musicEmbed = new EmbedBuilder();
                    musicEmbed.setColor(0x28346d);
                    musicEmbed.setTitle("<a:music:886624918983278622> Musik Befehle");
                    musicEmbed.setDescription("Hier findest du alle wichtigen Befehle, um unsere Musikbots bedienen zu können!");
                    musicEmbed.addField("Befehle", "`/play` ➜  Spielt einen Song ab \n" +
                            "`/pause` ➜  Pausiert den aktuellen Song \n" +
                            "`/resume` ➜  Setzt die Wiedergabe des aktuellen Songs fort \n" +
                            "", false);
                    musicEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/918508681014960188/banner_border.png");

                    selectionMenuEvent.replyEmbeds(bannerEmbed.build(), musicEmbed.build()).setEphemeral(true).queue();
                    selectionMenuEvent.getMessage().editMessageEmbeds(selectionMenuEvent.getMessage().getEmbeds()).setActionRow(selectionMenuEvent.getSelectMenu()).queue();
                    break;

                case "sonstige":

                    bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0x28346d);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/922920765693775962/banner_sonstiges.png.png");

                    EmbedBuilder sonstigesEmbed = new EmbedBuilder();
                    sonstigesEmbed.setColor(0x28346d);
                    sonstigesEmbed.setTitle(":notepad_spiral: Sonstige Befehle");
                    sonstigesEmbed.setDescription("Hier findest du alle wichtigen Befehle für das Sonstige Systeme!");
                    sonstigesEmbed.addField("Befehle", "`/letsjohannes` ➜ **Hmm...** \n", false);
                    sonstigesEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/918508681014960188/banner_border.png");

                    selectionMenuEvent.replyEmbeds(bannerEmbed.build(), sonstigesEmbed.build()).setEphemeral(true).queue();
                    selectionMenuEvent.getMessage().editMessageEmbeds(selectionMenuEvent.getMessage().getEmbeds()).setActionRow(selectionMenuEvent.getSelectMenu()).queue();
                    break;

            }
        }
    }

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {
            if (event.getName().equals("letsjohannes")) {
                event.reply("https://discord.gg/NJYZVfSSf7").setEphemeral(true).queue();
            }
    }
}