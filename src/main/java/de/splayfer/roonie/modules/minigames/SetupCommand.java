package de.splayfer.roonie.modules.minigames;

import de.splayfer.roonie.config.Config;
import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class SetupCommand extends ListenerAdapter {

    public static void setup(SlashCommandInteractionEvent event) {
        EmbedBuilder mainEmbed = new EmbedBuilder();
        mainEmbed.setColor(0x28346d);
        mainEmbed.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/937036898151583794/1f3b3.png");
        mainEmbed.setTitle(":game_die: Starte ein Minigame");
        mainEmbed.setDescription("> Spiele gegen andere Nutzer des Servers Minigames. Unter dieser Nachricht kannst du den Spielmodus auswählen.");
        mainEmbed.addField(":crossed_swords: Fordere andere Spieler heraus", "Fordere andere Spieler heraus und steige in der Rangliste auf", false);
        mainEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");
        SelectMenu test = StringSelectMenu.create("minigames.topic")
                .addOption("Suche ein Match", "search", "Suche nach einem anderen Mitspieler", Emoji.fromFormatted("\uD83D\uDD0E"))
                .addOption("Herausfordern", "challenge", "Fordere einen anderen Spieler heraus", Emoji.fromFormatted("⚔"))
                .addOption("Statistiken ansehen", "stats", "Schau dir deine aktuellen Statistiken an", Emoji.fromCustom("stats", Long.parseLong("937041708967927818"), false))
                .build();
        event.getChannel().sendTyping().queue();
        event.getChannel().sendMessageEmbeds(Embeds.BANNER_MINIGAME, mainEmbed.build()).setActionRow(test).complete();
        Config.setConfigChannel("minigames", event.getChannel());
    }
}