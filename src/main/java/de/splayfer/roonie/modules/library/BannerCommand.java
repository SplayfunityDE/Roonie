package de.splayfer.roonie.modules.library;

import de.splayfer.roonie.utils.DefaultMessage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BannerCommand extends ListenerAdapter {

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {

        if (event.getName().equals("banner") && event.isFromGuild()) {
            String link = event.getOption("url").getAsString();
            if (link.startsWith("https://cdn.discordapp.com/attachments/")) {
                switch (event.getSubcommandName()) {
                    case "add":
                        String category = event.getOption("category").getAsString();
                        if (!LibraryManager.existsBanner(link)) {
                            LibraryManager.addBanner(category, link);
                            event.replyEmbeds(DefaultMessage.success("Banner erfolgreich hinzugefügt!", "Der angegeben Banner wurde erfolgerich in die Bibliothek hinzugefügt!", new MessageEmbed.Field("Details", ":link: Banner: `" + link + "`\n \uD83D\uDCC1 Kategorie: `" + category + "`", false))).setEphemeral(true).queue();
                        } else
                            event.replyEmbeds(DefaultMessage.error("Banner existiert bereits", "Es scheint als wurde der angegebene Banner bereits zuvor hinzugefügt...")).setEphemeral(true).queue();
                        break;
                    case "remove":
                        if (LibraryManager.existsBanner(link)) {
                            LibraryManager.removeBanner(link);
                            event.replyEmbeds(DefaultMessage.success("Banner erfolgreich entfernt!", "Der angegeben Banner wurde erfolgreich aus der Bibliothek entfernt!", new MessageEmbed.Field("Details", ":link: Banner: `" + link + "`", false))).setEphemeral(true).queue();
                        } else
                            event.replyEmbeds(DefaultMessage.error("Banner nicht gefunden", "Es scheint als existiert dein angegebener Banner nicht :(")).setEphemeral(true).queue();
                        break;
                }
            } else
                event.replyEmbeds(DefaultMessage.error("Ungültiger Link", "Bei `" + link + "` handelt es sich um keinen gültigen Discord Attachment Url!", new MessageEmbed.Field("So sollte ein Url aussehen", "https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png", false))).setEphemeral(true).queue();
        }
    }
}