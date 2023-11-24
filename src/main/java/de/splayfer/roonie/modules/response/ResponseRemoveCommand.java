package de.splayfer.roonie.modules.response;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ResponseRemoveCommand extends ListenerAdapter {

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {

        if (event.getName().equals("response")) {

            if (event.getSubcommandName().equals("remove")) {

                String response = event.getOptionsByName("nachricht").get(0).getAsString();

                ResponseManager.removeResponse(response);

                EmbedBuilder bannerEmbed = new EmbedBuilder();
                bannerEmbed.setColor(0x43b480);
                bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                EmbedBuilder reply = new EmbedBuilder();
                reply.setColor(0x43b480);
                reply.setTitle(":wastebasket: **RESPONSE ERFOLGREICH ENTFERNT!**");
                reply.setDescription("> Du hast den Begriff erfolgreich entfernt!");
                reply.addField("<:text:886623802954498069> Gelöschter Begriff", response, false);
                reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                event.replyEmbeds(bannerEmbed.build(), reply.build()).setEphemeral(true).queue();

            } else {

                EmbedBuilder bannerEmbed = new EmbedBuilder();
                bannerEmbed.setColor(0xed4245);
                bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");

                EmbedBuilder reply = new EmbedBuilder();
                reply.setColor(0xed4245);
                reply.setTitle(":no_entry_sign: **BEGRIFF NOCH EINGETRAGEN**");
                reply.setDescription("> Merkwürdig. Es scheint, als ist der von dir ausgewählte Begriff noch nicht eingetragen!");
                reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                event.replyEmbeds(bannerEmbed.build(), reply.build()).setEphemeral(true).queue();

            }

        }

    }

}
