package de.splayfer.roonie.commands;

import de.splayfer.roonie.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SetupCommand extends ListenerAdapter {

    protected static Role adminrole;
    protected static Role modrole;
    protected static Role suprole;
    protected static Role azubirole;

    public static void setup(SlashCommandInteractionEvent event) {
        if (event.isFromGuild()) {

            adminrole = event.getGuild().getRoleById("873515181114806272");
            modrole = event.getGuild().getRoleById("873508502063169556");
            suprole = event.getGuild().getRoleById("873515229470928937");
            azubirole = event.getGuild().getRoleById("880863788763603054");

            EmbedBuilder bannerEmbed = new EmbedBuilder();
            bannerEmbed.setColor(0x28346d);
            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/909071872723935292/banner_commands.png");

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
            Message commandMessage = event.getChannel().sendMessageEmbeds(bannerEmbed.build(), commandEmbed.build(), deleteEmbed.build()).setActionRow(StringSelectMenu.create("selectCommandInfo")
                    .addOption("Level Commands", "levelcommands", "Klicke hier, um diese Commands anzeigen zu lassen!", Emoji.fromCustom("level", Long.parseLong("909085962934562896"), false))
                    .addOption("Musik Commands", "musiccommands", "Klicke hier, um diese Commands anzeigen zu lassen!", Emoji.fromCustom("music", Long.parseLong("886624918983278622"), true))
                    .addOption("Sonstige", "sonstige", "Klicke hier, um diese Commands anzeigen zu lassen!", Emoji.fromFormatted("\uD83D\uDDD2"))
                    .build()).complete();

            //save to config
            Config.setConfigChannel("commands", event.getChannel(), commandMessage);

        }
    }
}