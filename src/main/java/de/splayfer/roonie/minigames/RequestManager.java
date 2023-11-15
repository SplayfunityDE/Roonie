package de.splayfer.roonie.minigames;

import de.splayfer.roonie.FileSystem;
import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestManager extends ListenerAdapter {

    protected static YamlConfiguration yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

    public static void sendGameRequest(User user, String id) {

        yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

        PrivateChannel dm = user.openPrivateChannel().complete();

        EmbedBuilder bannerEmbed = new EmbedBuilder();
        bannerEmbed.setColor(0x28346d);
        bannerEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/937036676239347812/banner_minigames.png");

        EmbedBuilder mainEmbed = new EmbedBuilder();
        mainEmbed.setColor(0x28346d);
        mainEmbed.setTitle(":crossed_swords: Du wurdest herausgefordert!");
        mainEmbed.setDescription("> Auf SPLΛYFUNITY wurdest du zu einem Minigame herausgefordert!");
        mainEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");


            mainEmbed.addField("Details", ":busts_in_silhouette:│Nutzer: " + Roonie.shardMan.getUserById(yml.getString(id + ".player1")).getAsMention() + "\n" +
                    ":game_die:│Game: " + yml.getString(id + ".game"), false);


        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.primary("join." + id, "Nimm die Herausforderung an!").withEmoji(Emoji.fromFormatted("⚔")));

        dm.sendTyping().queue();
        dm.sendMessageEmbeds(bannerEmbed.build(), mainEmbed.build()).setActionRow(buttons).queue();

    }

    public static void setWaitingStatus(ThreadChannel channel) {

        List<Message> messages = channel.getHistory().retrievePast(100).complete();

        channel.deleteMessages(messages).queue();

        EmbedBuilder bannerEmbed = new EmbedBuilder();
        bannerEmbed.setColor(0x28346d);
        bannerEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/937416756547452938/banner_warten.png");

        EmbedBuilder mainEmbed = new EmbedBuilder();
        mainEmbed.setColor(0x28346d);
        mainEmbed.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/937417320157020261/231b.png");
        mainEmbed.setTitle("<a:loading:877158828465090570> Warten auf Mitspieler");
        mainEmbed.setDescription("> Nun wird darauf gewartet, dass der Mitspieler, den du herausgefordert hast die Anfrage annimmt. In dieser Zeit kannst du dich mit den Spielregeln vertraut machen!");
        mainEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");

        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.secondary("minigames.tutorial", "Lies dir die Spielregeln durch!").withEmoji(Emoji.fromCustom("text", Long.parseLong("886623802954498069"), false)));
        buttons.add(Button.danger("minigames.cancel", "Spiel abbrechen").withEmoji(Emoji.fromCustom("cross", 880711722288169032L, true)));

        channel.sendTyping().queue();
        channel.sendMessageEmbeds(bannerEmbed.build(), mainEmbed.build()).setActionRow(buttons).queue();

    }

    public void onButtonInteraction (ButtonInteractionEvent event) {

        if (event.getButton().getId().equals("minigames.tutorial")) {



        } else if (event.getButton().getId().equals("minigames.cancel")) {

            yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

            if (yml.getString(event.getChannel().getId() + ".status").equals("waiting")) {

                event.getChannel().delete().queue();

                yml.set(event.getChannel().getId(), null);
                try {
                    yml.save(FileSystem.GameLog);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else {

                EmbedBuilder banner = new EmbedBuilder();
                banner.setColor(0xed4245);
                banner.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");

                EmbedBuilder main = new EmbedBuilder();
                main.setColor(0xed4245);
                main.setTitle(":no_entry_sign: **GAME BEREITS GESTARTET**");
                main.setDescription("> Es scheint als ist das Game bereits gestartet! Du kannst nur Runden im Wartemodus vorzeitig beenden!");
                main.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                event.replyEmbeds(banner.build(), main.build()).setEphemeral(true).queue();

            }

        } else if (event.getButton().getId().startsWith("join.")) {

            yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

            String id = event.getButton().getId().substring(5);

            if (yml.getKeys(false).contains(id)) {

                if (yml.getString(id + ".player2").equals(event.getUser().getId())) {

                if (yml.get(id + ".status").equals("waiting")) {

                    //get guild

                    Guild guild = null;

                    for (Guild g : Roonie.shardMan.getGuilds()) {

                        for (ThreadChannel threadChannel : g.getThreadChannels()) {

                            if (threadChannel.getId().equals(id)) {

                                guild = g;

                            }

                        }

                    }

                    Member player1 = guild.getMemberById(yml.getString(id + ".player1"));
                    Member player2 = guild.getMemberById(yml.getString(id + ".player2"));
                    ThreadChannel channel = guild.getThreadChannelById(id);

                    channel.addThreadMember(player2).queue();

                    if (yml.getString(id + ".game").equals("tictactoe")) {

                        TicTacToe.startGame(player1, player2, channel);

                    }

                    yml.set(id + ".status", "playing");
                    try {
                        yml.save(FileSystem.GameLog);
                    } catch (IOException exception) {

                    }


                    EmbedBuilder bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0x43b480);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                    EmbedBuilder mainEmbed = new EmbedBuilder();
                    mainEmbed.setColor(0x43b480);
                    mainEmbed.setTitle(":white_check_mark: Einladung erfolgreich angenommen!");
                    mainEmbed.setDescription("> Du hast die Einladung erfolgreich angenommen und kannst nun dem Game beitreten. Klicke dazu einfach auf den Button unter dieser Nachricht!");
                    mainEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                    List<Button> buttons = new ArrayList<>();
                    buttons.add(Button.secondary("link", "Trete dem Game jetzt bei!").withUrl("https://discord.com/channels/" + guild.getId() + "/" + id).withEmoji(Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false)));

                    event.replyEmbeds(bannerEmbed.build(), mainEmbed.build()).setEphemeral(true).addActionRow(buttons).queue();

                } else {

                    EmbedBuilder bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0xed4245);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");

                    EmbedBuilder entryEmbed = new EmbedBuilder();
                    entryEmbed.setColor(0xed4245);
                    entryEmbed.setTitle(":no_entry_sign: Herausforderung bereits angenommen!");
                    entryEmbed.setDescription("> Du hast die folgende Herausforderung bereits angenommen. Über den Button unter dieser Nachricht kannst du dem Game beitreten!");
                    entryEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                    //get guild

                    Guild guild = null;

                    for (Guild g : Roonie.shardMan.getGuilds()) {

                        for (ThreadChannel threadChannel : g.getThreadChannels()) {

                            if (threadChannel.getId().equals(id)) {

                                guild = g;

                            }

                        }

                    }

                    List<Button> buttons = new ArrayList<>();
                    buttons.add(Button.secondary("link", "Trete dem Game jetzt bei!").withUrl("https://discord.com/channels/" + guild.getId() + "/" + id).withEmoji(Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false)));

                    event.replyEmbeds(bannerEmbed.build(), entryEmbed.build()).setEphemeral(true).addActionRow(buttons).queue();

                }

                } else {

                    EmbedBuilder bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0xed4245);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");

                    EmbedBuilder entryEmbed = new EmbedBuilder();
                    entryEmbed.setColor(0xed4245);
                    entryEmbed.setTitle(":no_entry_sign: Anfrage zurückgezogen!");
                    entryEmbed.setDescription("> Der Nutzer hat die Herausforderung zurückgezogen.");
                    entryEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                    event.replyEmbeds(bannerEmbed.build(), entryEmbed.build()).setEphemeral(true).queue();

                }

            } else {

                EmbedBuilder bannerEmbed = new EmbedBuilder();
                bannerEmbed.setColor(0xed4245);
                bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");

                EmbedBuilder entryEmbed = new EmbedBuilder();
                entryEmbed.setColor(0xed4245);
                entryEmbed.setTitle(":no_entry_sign: Game bereits beendet!");
                entryEmbed.setDescription("> Das Game zu dem du eingeladen wurdest, ist inzwischen leider schon beendet!");
                entryEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                event.replyEmbeds(bannerEmbed.build(), entryEmbed.build()).setEphemeral(true).queue();

            }

        }

    }

}
