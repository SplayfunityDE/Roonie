package de.splayfer.roonie.modules.giveaway;

import de.splayfer.roonie.utils.DefaultMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GiveawayCreateCommand extends ListenerAdapter {

    static HashMap<Member, InteractionHook> interactionList = new HashMap<>();
    protected static LocalDateTime time;

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("giveaway") && event.getSubcommandName().equals("create")) {
            if (!Giveaway.existsGiveaway(event.getMember())) {
                Giveaway giveaway = Giveaway.create(event.getMember());
                interactionList.put(event.getMember(), event.getHook());
                event.replyEmbeds(getSetupEmbed(giveaway)).setComponents(getSetupActionRow(giveaway)).setEphemeral(true).queue();
            } else
                event.replyEmbeds(DefaultMessage.error("Bereits erstellt", "Es scheint als hast du bereits mit dem Erstellen eines neuen Giveaways begonnen!")).setEphemeral(true).queue();
        }
    }

    public List<MessageEmbed> getSetupEmbed(Giveaway giveaway) {
        EmbedBuilder bannerEmbed = new EmbedBuilder();
        bannerEmbed.setColor(0x28346d);
        bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/910194455494144021/banner_umfrage.png");

        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(0x28346d);
        if (giveaway.getChannel() != null)
            embed.addField("<:channel:1001082478804615238> Kanal", giveaway.getChannel().getAsMention(), true);
        else
            embed.addField("<:channel:1001082478804615238> Kanal", "*Nicht ausgewählt*", true);
        if (giveaway.getPrize() != null)
            embed.addField("<:write:1001784497626435584> Preis", giveaway.getPrize(), true);
        else
            embed.addField("<:write:1001784497626435584> Preis", "*Nicht ausgewählt*", true);
        if (giveaway.getDuration() != null)
            embed.addField("<:list:1002591375960842300> Dauer", "<t:" + giveaway.getDuration() + ":R>", true);
        else
            embed.addField("<:list:1002591375960842300> Dauer", "*Nicht ausgewählt*", true);
        if (giveaway.getRequirement() != null)
            embed.addField()
        embed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
        return List.of(bannerEmbed.build(), embed.build());
    }

    public List<ActionRow> getSetupActionRow(Giveaway giveaway) {
        List<ActionRow> actionRow = new ArrayList<>();
        switch (giveaway.getStep()) {
            case 1 -> actionRow.add(ActionRow.of(EntitySelectMenu.create("giveaway.create.channel", EntitySelectMenu.SelectTarget.CHANNEL).setChannelTypes(ChannelType.TEXT).build()));
            case 2 -> actionRow.add(ActionRow.of(Button.secondary("giveaway.create.prize", "Wähle das Thema")));
            case 3 -> actionRow.add(ActionRow.of(Button.secondary("giveaway.create.duration", "Setze die Dauer des Giveaways")));
            case 4 -> actionRow.add(ActionRow.of(StringSelectMenu.create("giveaway.create.requirement")
                    .addOption("Keine Bedingung", "giveaway.create.requirement.none", "Es sind keine Bedingungen erforderlich")
                    .addOption("Rolle", "giveaway.create.requirement.role", "Das Mitglied muss Mitglied einer Rolle sein")
                    .addOption("Level", "giveaway.create.requirement.level", "Das Mitglied braucht ein bestimmtes Level")
                    .build()));
            case 5 -> actionRow.add(ActionRow.of(Button.secondary("giveaway.create.amount", "Gib die Anzahl der Gewinner an")));
            case 6 -> actionRow.add(ActionRow.of(Button.secondary("giveaway,create.picture", "Ich habe kein Bild")));
        }
        return actionRow;
    }

    public static Long getDurationFromText(String text) {
        time = LocalDateTime.now();
        int count = 0;
        char[] array = text.toCharArray();
        for (char ch : array) {
            if (!Character.isDigit(ch)) {
                String num = "";
                for (char c : Arrays.copyOfRange(array, 0, count))
                    num += c;
                switch (ch) {
                    case 'm':
                        time = time.plusMinutes(Long.parseLong(num));
                        break;
                    case 'h':
                        time = time.plusHours(Long.parseLong(num));
                        break;
                    case 'd':
                        time = time.plusDays(Long.parseLong(num));
                        break;
                }
                array = Arrays.copyOfRange(array, count, array.length);
                count = 0;
            } else
                count++;
        }
        time = time.minusHours(1);
        long unix = time.toEpochSecond(ZoneOffset.UTC);
        return unix;
    }
}

