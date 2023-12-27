package de.splayfer.roonie.modules.management.commands;

import de.splayfer.roonie.config.Config;
import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class SetupCommand extends ListenerAdapter {

    public static void setup(SlashCommandInteractionEvent event) {
        if (event.isFromGuild()) {

            EmbedBuilder commandEmbed = new EmbedBuilder();
            commandEmbed.setColor(0x28346d);
            commandEmbed.setThumbnail("https://cdn.discordapp.com/attachments/985551183479463998/999237951559049337/coding2.gif");
            commandEmbed.setTitle(":wrench: Bot Commands auf SPLΛYFUNITY");
            commandEmbed.setDescription("In diesem Kanal findest du alle wichtigen Dinge, die du wissen musst, um unsere Bots bedienen zu können!");
            commandEmbed.addField("", "Klicke dazu einfach auf das Menü unter dieser Nachricht!", false);
            commandEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

            EmbedBuilder deleteEmbed = new EmbedBuilder();
            deleteEmbed.setColor(0x28346d);
            deleteEmbed.setTitle(":warning: Alle Nachrichten werden nach 30 Sekunden gelöscht!");

            event.getChannel().sendTyping().queue();
            Message commandMessage = event.getChannel().sendMessageEmbeds(Embeds.BANNER_COMMANDS, commandEmbed.build(), deleteEmbed.build()).setActionRow(StringSelectMenu.create("selectCommandInfo")
                    .addOption("Level Commands", "levelcommands", "Klicke hier, um diese Commands anzeigen zu lassen!", Emoji.fromCustom("level", Long.parseLong("909085962934562896"), false))
                    .addOption("Musik Commands", "musiccommands", "Klicke hier, um diese Commands anzeigen zu lassen!", Emoji.fromCustom("music", Long.parseLong("886624918983278622"), true))
                    .addOption("Sonstige", "sonstige", "Klicke hier, um diese Commands anzeigen zu lassen!", Emoji.fromFormatted("\uD83D\uDDD2"))
                    .build()).complete();

            Config.setConfigChannel("commands", event.getChannel(), commandMessage);
        }
    }
}