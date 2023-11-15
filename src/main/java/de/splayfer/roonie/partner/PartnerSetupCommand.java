package de.splayfer.roonie.partner;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class PartnerSetupCommand extends ListenerAdapter {

    protected TextChannel channel;

    public void onMessageReceived (MessageReceivedEvent event) {

        if (event.isFromGuild()) {

            if (event.getMessage().getContentStripped().startsWith(Roonie.prefix + "setup partner")) {

                if (event.getChannelType().isMessage()) {

                    if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {

                        String[] args = event.getMessage().getContentStripped().split(" ");
                        channel = (TextChannel) event.getChannel();

                        if (args.length == 3) {

                            switch (args[2]) {

                                case "1":

                                    partner1();

                                    break;

                                default:

                                    //do nothing

                                    break;

                            }

                        } else {

                            //building Embed

                            EmbedBuilder embedBuilder = new EmbedBuilder();
                            embedBuilder.setTitle(":exclamation: **Bitte nutze `/setup partner <Modul>`!**");
                            embedBuilder.setColor(0xc01c34);

                            event.getChannel().sendTyping().queue();
                            event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();

                        }

                    } else {

                        //building Embed

                        EmbedBuilder embedBuilder = new EmbedBuilder();
                        embedBuilder.setTitle(":exclamation: **Du hast nicht die Rechte, dies zu tun!**");
                        embedBuilder.setColor(0xc01c34);

                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();

                    }

                }

            }

        }

    }

    public void partner1() {

        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.secondary("unlockpartner", "Jetzt freischalten!").withEmoji(Emoji.fromFormatted("\uD83D\uDD12")));

        channel.sendTyping().queue();
        channel.sendMessage("https://cdn.discordapp.com/attachments/883278317753626655/893877776321507368/splayfunity_partner.png.png").queue();
        channel.sendMessage("**:lock: Schalte den Partner Bereich auf SPLΛYFUNITY frei!**\n" +
                "\n" +
                "> In dieser Kategorie kannst du dich als :promo: Partner von SPLΛYFUNITY bewerben! Als Partner erhältst du zahlreiche Vorteile, wie :name~1:Rollen, :bot~4: Bots, Desings und vieles mehr!\n" +
                "\n" +
                "> Es gibt viele verschiedene Möglichkeiten um eine Partnerschaft mit uns zu beantragen. In der Regel betreiben wir Partnerschaften über Discord, YouTube, Instagram und Twitch. Du kannst aber auch eine individuelle Partnerschaft mit uns zusammen eingehen, die Serverleitung hat für Partnerschaften immer ein offenes Ohr!\n" +
                "\n" +
                "> Kliche jetzt auf den Button unter dieser :chat~6: Nachricht und schau dich mal um!").setActionRow(buttons).queue();

    }
}
