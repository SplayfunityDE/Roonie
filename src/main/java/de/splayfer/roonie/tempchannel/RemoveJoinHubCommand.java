package de.splayfer.roonie.tempchannel;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RemoveJoinHubCommand extends ListenerAdapter {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd - HH:mm ");
    Date currentTime = new Date();

    public void onMessageReceived (MessageReceivedEvent event) {

        if (event.isFromGuild()) {

            if (event.getMessage().getContentStripped().toLowerCase(Locale.ROOT).startsWith(Roonie.prefix + "voicehub remove")) {

                if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {

                    String[] args = event.getMessage().getContentStripped().split(" ");

                    if (args.length == 3) {

                        try {

                            Double.parseDouble(args[2]);

                            VoiceChannel c = event.getGuild().getVoiceChannelById(args[2]);

                            if (JoinHubManager.existesJoinHub(c.getIdLong())) {
                                JoinHubManager.removeJoinHub(c);
                            }

                            EmbedBuilder sucess = new EmbedBuilder();
                            sucess.setTitle("Der Kanal " + c.getName() + " ist nun kein Voicehub mehr!");
                            sucess.setDescription("Über diesen Kanal können nun keine temporäre Kanäle mehr erstellt werden!");
                            sucess.addField("<a:loading:877158828465090570> Aktion durchgeführt von", event.getMember().getAsMention(), true);
                            sucess.addField(":clock1130: Uhrzeit der Aktion", formatter.format(currentTime), true);

                            event.getChannel().sendTyping().queue();
                            event.getChannel().sendMessageEmbeds(sucess.build()).queue();

                        } catch (IllegalArgumentException illegalArgumentException) {

                            //building Embed

                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setTitle(":exclamation: **Bitte nutze `/voicehub remove <channelID / #channel>`!**");
                            embedBuilder.setColor(0xc01c34);

                            event.getChannel().sendTyping().queue();
                            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                            embedBuilder.clear();

                        }


                    } else {

                        //building Embed

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle(":exclamation: **Bitte nutze `/voicehub remove <channelID / #channel>`!**");
                        embedBuilder.setColor(0xc01c34);

                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                        embedBuilder.clear();

                    }

                } else {

                    //building Embed

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle(":exclamation: **Du hast nicht die Rechte, dies zu tun!**");
                    embedBuilder.setColor(0xc01c34);

                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                    embedBuilder.clear();

                }

            }

        }

    }

}
