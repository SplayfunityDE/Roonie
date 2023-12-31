package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

public class TicketSetupCommand extends ListenerAdapter {

    public static void setup(SlashCommandInteractionEvent event) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0x28346d);
        embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/985551183479463998/986627257307115640/icon.png");
        embedBuilder.setTitle("**\uD83C\uDFAB 〣ERSTELLE EIN TICKET**");
        embedBuilder.setDescription("Der Ticket Support ist die erste Anlaufstelle, wenn du Hilfe benötigst, oder das Serverteam kontaktieren möchtest!");
        embedBuilder.addField("<:icon_chat:986654051183783948> Stelle eine Frage...", "> Wenn du eine allgemeine Frage zum Server oder Discord hast, bist du hier richtig!", false);
        embedBuilder.addField("<:icon_vorteile:986654264686411867> Beantrage Vorteile...", "> Hier kannst du deine Level & Booster Vorteile jederzeit beanspruchen!", false);
        embedBuilder.addField("<:icon_melden:986654508375486474> Melde einen Nutzer...", "> Dir fällt etwas merkwürdiges auf? Dann teile es uns hier mit!", false);
        embedBuilder.addField("<:icon_neu:986654769101828166> Hole dir Infos...", "> Du bist neu? Erkundige dich hier jederzeit über Funktionen, Kanäle, etc.", false);
        embedBuilder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

        event.getChannel().sendMessageEmbeds(Embeds.BANNER_TICKET, embedBuilder.build()).setActionRow(StringSelectMenu.create("support.create")
                .addOption("Stelle eine Frage", "question", "Stelle unserem Team eine Frage!", Emoji.fromCustom("support", 880028066733236264L, false))
                .addOption("Bug melden", "bug", "Teile uns einen Bug mit!", Emoji.fromCustom("warning", 877158816419020820L, false))
                .addOption("Nutzer melden", "report", "Weise auf das Fehlverhalten eines Nutzers hin!", Emoji.fromCustom("cancel", 877158821779345428L, false))
                .build()).queue();
    }
}