package de.splayfer.roonie.nitrogames;

import de.splayfer.roonie.FileSystem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class NitroGamesListener extends ListenerAdapter {

    protected File gameLog = FileSystem.NitroGamesLog.getAbsoluteFile();
    protected YamlConfiguration yml = YamlConfiguration.loadConfiguration(gameLog);

    public void onStringSelectInteraction(StringSelectInteractionEvent event) {

        if (event.getSelectMenu().getId().equals("nitrogames.topic")) {

            EmbedBuilder bannerEmbed;
            EmbedBuilder entryEmbed;
            List<Button> buttons;

            switch (event.getValues().get(0)) {

                case "downloadGames":

                    bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0x28346d);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/935611372786577509/banner_nitrogames_games.png");

                    entryEmbed = new EmbedBuilder();
                    entryEmbed.setColor(0x28346d);
                    entryEmbed.setTitle("Wähle dein Game!");
                    entryEmbed.setDescription("Wähle aus, zu welchem Game du einen Code erhalten möchtest!");
                    entryEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                    event.replyEmbeds(bannerEmbed.build(), entryEmbed.build()).setEphemeral(true).addActionRow(StringSelectMenu.create("nitrogames.select")

                            .setPlaceholder("Wähle dein Game aus!")
                            .setRequiredRange(1, 25)

                            .addOption("Stage Towers", "https://ptb.discord.com/store/skus/417068114480726017/sage-towers", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))
                            .addOption("Jumplats", "https://ptb.discord.com/store/skus/618864578545319956/jumplats", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))
                            .addOption("Adventure of PepeL", "https://ptb.discord.com/store/skus/554072366213234729/the-adventures-of-pepel", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))
                            .addOption("PickCrafter", "https://ptb.discord.com/store/skus/560643262424285194/pickcrafter", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))
                            .addOption("Forestir", "https://ptb.discord.com/store/skus/554072621000556584/forestir", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))
                            .addOption("Hard Being A Dog", "https://ptb.discord.com/store/skus/565994833953554432/it-s-hard-being-a-dog", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))
                            .addOption("Star Sonata 2", "https://ptb.discord.com/store/skus/459415040227803141/star-sonata-2", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))
                            .addOption("Sandboxes", "https://ptb.discord.com/store/skus/519249930611589141/sandboxes", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))
                            .addOption("Secret Laboratory", "https://ptb.discord.com/store/skus/420676877766623232/scp-secret-laboratory", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))
                            .addOption("ZombsRoyale.io", "https://ptb.discord.com/store/skus/519338998791929866/zombsroyale-io", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))
                            .addOption("Heroes & Generals WWII", "https://ptb.discord.com/store/skus/550277544025522176/heroes-generals-wwii", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))
                            .addOption("Paladins", "https://ptb.discord.com/store/skus/528145079819436043/paladins", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))
                            .addOption("Light From The Butt", "https://ptb.discord.com/store/skus/594073512906588179/light-from-the-butt", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))
                            .addOption("PollyDestroyer Music", "https://ptb.discord.com/store/skus/571432807067549696/pollydestroyer-music", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))
                            .addOption("Reworld", "https://ptb.discord.com/store/skus/610933633653669891", Emoji.fromCustom("controller", Long.parseLong("935910478289440788"), false))

                            .build()).queue();

                    break;

                case "tutorialGames":

                    bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0x28346d);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/935611372786577509/banner_nitrogames_games.png");

                    entryEmbed = new EmbedBuilder();
                    entryEmbed.setColor(0x28346d);
                    entryEmbed.setTitle(":mag_right: Schau dir eine Anleitung an!");
                    entryEmbed.setDescription("In diesem Video wird dir alles gezeigt, was du zu der Nutzung von Nitro Games wissen musst!");
                    entryEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                    buttons = new ArrayList<>();
                    buttons.add(Button.secondary("link", "Schau es dir jetzt an!").withEmoji(Emoji.fromCustom("text", Long.parseLong("886623802954498069"), false)).withUrl("https://www.youtube.com/watch?v=YiZdlhnq-Qw&t"));

                    event.replyEmbeds(bannerEmbed.build(), entryEmbed.build()).setEphemeral(true).addActionRow(buttons).queue();

                    break;

                case "furhterGames":

                    bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0x43b480);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                    entryEmbed = new EmbedBuilder();
                    entryEmbed.setColor(0x43b480);
                    entryEmbed.setTitle(":notepad_spiral: Infos erfolgreich geladen!");
                    entryEmbed.setDescription("Klicke auf den Button unter dieser Nachricht und du wirst weitergeleitet!");
                    entryEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                    buttons = new ArrayList<>();
                    buttons.add(Button.secondary("nitrogames.link", "Erfahre mehr!").withEmoji(Emoji.fromFormatted("\uD83D\uDD0E")).withUrl("https://support.discord.com/hc/de/articles/360034828852-Wo-sind-die-Nitro-Spiele-"));

                    event.replyEmbeds(bannerEmbed.build(), entryEmbed.build()).setEphemeral(true).addActionRow(buttons).queue();

                    break;

            }

        } else if (event.getSelectMenu().getId().equals("nitrogames.select")) {

            StringBuilder links = new StringBuilder();

            for (String s : event.getValues()) {

                links.append(" ").append(s);

            }

            EmbedBuilder bannerEmbed = new EmbedBuilder();
            bannerEmbed.setColor(0x43b480);
            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

            EmbedBuilder entryEmbed = new EmbedBuilder();
            entryEmbed.setColor(0x43b480);
            entryEmbed.setTitle(":notepad_spiral: Games erfolgreich geladen!");
            entryEmbed.setDescription("Klicke nun unten auf die Schaltfläche um loszulegen!");
            entryEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");  //  <-- Nicht benötigt!

            event.reply(links.toString()).setEphemeral(true).queue();

        }

    }

}