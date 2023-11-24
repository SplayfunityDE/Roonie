package de.splayfer.roonie.modules.library;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AddBannerCommand extends ListenerAdapter {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd - HH:mm ");
    Date currentTime = new Date();

    public void onMessageReceived (MessageReceivedEvent event) {

        if (event.isFromGuild()) {

            if (event.getMessage().getContentStripped().startsWith("/banner add")) {

                if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {

                    String[] args = event.getMessage().getContentRaw().split(" ");

                    if (args.length == 4) {

                        String category = args[2];
                        String link = args[3];

                        if (!LibraryManager.existsBanner(link)) {
                            LibraryManager.addBanner(category, link);

                            //building embed

                            EmbedBuilder succes = new EmbedBuilder();
                            succes.setColor(0x3aa65b);
                            succes.setTitle(":art: Banner erfolgreich hinzugefügt!");
                            succes.setDescription("Der angegeben Banner wurde erfolgerich in die Bibliothek hinzugefügt!");
                            succes.addField("Details zu dieser Aktion", ":link: Banner: `" + link + "`\n" +
                                    "\uD83D\uDCC1 Kategorie: `" + category + "`\n" +
                                    ":clock10: Uhrzeit: `" + formatter.format(currentTime) + "Uhr`", false);

                            event.getChannel().sendTyping().queue();
                            event.getChannel().sendMessageEmbeds(succes.build()).queue();
                        }
                    } else {

                        //building Embed

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle(":exclamation: **Bitte nutze `/banner add <Kategorie> <Link>`!**");
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

            }

        }

    }
}
