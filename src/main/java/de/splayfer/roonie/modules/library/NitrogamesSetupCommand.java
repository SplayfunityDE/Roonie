package de.splayfer.roonie.modules.library;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class NitrogamesSetupCommand extends ListenerAdapter {

    public static void setup(SlashCommandInteractionEvent event) {
        EmbedBuilder bannerEmbed = new EmbedBuilder();
        bannerEmbed.setColor(0x28346d);
        bannerEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/935529834837319700/banner_nitrogames.png");
        EmbedBuilder gameEmbed = new EmbedBuilder();
        gameEmbed.setColor(0x28346d);
        gameEmbed.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/935534352354398289/nitrogames_logo.png");
        gameEmbed.setTitle("Hol dir Discord Nitro Games!");
        gameEmbed.setDescription("Nitro Games sind eine hervorragende Möglichkeit, um integrierte Videospiele aus der Discord Bibliothek zu spielen!");
        gameEmbed.addField("<a:hypesquad_glow:935537364250533888> Spiele effizienter!", "Durch Nitro Games benötigst du keinen Launcher und sparst so an Performance und Speicherplatz", false);
        gameEmbed.addField("<a:loading:877158828465090570> Verringere deine Ladezeiten!", "In vielen Fällen sind die Ladezeiten von Nitro Games geringer, als im Launcher", false);
        gameEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");
        event.getChannel().sendMessageEmbeds(bannerEmbed.build(), gameEmbed.build()).setActionRow(StringSelectMenu.create("nitrogames.topic")
                .setPlaceholder("Starte jetzt!")
                .addOption("Installiere sie jetzt!", "downloadGames", "Lade dir die Nitro Games herunter!", Emoji.fromCustom("download", Long.parseLong("935541956224909372"), false))
                .addOption("Wie geht das?", "tutorialGames", "Lass dir ein Tutorial anzeigen!", Emoji.fromFormatted("❓"))
                .addOption("Weitere Infos", "furhterGames", "Erfahre genauere Informationen!", Emoji.fromFormatted("\uD83D\uDDD2"))
                .build()).queue();
    }
}