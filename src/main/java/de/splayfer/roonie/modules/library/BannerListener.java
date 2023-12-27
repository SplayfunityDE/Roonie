package de.splayfer.roonie.modules.library;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

public class BannerListener extends ListenerAdapter {

    protected File cache = new File(System.getProperty("user.dir") + File.separator + "media" + File.separator + "cache");

    public void onStringSelectInteraction (StringSelectInteractionEvent event) {

        if (event.getSelectMenu().getId().equals("serverbanner")) {
            EmbedBuilder bannerEmbed;
            EmbedBuilder reply;

            switch (event.getValues().get(0)) {

                case "maker":

                    bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0xed4245);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");

                    reply = new EmbedBuilder();
                    reply.setColor(0xed4245);
                    reply.setTitle(":no_entry_sign: FUNKTION NOCH IN ARBEIT");
                    reply.setDescription("Diese Funktion befindet aktuell in Arbeit und noch nicht für den Nutzer verfügbar! Bitte habe etwas Geduld!");
                    reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                    List<Button> buttons = new ArrayList<>();
                    buttons.add(Button.secondary("setping.neuigkeiten", "Informiere mich, wenn es soweit ist!").withEmoji(Emoji.fromCustom("bell", Long.parseLong("898565138385371247"), true)));

                    event.replyEmbeds(bannerEmbed.build(), reply.build()).addActionRow(buttons).setEphemeral(true).queue();

                    break;

                case "verwaltung":

                    bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0x8b8a91);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/926066435887874058/banner_serverbanner_verwaltung.png");

                    reply = new EmbedBuilder();
                    reply.setColor(0x8b8a91);
                    reply.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/926068753807056896/verwaltung.png");
                    reply.setTitle("Für welchen Zweck brauchst du deinen Banner?");
                    reply.setDescription("Hier kannst du nun auswählen, welche Art von Bannern, du für deinen Server benötigst!");
                    reply.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");

                    event.replyEmbeds(bannerEmbed.build(), reply.build()).addActionRow(StringSelectMenu.create("serverbanner.verwaltung")
                            .setPlaceholder("Triff deine Auswahl!")

                            .addOption("Willkommen", "willkommen", "Banner zur Begrüßung der Nutzer", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))
                            .addOption("Regelwerk", "regelwerk", "Banner zur Dekoration des Regelwerks", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))
                            .addOption("Neuigkeiten", "neuigkeiten", "Perfekt für neue Updates & Änderungen", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))
                            .addOption("Umfragen", "umfragen", "Ideal für Abstimmungen am Server", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))
                            .addOption("Verifizierung", "verifizierung", "Optimal für Verify Systeme", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))

                            .build()).setEphemeral(true).queue();

                    break;

                case "rollenverteilung":

                    bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0x8b8a91);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/926066434709286932/banner_serverbanner_verteilung.png");

                    reply = new EmbedBuilder();
                    reply.setColor(0x8b8a91);
                    reply.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/926837158231293963/splayfunity_rollen.png");
                    reply.setTitle("Für welchen Zweck brauchst du deinen Banner?");
                    reply.setDescription("Hier kannst du nun auswählen, welche Art von Bannern, du für deinen Server benötigst!");
                    reply.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");

                    event.replyEmbeds(bannerEmbed.build(), reply.build()).addActionRow(StringSelectMenu.create("serverbanner.rollenverteilung")
                            .setPlaceholder("Triff deine Auswahl!")

                            .addOption("Alter", "alter", "Banner zur Begrüßung der Nutzer", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))
                            .addOption("Geschlecht", "geschlecht", "Banner zur Dekoration des Regelwerks", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))
                            .addOption("Pings", "pings", "Perfekt für neue Updates & Änderungen", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))
                            .addOption("Farben", "farben", "Ideal für Abstimmungen am Server", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))

                            .build()).setEphemeral(true).queue();

                    break;

                case "werbung":

                    bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0x8b8a91);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/926066434981912596/banner_serverbanner_werbung.png");

                    reply = new EmbedBuilder();
                    reply.setColor(0x8b8a91);
                    reply.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/926532714822262804/marketing-icon-300x300.png");
                    reply.setTitle("Für welchen Zweck brauchst du deinen Banner?");
                    reply.setDescription("Hier kannst du nun auswählen, welche Art von Bannern, du für deinen Server benötigst!");
                    reply.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");

                    event.replyEmbeds(bannerEmbed.build(), reply.build()).addActionRow(StringSelectMenu.create("serverbanner.werbung")
                            .setPlaceholder("Triff deine Auswahl!")

                            .addOption("Unsere-Werbung", "Unsere-Werbung", "Banner zur Begrüßung der Nutzer", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))
                            .addOption("Partner Bedingungen", "bedingungen", "Banner zur Dekoration des Regelwerks", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))
                            .addOption("Info", "info", "Perfekt für neue Updates & Änderungen", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))

                            .build()).setEphemeral(true).queue();

                    break;

                case "sonstiges":

                    bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0x8b8a91);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/926066435422298122/banner_serverbanner_sonstiges.png");

                    reply = new EmbedBuilder();
                    reply.setColor(0x8b8a91);
                    reply.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/926532656030683206/3391ce4715f3c814d6067911438e5bf7.png");
                    reply.setTitle("Für welchen Zweck brauchst du deinen Banner?");
                    reply.setDescription("Hier kannst du nun auswählen, welche Art von Bannern, du für sonstige Zwecke benötigst benötigst!");
                    reply.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");

                    event.replyEmbeds(bannerEmbed.build(), reply.build()).addActionRow(StringSelectMenu.create("serverbanner.werbung")
                            .setPlaceholder("Triff deine Auswahl!")

                            .addOption("Unsere-Werbung", "Unsere-Werbung", "Banner zur Begrüßung der Nutzer", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))
                            .addOption("Partner Bedingungen", "bedingungen", "Banner zur Dekoration des Regelwerks", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))
                            .addOption("Info", "info", "Perfekt für neue Updates & Änderungen", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))

                            .build()).setEphemeral(true).queue();

                    break;

            }

        } else if (event.getSelectMenu().getId().equals("serverbanner.verwaltung")) {

            EmbedBuilder show;
            boolean send = false;

            for (String s: event.getValues()) {

                switch (s) {

                    case "allgemein":

                        if (LibraryManager.existsBannerCategory(s)) {

                            int id = 1;

                            for (String link : LibraryManager.getBannerByCategory(s)) {

                                show = new EmbedBuilder();
                                show.setColor(0x2e3036);
                                show.setAuthor("Banner Allgemein #" + id, "https://discord.gg/splayfer", "https://cdn.discordapp.com/attachments/906251556637249547/925107636809117696/5053_Gears.png");
                                show.setTitle(":wrench: Klicke hier, wenn du diesen Banner auswählen möchtest");
                                show.setDescription("> Du kannst nun unter diese Nachricht klicken, um dir diesen Banner mit einem eigenen Text zu personalisieren!");

                                //creating sample banner

                                BufferedImage banner = null;
                                URLConnection urlConnection;

                                try {

                                    urlConnection = new URL(link).openConnection();
                                    urlConnection.addRequestProperty("User-Agent", "Mozilla");
                                    urlConnection.setReadTimeout(5000);
                                    urlConnection.setConnectTimeout(5000);

                                    banner = ImageIO.read(urlConnection.getInputStream());

                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                }

                                int width = banner.getWidth();
                                int height = banner.getHeight();

                                BufferedImage container = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

                                Font archive = null;

                                try {

                                    archive = Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + File.separator + "media" + File.separator + "fonts" + File.separator + "Archive.otf"));

                                } catch (FontFormatException | IOException e) {
                                    e.printStackTrace();
                                }

                                Graphics g = container.getGraphics();

                                g.drawImage(banner, 0, 0, null);

                                AttributedString beispielText = new AttributedString("Beispiel");
                                beispielText.addAttribute(TextAttribute.SIZE, 20);
                                beispielText.addAttribute(TextAttribute.FONT, archive.deriveFont(45f).deriveFont(Font.BOLD));

                                g.drawString(beispielText.getIterator(), 80, 55);

                                g.dispose();

                                try {

                                    ImageIO.write(container, "png", new File(cache.getAbsolutePath() + File.separator + "bannerCache.png"));
                                } catch (IOException exception) {
                                    exception.printStackTrace();
                                }
                                File tempFile = new File(cache.getAbsolutePath() + File.separator + "bannerCache.png");

                                Message m = Roonie.shardMan.getTextChannelById("906251556637249547").sendFiles(FileUpload.fromData(tempFile)).complete();

                                String finalLink = m.getAttachments().get(0).getUrl();

                                show.setImage(finalLink);

                                m.delete().queue();

                                List<Button> buttons = new ArrayList<>();
                                buttons.add(Button.primary("create.banner", "Erstelle jetzt deinen eigenen Banner!").withEmoji(Emoji.fromFormatted("\uD83C\uDFA8")));

                                event.getUser().openPrivateChannel().complete().sendTyping().queue();
                                event.getUser().openPrivateChannel().complete().sendMessageEmbeds(show.build()).setActionRow(buttons).queue();

                                id++;

                                send = true;

                            }

                        } else {

                            send = false;

                        }

                        break;

                    case "transparent":

                    case "rot":

                    case "lila":

                    case "grau":

                    case "grün":

                    case "blau":

                    case "musik":

                    case "wallpaper":

                    case "galaxy":

                    case "gaming":

                    case "minecraft":

                        for (String link: LibraryManager.getBannerByCategory(s)) {

                            event.getUser().openPrivateChannel().complete().sendTyping().queue();
                            event.getUser().openPrivateChannel().complete().sendMessage(link).queue();

                        }

                        break;

                    default:

                        //do nothing

                        break;
                }

            }

            EmbedBuilder bannerEmbed = new EmbedBuilder();
            if (send) {

                bannerEmbed.setColor(0x43b480);
                bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                EmbedBuilder reply = new EmbedBuilder();
                reply.setColor(0x43b480);
                reply.setTitle(":white_check_mark: Vorlagen erfolgreich gesendet!");
                reply.setDescription("Dir wurden die Vorlagen aus deiner ausgewählten Kategorie erfolgreich zugesandt!");
                reply.addField("Deine Kategorie", event.getValues().get(0), true);
                reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                event.replyEmbeds(bannerEmbed.build(), reply.build()).setEphemeral(true).queue();

            } else {

                bannerEmbed.setColor(0xed4245);
                bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");

                EmbedBuilder reply = new EmbedBuilder();
                reply.setColor(0xed4245);
                reply.setTitle(":no_entry_sign: Senden fehlgeschlagen");
                reply.setDescription("Es scheint, als sind in dieser Kategorie noch keine Vorlagen vorhanden!");
                reply.addField("Fehlende Kategorie", event.getValues().get(0), true);
                reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

            }

        }

    }

}
