package de.splayfer.roonie.library;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.HashMap;
import java.util.List;

public class TemplateListener extends ListenerAdapter {

    HashMap<String, String> initCategories() {

        return new HashMap<>(){{

            put("gaming", "<:controller:885085671579062284> Gaming");
            put("musik", "<a:music:886624918983278622> Musik");
            put("community", "<a:partner:885212849440448512> Community");
            put("content", "\uD83D\uDCFD Content Creator");
            put("galaxy", "\uD83C\uDF00 Galaxy");
            put("projekt", "<:folder:883415478700232735> Projekt");

        }};

    }

    public void onSelectMenuInteraction (SelectMenuInteractionEvent event) {

        if (event.getSelectMenu().getId().equals("servertemplates")) {

            String value = event.getValues().get(0);

            EmbedBuilder bannerEmbed = new EmbedBuilder();
            bannerEmbed.setColor(0x8b8a91);
            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925053738220150794/banner_vorlagen_gaming.png");

            EmbedBuilder reply = new EmbedBuilder();
            reply.setColor(0x8b8a91);
            reply.setTitle(":grey_exclamation: Kategorie erfolgreich gesendet!");
            reply.setDescription("Dir wurde folgende Kategorie erfolgreich zugesendet!");
            reply.addField(initCategories().get(value), "", false);
            reply.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");

            //building embed

            EmbedBuilder bb = new EmbedBuilder();
            bb.setColor(0x43b480);
            bb.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

            EmbedBuilder mb = new EmbedBuilder();
            mb.setColor(0x43b480);
            mb.setTitle("✅ **VORLAGEN ERFOLGREICH GESENDET**");
            mb.setDescription("> Dir wurden erfolgreich Servervorlagen mit den folgenden Details zugesendet!");
            mb.addField("<:text:886623802954498069> Kategorie", value, false);
            mb.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

            event.replyEmbeds(bb.build(), mb.build()).setEphemeral(true).addActionRow(Button.secondary("link", "Schau sie dir an!").withUrl("https://discord.com/channels/@me/").withEmoji(Emoji.fromCustom("folder", 883415478700232735L, false))).queue();

            try {

                event.getUser().openPrivateChannel().complete().sendMessageEmbeds(bb.build(), mb.build()).queue();

            } catch (Exception e) {
                event.reply("Du hast deine Direktenachrichten deaktiviert!").setEphemeral(true).queue();
            }

            List<String> list = LibraryManager.getTemplatesByCategory(value);

            event.getUser().openPrivateChannel().complete().sendTyping().queue();

            for (String link : list) {
                event.getUser().openPrivateChannel().complete().sendMessage(link).queue();
            }



        }

    }

}
