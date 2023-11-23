package de.splayfer.roonie.library;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class RemoveBannerCommand extends ListenerAdapter {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd - HH:mm ");
    Date currentTime = new Date();

    public void onMessageReceived (MessageReceivedEvent event) {

        if (event.isFromGuild()) {

            if (event.getMessage().getContentStripped().startsWith("/banner remove")) {

                if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {

                    String[] args = event.getMessage().getContentStripped().split(" ");

                    String link = args[2];

                    if (args.length == 3) {

                        if (LibraryManager.existsBanner(link)) {
                            LibraryManager.removeBanner(link);

                            //building embed

                            EmbedBuilder succes = new EmbedBuilder();
                            succes.setColor(0x3aa65b);
                            succes.setTitle(":art: Banner erfolgreich entfernt!");
                            succes.setDescription("Der angegeben Banner wurde erfolgreich aus der Bibliothek entfernt!");
                            succes.addField("Details zu dieser Aktion", ":link: Banner: `" + link + "`\n" +
                                    ":clock10: Uhrzeit: `" + formatter.format(currentTime) + "Uhr`", false);

                            event.getChannel().sendTyping().queue();
                            event.getChannel().sendMessageEmbeds(succes.build()).queue();

                        }



                    } else {

                        //building Embed

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle(":exclamation: **Bitte nutze `/banner remove <Link>`!**");
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
