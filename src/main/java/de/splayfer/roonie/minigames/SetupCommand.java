package de.splayfer.roonie.minigames;

import de.splayfer.roonie.FileSystem;
import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.IOException;

public class SetupCommand extends ListenerAdapter {

    protected YamlConfiguration yml = YamlConfiguration.loadConfiguration(FileSystem.EmbedStats);

    public void onMessageReceived (MessageReceivedEvent event) {

        if (event.isFromGuild()) {

            if (event.getMessage().getContentStripped().startsWith(Roonie.prefix + "setup minigames")) {

                if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {

                    String[] args = event.getMessage().getContentStripped().split(" ");

                    if (args.length == 3) {

                        EmbedBuilder bannerEmbed;
                        EmbedBuilder mainEmbed;

                        switch (args[2]) {

                            case "1":

                                bannerEmbed = new EmbedBuilder();
                                bannerEmbed.setColor(0x28346d);
                                bannerEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/937036676239347812/banner_minigames.png");

                                mainEmbed = new EmbedBuilder();
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
                                event.getChannel().sendMessageEmbeds(bannerEmbed.build(), mainEmbed.build()).setActionRow(test).complete();

                                yml.set("channel", event.getChannel().getId());
                                try {
                                    yml.save(FileSystem.EmbedStats);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }

                                event.getMessage().delete().queue();

                                break;

                        }

                    }

                }

            }

        }

    }

}
