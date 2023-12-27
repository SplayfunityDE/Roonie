package de.splayfer.roonie.modules.library;

import de.splayfer.roonie.utils.DefaultMessage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TemplateCommand extends ListenerAdapter {

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {

        if (event.getName().equals("template") && event.isFromGuild()) {
            String link = event.getOption("url").getAsString();
            if (link.startsWith("https://discord.new/")) {
                switch (event.getSubcommandName()) {
                    case "add":
                        String category = event.getOption("category").getAsString();
                        if (!LibraryManager.existsTemplate(link)) {
                            LibraryManager.addTemplate(category, link);
                            event.replyEmbeds(DefaultMessage.success("Server-Vorlage erfolgreich hinzugefügt!", "Die angegebene Server-Vorlage wurde erfolgerich in die Bibliothek hinzugefügt!", new MessageEmbed.Field("Details", ":link: Vorlage: `" + link + "`\n \uD83D\uDCC1 Kategorie: `" + category + "`", false))).setEphemeral(true).queue();
                        } else
                            event.replyEmbeds(DefaultMessage.error("Server-Vorlage existiert bereits", "Es scheint als wurde die angegebene Server-Vorlage bereits zuvor hinzugefügt...")).setEphemeral(true).queue();
                        break;
                    case "remove":
                        if (LibraryManager.existsTemplate(link)) {
                            LibraryManager.removeTemplate(link);
                            event.replyEmbeds(DefaultMessage.success("Server-Vorlage erfolgreich entfernt!", "Die angegebene Server-Vorlage wurde erfolgreich aus der Bibliothek entfernt!", new MessageEmbed.Field("Details", ":link: Vorlage: `" + link + "`", false))).setEphemeral(true).queue();
                        } else
                            event.replyEmbeds(DefaultMessage.error("Server-Vorlage nicht gefunden", "Es scheint als existiert die angegebene Server-Vorlage nicht :(")).setEphemeral(true).queue();
                        break;
                }
            } else
                event.replyEmbeds(DefaultMessage.error("Ungültiger Link", "Bei `" + link + "` handelt es sich um keinen gültigen Discord Server-Vorlagen Url!", new MessageEmbed.Field("So sollte ein Url aussehen", "https://discord.new/e5teVR7BEPSv", false))).setEphemeral(true).queue();
        }
    }
}