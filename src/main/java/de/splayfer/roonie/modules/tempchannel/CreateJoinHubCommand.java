package de.splayfer.roonie.modules.tempchannel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CreateJoinHubCommand extends ListenerAdapter {

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd - HH:mm ");
    Date currentTime = new Date();

    public void onMessageReceived (MessageReceivedEvent event) {

        if (event.isFromGuild()) {

            if (event.getMessage().getContentStripped().toLowerCase(Locale.ROOT).startsWith("/voicehub add")) {

                if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {

                    String[] args = event.getMessage().getContentStripped().split(" ");

                    if (args.length == 3) {

                        try {

                            Double.parseDouble(args[2]);
                            VoiceChannel c = event.getGuild().getVoiceChannelById(args[2]);

                            System.out.println(c.getName());

                            //save to yml

                            if (!JoinHubManager.existesJoinHub(c.getIdLong())) {
                                JoinHubManager.createJoinHub(c, event.getMember());
                            }

                            EmbedBuilder sucess = new EmbedBuilder();
                            sucess.setTitle("Der Kanal " + c.getName() + " ist nun ein Voicehub!");
                            sucess.setDescription("Über diesen Kanal können nun temporäre Kanäle erstellt werden!");
                            sucess.addField("<a:loading:877158828465090570> Aktion durchgeführt von", event.getMember().getAsMention(), true);
                            sucess.addField(":clock1130: Uhrzeit der Aktion", formatter.format(currentTime), true);

                            event.getChannel().sendTyping().queue();
                            event.getChannel().sendMessageEmbeds(sucess.build()).queue();

                            sucess.clear();

                        } catch (IllegalArgumentException illegalArgumentException) {

                            //building Embed

                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setTitle(":exclamation: **Bitte nutze `/voicehub add <channelID / #channel>`!**");
                            embedBuilder.setColor(0xc01c34);

                            event.getChannel().sendTyping().queue();
                            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();
                            embedBuilder.clear();

                        }


                    } else {

                        //building Embed

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle(":exclamation: **Bitte nutze `/voicehub add <channelID / #channel>`!**");
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
