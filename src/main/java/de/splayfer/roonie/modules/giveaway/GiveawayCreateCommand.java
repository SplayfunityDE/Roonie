package de.splayfer.roonie.modules.giveaway;

import de.splayfer.roonie.utils.DefaultMessage;
import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.EntitySelectInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class GiveawayCreateCommand extends ListenerAdapter {

    static HashMap<Member, InteractionHook> interactionMap = new HashMap<>();
    static HashMap<Member, InteractionHook> menuMap = new HashMap<>();
    protected static LocalDateTime time;

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("giveaway") && event.getSubcommandName().equals("create")) {
            if (!Giveaway.existsGiveaway(event.getMember())) {
                Giveaway giveaway = Giveaway.create(event.getMember());
                interactionMap.put(event.getMember(), event.getHook());
                event.replyEmbeds(getSetupEmbed(giveaway)).setComponents(getSetupActionRow(giveaway)).setEphemeral(true).queue();
                checkTimeout(event.getMember());
            } else
                event.replyEmbeds(DefaultMessage.error("Vorgang bereits gestartet", "Du hast bereits ein Giveaway gestartet!")).addActionRow(Button.danger("giveaway.create.restart", "Neue Umfrage starten").withEmoji(Emoji.fromCustom("undo", 878590238782550076L, false)), Button.success("giveaway.create.resume", "Mit aktueller fortfahren").withEmoji(Emoji.fromCustom("text", 877158818088386580L, false))).setEphemeral(true).queue();
        }
    }

    public void onEntitySelectInteraction(EntitySelectInteractionEvent event) {
        if (event.getSelectMenu().getId().startsWith("giveaway.create")) {
            switch (event.getSelectMenu().getId().split("\\.")[2]) {
                case "channel":
                    Giveaway.getFromMember(event.getMember()) .setChannel(event.getGuild().getTextChannelById(event.getMentions().getChannels().get(0).getId()));
                    updateHook(event.getMember());
                    event.deferEdit().queue();
                    break;
                case "requirement":
                    switch (event.getSelectMenu().getId().split("\\.")[3]) {
                        case "role":
                            Giveaway.getFromMember(event.getMember()).setRequirement(List.of("role", event.getMentions().getRoles().get(0).getId()));
                            menuMap.get(event.getMember()).deleteOriginal().queue();
                            menuMap.remove(event.getMember());
                            updateHook(event.getMember());
                            break;
                    }
                    event.deferEdit().queue();
                    break;
            }
        }
    }

    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getSelectMenu().getId().startsWith("giveaway.create")) {
            switch (event.getSelectMenu().getId().split("\\.")[2]) {
                case "requirement":
                    switch (event.getValues().get(0).split("\\.")[3]) {
                        case "none":
                            Giveaway.getFromMember(event.getMember()).setRequirement(List.of("none"));
                            updateHook(event.getMember());
                            break;
                        case "role":
                            menuMap.put(event.getMember(), event.reply("Eingeben").addActionRow(EntitySelectMenu.create("giveaway.create.requirement.role", EntitySelectMenu.SelectTarget.ROLE).build()).setEphemeral(true).complete());
                            break;
                        case "level":
                            event.replyModal(Modal.create("giveaway.create.requirement.level", "Setze die Levelanforderung!")
                                    .addComponents(
                                            ActionRow.of(TextInput.create("level", "Level", TextInputStyle.SHORT)
                                                    .setPlaceholder("Gib das Level an")
                                                    .setRequiredRange(1, 3)
                                                    .build()))
                                    .build()).queue();
                            break;
                    }
                    event.deferEdit().queue();
                    break;
            }
        }
    }

    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getButton().getId().startsWith("giveaway.create")) {
            Giveaway giveaway;
            switch (event.getButton().getId().split("\\.")[2]) {
                case "prize":
                    event.replyModal(Modal.create("giveaway.create.prize", "Wähle den Preis!")
                            .addComponents(
                                    ActionRow.of(TextInput.create("prize", "Preis", TextInputStyle.SHORT)
                                            .setPlaceholder("Gib den Preis an")
                                            .setRequiredRange(1, 50)
                                            .build()))
                            .build()).queue();
                    break;
                case "duration":
                    event.replyModal(Modal.create("giveaway.create.duration", "Wähle die Dauer!")
                            .addActionRow(
                                    TextInput.create("duration", "Dauer", TextInputStyle.SHORT)
                                            .setPlaceholder("Beispiel: 4d 2h 8m")
                                            .setRequiredRange(1, 50)
                                            .build())
                            .build()).queue();
                    break;
                case "amount":
                    event.replyModal(Modal.create("giveaway.create.amount", "Wähle die Gewinneranzahl!")
                            .addActionRow(
                                    TextInput.create("amount", "Anzahl Gewinner", TextInputStyle.SHORT)
                                            .setPlaceholder("Gib die Anzahl der Gewinner an")
                                            .setRequiredRange(1, 3)
                                            .build())
                            .build()).queue();
                    break;
                case "picture":
                    event.replyModal(Modal.create("giveaway.create.picture", "Wähle ein Bild!")
                            .addActionRow(
                                    TextInput.create("picture", "Anzahl Gewinner", TextInputStyle.SHORT)
                                            .setPlaceholder("Gib den Url an oder lass das Feld leer")
                                            .setRequired(false)
                                            .build())
                            .build()).queue();
                    break;
                case "continue":
                    giveaway = Giveaway.getFromMember(event.getMember());
                    EmbedBuilder information = new EmbedBuilder();
                    information.setColor(0x28346d);
                    information.setTitle("Giveaway " + giveaway.getPrize());
                    information.addField("Gewinner", String.valueOf(giveaway.getAmount()), false);
                    information.addField(":clock10: Endet in", "<t:" + giveaway.getDuration() + ":R>", true);
                    information.addField("Klicke auf den unteren Button um teilzunehmen!", "", false);
                    information.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
                    giveaway.setMessage(giveaway.getChannel().sendMessageEmbeds(Embeds.BANNER_GIVEAWAY, information.build()).setActionRow(Button.primary("giveaway.enter", "Jetzt teilnehmen!")).complete());
                    giveaway.insertToMongoDB();
                    event.editMessageEmbeds(DefaultMessage.success("Giveaway erfolgreich erstellt!", "Die Umfrage wurde mit deinen angegebenen Details erfolgreich in " + giveaway.getChannel().getAsMention() + " erstellt!")).setComponents().queue();
                    interactionMap.remove(event.getMember());
                    event.deferEdit().queue();
                    break;
                case "restart":
                    interactionMap.get(event.getMember()).deleteOriginal().queue();
                    menuMap.get(event.getMember()).deleteOriginal().queue();
                    giveaway = Giveaway.create(event.getMember());
                    interactionMap.put(event.getMember(), event.replyEmbeds(getSetupEmbed(giveaway)).setEphemeral(true).setComponents(getSetupActionRow(giveaway)).complete());
                    event.getMessage().delete().queue();
                    event.deferEdit().queue();
                    break;
                case "resume":
                    interactionMap.get(event.getMember()).deleteOriginal().queue();
                    interactionMap.put(event.getMember(), event.replyEmbeds(getSetupEmbed(Giveaway.getFromMember(event.getMember()))).setEphemeral(true).setComponents(getSetupActionRow(Giveaway.getFromMember(event.getMember()))).complete());
                    event.getMessage().delete().queue();
                    event.deferEdit().queue();
                    break;
            }
        }
    }

    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().startsWith("giveaway.create")) {
            switch (event.getModalId().split("\\.")[2]) {
                case "prize":
                    Giveaway.getFromMember(event.getMember()).setPrize(event.getValue("prize").getAsString());
                    updateHook(event.getMember());
                    event.deferEdit().queue();
                    break;
                case "duration":
                    Giveaway.getFromMember(event.getMember()).setDuration(getDurationFromText(event.getValue("duration").getAsString()));
                    updateHook(event.getMember());
                    event.deferEdit().queue();
                    break;
                case "requirement":
                    switch (event.getModalId().split("\\.")[3]) {
                        case "level":
                            Giveaway.getFromMember(event.getMember()).setRequirement(List.of("level", event.getValue("level").getAsString()));
                            updateHook(event.getMember());
                            break;
                    }
                    event.deferEdit().queue();
                    break;
                case "amount":
                    Giveaway.getFromMember(event.getMember()).setAmount(Integer.parseInt(event.getValue("amount").getAsString()));
                    updateHook(event.getMember());
                    event.deferEdit().queue();
                    break;
                case "picture":
                    Giveaway.getFromMember(event.getMember()).setPicture(event.getValue("picture").getAsString());
                    updateHook(event.getMember());
                    event.deferEdit().queue();
                    break;
            }
        }
    }

    public List<MessageEmbed> getSetupEmbed(Giveaway giveaway) {
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
            if (giveaway.getRequirement().get(0).equals("none"))
                embed.addField("<:icon_lock:1178291840127078400> Bedingung", "KEINE", true);
            else
                embed.addField("<:icon_lock:1178291840127078400> Bedingung", giveaway.getRequirement().get(0) + ": " + giveaway.getRequirement().get(1), true);
        else
            embed.addField("<:icon_lock:1178291840127078400> Bedingung", "*Nicht ausgewählt*", true);
        if (giveaway.getAmount() != null)
            embed.addField("<:people:1001082477537935501> Anzahl Gewinner", "**`" + giveaway.getAmount() + "`**", true);
        else
            embed.addField("<:people:1001082477537935501> Anzahl Gewinner", "*Nicht ausgewählt*", true);
        if (giveaway.getPicture() != null)
            embed.setThumbnail(giveaway.getPicture());
        embed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
        return List.of(Embeds.BANNER_GIVEAWAY, embed.build());
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
            case 6 -> actionRow.add(ActionRow.of(Button.secondary("giveaway.create.picture", "Gib ein Bild an")));
            case 7 -> actionRow.add(ActionRow.of(Button.primary("giveaway.create.continue", "Vervollständigen").withEmoji(Emoji.fromCustom("chat", 879356542791598160L, true))));
        }
        return actionRow;
    }

    public static Long getDurationFromText(String text) {
        time = LocalDateTime.now();
        int count = 0;
        char[] array = text.replaceAll("\\s+", "").toCharArray();
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

    public void updateHook(Member member) {
        Giveaway giveaway = Giveaway.getFromMember(member);
        InteractionHook hook = interactionMap.get(member);
        hook.editOriginalEmbeds(getSetupEmbed(giveaway)).setComponents(getSetupActionRow(giveaway)).queue();
    }

    public static void checkTimeout(Member member) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (interactionMap.containsKey(member)) {
                    interactionMap.get(member).editOriginalEmbeds(DefaultMessage.error("Vorgang abgebrochen!", "Aufgrund deiner Inaktivität wurde der Erstellvorgang des Giveaways abgebrochen!")).setComponents().queue();
                    menuMap.get(member).deleteOriginal().queue();
                    interactionMap.remove(member);
                }
            }
        }, 1000 * 60 * 14);
    }
}