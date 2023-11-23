package de.splayfer.roonie.library;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class LibrarySetupCommand extends ListenerAdapter {

    public void onMessageReceived (MessageReceivedEvent event) {

        if (event.isFromGuild()) {

            if (event.getMessage().getContentStripped().startsWith("/setup library")) {

                String[] args = event.getMessage().getContentStripped().split(" ");

                if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {

                    if (args.length == 3) {

                        TextChannel channel = event.getChannel().asTextChannel();

                        switch (args[2]) {

                            case "1":

                                //unlock

                                channel.sendTyping().queue();
                                channel.sendMessage("https://cdn.discordapp.com/attachments/883278317753626655/892445735746949150/Pfeil_2.png").queue();
                                channel.sendMessage(":lock: **Schalte die Bibliothek auf SPLΛYFUNITY frei!**\n" +
                                        "\n" +
                                        "> Die Bibliothek gibt dir Zugriff auf zahlreiche <:folder:883415478700232735> Servervorlagen, :frame_photo: Designs, <:bot:877158821276024874> Bots und vielem mehr!\n" +
                                        "> Es ist der perfekt Ort für Server Inhaber, um sich auszutauschen und den Server zu verbessern.\n" +
                                        "\n" +
                                        "> Unter der Kategorie \"Server Vorlagen\" kannst du dir unser Sortiment an Discord Server Vorlagen genauer anschauen. Dort findest du alle wichtigen Dinge, die du für deinen Start als Server Owner benötigst!\n" +
                                        "\n" +
                                        "> Unter der Kategorie \"Designs\" findest du Banner und Texte in allen Formen und Varianten, egal ob animiert oder transparent!").queue();
                                channel.sendMessage("https://cdn.discordapp.com/attachments/883278317753626655/892417910054740018/splayfunity_bookshelf1.png").queue();

                                List<Button> buttons = new ArrayList<>();
                                buttons.add(Button.secondary("unlocklibrary", "Jetzt freischalten!").withEmoji(Emoji.fromFormatted("\uD83D\uDD12")));

                                channel.sendMessage("> Klicke jetzt einfach auf den Button unter dieser <a:chat:879356542791598160> Nachricht und schau dich um!").setActionRow(buttons).queue();

                                break;

                            case "2":

                                //server templates

                                EmbedBuilder bannerEmbed = new EmbedBuilder();
                                bannerEmbed.setColor(0xb9bbbe);
                                bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905401308201250866/banner_servertemplates_2.png");

                                EmbedBuilder servertemplates = new EmbedBuilder();
                                servertemplates.setColor(0xb9bbbe);
                                servertemplates.setThumbnail("https://cdn.discordapp.com/attachments/883278317753626655/892449913932234795/template.PNG");
                                servertemplates.setTitle(":file_folder: Server Vorlagen zum Benutzen");
                                servertemplates.setDescription("Hier erhältst du Zugriff auf zahlreiche Server Banner. Wähle dazu einfach die passende Kategorie im Menü unter dieser Nachricht aus!");
                                servertemplates.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                                servertemplates.addField(":question: Wie funktionieren die Kategorien?", "Das System ist ganz einfach! Wir unterteilen alle Vorlagen in Kategorien. Du musst einfach nur im Menü unter dieser Nachricht das passende Menü auswählen und bekommst alle Server, die unter diese Kategorie fallen per Direktnachricht zugeschickt!", false);
                                servertemplates.addField("\uD83D\uDCA1 Tipp!", "Du kannst dir auch mehrere Kategorien gleichzeitig zuschicken lassen!", false);

                                channel.sendTyping().queue();

                                channel.sendMessageEmbeds(bannerEmbed.build(), servertemplates.build()).setActionRow(StringSelectMenu.create("servertemplates")
                                        .setPlaceholder("\uD83D\uDCC1 Wähle deine Server Kategorien!")
                                        .addOption("Gaming", "gaming", "Klicke hier, um diese Kategorie auszuwählen!", Emoji.fromCustom(event.getJDA().getEmojiById("885085671579062284")))
                                        .addOption("Musik", "musik", "Klicke hier, um diese Kategorie auszuwählen!", Emoji.fromCustom(event.getJDA().getEmojiById("886624918983278622")))
                                        .addOption("Community", "community", "Klicke hier, um diese Kategorie auszuwählen!", Emoji.fromCustom(event.getJDA().getEmojiById("885212849440448512")))
                                        .addOption("Content Creator", "content", "Klicke hier, um diese Kategorie auszuwählen!", Emoji.fromFormatted("\uD83D\uDCFD"))
                                        .addOption("Galaxy", "galaxy", "Klicke hier, um diese Kategorie auszuwählen!", Emoji.fromFormatted("\uD83C\uDF00"))
                                        .addOption("Projekt", "projekt", "Klicke hier, um diese Kategorie auszuwählen!", Emoji.fromCustom(event.getJDA().getEmojiById("892461354533941268")))

                                        .build()).queue();

                                break;

                            case "3":

                                //server designs

                                EmbedBuilder serverbannerheadline = new EmbedBuilder();
                                serverbannerheadline.setColor(0xd99f83);
                                serverbannerheadline.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905401309660848148/banner_banner.png");

                                EmbedBuilder serverbanner = new EmbedBuilder();
                                serverbanner.setColor(0xd99f83);
                                serverbanner.setThumbnail("https://cdn.discordapp.com/attachments/883278317753626655/892449913932234795/template.PNG");
                                serverbanner.setTitle("\uD83C\uDFA8 Server Banner zum Benutzen");
                                serverbanner.setDescription("Hier erhältst du Zugriff auf zahlreiche Server Banner. Wähle dazu einfach die passende Kategorie im Menü unter dieser Nachricht aus!\n");
                                serverbanner.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                                serverbanner.addField(":question: Wie funktionieren die Kategorien?", "Das System ist ganz einfach! Wir unterteilen alle Desgins in Kategorien. Du musst einfach nur im Menü unter dieser Nachricht das passende Menü auswählen und bekommst alle Server, die unter diese Kategorie fallen per Direktnachricht zugeschickt!", false);
                                serverbanner.addField("\uD83D\uDCA1 Tipp!", "Du kannst dir auch mehrere Kategorien gleichzeitig zuschicken lassen!", false);

                                channel.sendTyping().queue();

                                channel.sendMessageEmbeds(serverbannerheadline.build(), serverbanner.build()).setActionRow(StringSelectMenu.create("serverbanner")
                                        .setPlaceholder("\uD83C\uDFA8 Wähle deine Banner Kategorien!")
                                        .addOption("Banner-Maker (Coming Soon)", "maker", "Klicke, um diese Banner anzusehen", Emoji.fromCustom("staff", Long.parseLong("879285803531010118"), true))
                                        .addOption("Server-Verwaltung", "verwaltung", "Klicke, um diese Banner anzusehen", Emoji.fromCustom("symbol_verwaltung", Long.parseLong("925823049272528967"), false))
                                        .addOption("Rollenverteilung", "rollenverteilung", "Klicke, um diese Banner anzusehen", Emoji.fromFormatted("\uD83C\uDFAD"))
                                        .addOption("Werbung", "werbung", "Klicke, um diese Banner anzusehen", Emoji.fromCustom("promotion", Long.parseLong("893878976236359801"), false))
                                        .addOption("Sonstiges", "sonstiges", "Klicke, um diese Banner anzusehen", Emoji.fromFormatted("\uD83D\uDDD2"))

                                        .build()).queue();


                                break;

                            default:

                                //building Embed

                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle(":exclamation: **Bitte nutze `/setup library <Modul>`!**");
                                embedBuilder.setColor(0xc01c34);

                                event.getChannel().sendTyping().queue();
                                Message m = event.getChannel().sendMessageEmbeds(embedBuilder.build()).complete();
                                embedBuilder.clear();

                                Timer t = new Timer();
                                t.schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        m.delete().queue();

                                        t.cancel();

                                    }
                                }, 8000);

                                break;

                        }

                    } else {

                        //building Embed

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle(":exclamation: **Bitte nutze `/setup library <Modul>`!**");
                        embedBuilder.setColor(0xc01c34);

                        event.getChannel().sendTyping().queue();
                        Message m = event.getChannel().sendMessageEmbeds(embedBuilder.build()).complete();
                        embedBuilder.clear();

                        Timer t = new Timer();
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {

                                m.delete().queue();

                                t.cancel();

                            }
                        }, 8000);

                    }

                } else {

                    //building Embed

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle(":exclamation: **Du hast nicht die Rechte, dies zu tun!**");
                    embedBuilder.setColor(0xc01c34);

                    event.getChannel().sendTyping().queue();
                    Message m = event.getChannel().sendMessageEmbeds(embedBuilder.build()).complete();
                    embedBuilder.clear();

                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            m.delete().queue();

                            t.cancel();

                        }
                    }, 8000);

                }

                event.getMessage().delete().queue();

            }

        }

    }

}
