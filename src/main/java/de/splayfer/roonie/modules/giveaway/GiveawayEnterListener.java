package de.splayfer.roonie.modules.giveaway;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.Objects;

public class GiveawayEnterListener extends ListenerAdapter {

    public void onButtonInteraction (ButtonInteractionEvent event) {

        if (Objects.requireNonNull(event.getButton().getId()).equals("giveaway.enter")) {

            if (Giveaway.isGiveaway(event.getMessage())) {

                Giveaway giveaway = Giveaway.getFromMessage(event.getMessage());

                //wenn geladen

                List<String> entrys = Giveaway.getEntrys(event.getMessage());

                EmbedBuilder bannerEmbed;
                EmbedBuilder entryEmbed;

                switch (giveaway.getRequirement().keySet().toArray()[0].toString()) {

                    case "none":

                        if (!entrys.contains(event.getMember().getId())) {

                            Giveaway.addEntry(event.getMessage(), event.getMember());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            entryEmbed = new EmbedBuilder();
                            entryEmbed.setColor(0x43b480);
                            entryEmbed.setTitle(":tada: ERFOLGREICH TEILGENOMMEN!");
                            entryEmbed.setDescription("Du hast erfolgreich an dem Gewinnspiel teilgenommen!");
                            entryEmbed.addField("<a:wettbewerb:898566916958978078> Preis", giveaway.getPrize(), true);
                            entryEmbed.addField(":clock10: Endet in", "<t:" + giveaway.getDuration() + ":R>", true);
                            entryEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                            event.replyEmbeds(bannerEmbed.build(), entryEmbed.build()).setEphemeral(true).queue();

                        } else {

                            Giveaway.removeEntry(event.getMessage(), event.getMember());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914598873719263282/banner_erfolg2.png");

                            entryEmbed = new EmbedBuilder();
                            entryEmbed.setColor(0x43b480);
                            entryEmbed.setTitle(":tada: ERFOLGREICH ENTFERNT!");
                            entryEmbed.setDescription("Du hast deine Teilnahme an dem Giveaway erfolgreich abgebrochen!");
                            entryEmbed.addField("<a:wettbewerb:898566916958978078> Preis", giveaway.getPrize(), true);
                            entryEmbed.addField(":clock10: Endet in", "<t:" + giveaway.getDuration() + ":R>", true);
                            entryEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                            event.replyEmbeds(bannerEmbed.build(), entryEmbed.build()).setEphemeral(true).queue();

                        }

                        break;

                }

            }


        }

    }

}
