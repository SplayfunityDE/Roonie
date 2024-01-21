package de.splayfer.roonie.modules.poll;

import de.splayfer.roonie.utils.DefaultMessage;
import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
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
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.EntitySelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.*;

public class PollCreateCommand extends ListenerAdapter {

    static HashMap<Member, InteractionHook> interactionMap = new HashMap<>();
    static HashMap<Button, InteractionHook> buttonMap = new HashMap<>();

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("umfrage") && event.getSubcommandName().equals("create")) {
            if (!interactionMap.containsKey(event.getMember())) {
                Poll poll = Poll.create(event.getMember());
                interactionMap.put(event.getMember(), event.replyEmbeds(getSetupEmbed(poll)).setEphemeral(true).setComponents(getSetupActionRow(poll)).complete());
                checkTimeout(event.getMember());
            } else
                event.replyEmbeds(DefaultMessage.error("Vorgang bereits gestartet", "Du hast bereits eine Umfrage gestartet!")).addActionRow(Button.danger("poll.create.restart", "Neue Umfrage starten").withEmoji(Emoji.fromCustom("undo", 878590238782550076L, false)), Button.success("poll.create.resume", "Mit aktueller fortfahren").withEmoji(Emoji.fromCustom("text", 877158818088386580L, false))).setEphemeral(true).queue();
        }
    }

    public void onEntitySelectInteraction(EntitySelectInteractionEvent event) {
        if (event.getSelectMenu().getId().startsWith("poll.create")) {
            if ("channel".equals(event.getSelectMenu().getId().split("\\.")[2])) {
                if (event.getMentions().getChannels().get(0).getType().equals(ChannelType.TEXT)) {
                    Poll poll = Poll.getFromMember(event.getMember());
                    poll.setChannel(event.getGuild().getTextChannelById(event.getMentions().getChannels().get(0).getId()));
                    updateHook(event.getMember());
                    event.deferEdit().queue();
                }
            }
        }
    }

    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getButton().getId().startsWith("poll.create")) {
            Button button = null;
            switch (event.getButton().getId().split("\\.")[2]) {
                case "topic":
                    event.replyModal(Modal.create("poll.create.topic", "Wähle das Thema!")
                            .addComponents(
                                    ActionRow.of(TextInput.create("topic", "Thema", TextInputStyle.SHORT)
                                            .setPlaceholder("Erläutere das Thema")
                                            .setRequiredRange(1, 50)
                                            .build()))
                            .build()).queue();
                    break;
                case "description":
                    event.replyModal(Modal.create("poll.create.description", "Setze die Beschreibung!")
                            .addComponents(
                                    ActionRow.of(TextInput.create("description", "Beschreibung", TextInputStyle.PARAGRAPH)
                                            .setPlaceholder("Beschreibe die Umfrage")
                                            .setRequiredRange(1, 100)
                                            .build()))
                            .build()).queue();
                    break;
                case "buttonContent":
                    for (Button bt : Poll.getFromMember(event.getMember()).getButtons())
                        if (bt.getId().equals(event.getButton().getId().split("\\.")[3]))
                            button = bt;
                    event.replyModal(Modal.create("poll.create.buttonContent." + button.getId(), "Inhalt bearbeiten!")
                            .addComponents(
                                    ActionRow.of(TextInput.create("content", "Neuer Inhalt", TextInputStyle.SHORT)
                                            .setPlaceholder("Gib den neuen Inhalt an")
                                            .setRequiredRange(1, 25)
                                            .build()))
                            .build()).queue();
                    break;
                case "buttonColor":
                    for (Button bt : Poll.getFromMember(event.getMember()).getButtons())
                        if (bt.getId().equals(event.getButton().getId().split("\\.")[3]))
                            button = bt;
                    event.reply("Eingeben").addActionRow(StringSelectMenu.create("poll.create.buttonColor." + button.getId())
                            .addOption("Grün", "poll.create.buttonColor.success", Emoji.fromFormatted("\uD83D\uDFE9"))
                            .addOption("Rot", "poll.create.buttonColor.danger", Emoji.fromFormatted("\uD83D\uDFE5"))
                            .addOption("Blau", "poll.create.buttonColor.primary", Emoji.fromFormatted("\uD83D\uDFE6"))
                            .addOption("Grau", "poll.create.buttonColor.secondary", Emoji.fromFormatted("⬜"))
                            .setDefaultValues("poll.create.buttonColor." + button.getStyle().name().toLowerCase()).build()).setEphemeral(true).queue();
                    break;
                case "buttonContinue":
                    if (Poll.getFromMember(event.getMember()).getButtons().length >= 2) {
                        Poll poll = Poll.getFromMember(event.getMember());
                        EmbedBuilder information = new EmbedBuilder();
                        information.setColor(0x28346d);
                        information.setTitle("Umfrage zum Thema " + poll.getTopic());
                        information.addField("Details", poll.getDescription(), false);
                        information.addField(":clock10: Dauer der Umfrage", "`UNBEGRENZT`", true);
                        information.addField("Klicke auf einen der unteren Button um abzustimmen!", "", false);
                        information.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
                        int count = 0;
                        Button[] buttons = poll.getButtons();
                        for (Button pollButton : buttons) {
                            pollButton = pollButton.withLabel(pollButton.getLabel() + " (0)");
                            buttons[count] = pollButton;
                            count++;
                        }
                        poll.setMessage(poll.getChannel().sendMessageEmbeds(Embeds.BANNER_UMFRAGE, information.build()).setActionRow(buttons).complete());
                        poll.insertToMongoDB();
                        event.editMessageEmbeds(DefaultMessage.success("Umfrage erfolgreich erstellt!", "Die Umfrage wurde mit deinen angegebenen Details erfolgreich erstellt!")).queue();
                        interactionMap.remove(event.getMember());
                        buttonMap.forEach((bt, hook) -> {
                            if (Arrays.asList(poll.getButtons()).contains(bt)) {
                                hook.deleteOriginal().queue();
                                buttonMap.remove(bt);
                            }
                        });
                    } else
                        event.replyEmbeds(DefaultMessage.error("Ungültige Buttonangabe", "Bitte gib bei der Umfrage mindestens **2** Buttons an, um fortzufahren!")).setEphemeral(true).queue();
                    break;
                case "restart":
                    interactionMap.get(event.getMember()).deleteOriginal().queue();
                    buttonMap.forEach((bt, hook) -> {
                        if (Arrays.asList(Poll.getFromMember(event.getMember()).getButtons()).contains(bt)) {
                            hook.deleteOriginal().queue();
                            buttonMap.remove(bt);
                        }
                    });
                    Poll poll = Poll.create(event.getMember());
                    interactionMap.put(event.getMember(), event.replyEmbeds(getSetupEmbed(poll)).setEphemeral(true).setComponents(getSetupActionRow(poll)).complete());
                    event.getMessage().delete().queue();
                    break;
                case "resume":
                    interactionMap.get(event.getMember()).deleteOriginal().queue();
                    buttonMap.forEach((bt, hook) -> {hook.deleteOriginal().queue(); buttonMap.remove(bt);});
                    interactionMap.put(event.getMember(), event.replyEmbeds(getSetupEmbed(Poll.getFromMember(event.getMember()))).setEphemeral(true).setComponents(getSetupActionRow(Poll.getFromMember(event.getMember()))).complete());
                    event.getMessage().delete().queue();
                    break;
            }
        }
    }

    public void onModalInteraction(ModalInteractionEvent event) {
        if (event.getModalId().startsWith("poll.create")) {
            Button button = null;
            switch (event.getModalId().split("\\.")[2]) {
                case "topic" -> {
                    Poll.getFromMember(event.getMember()).setTopic(event.getValue("topic").getAsString());
                    updateHook(event.getMember());
                    event.deferEdit().queue();
                }
                case "description" -> {
                    Poll.getFromMember(event.getMember()).setDescription(event.getValue("description").getAsString());
                    updateHook(event.getMember());
                    event.deferEdit().queue();
                }
                case "buttonContent" -> {
                    for (Button bt : Poll.getFromMember(event.getMember()).getButtons())
                        if (bt.getId().equals(event.getModalId().split("\\.")[3]))
                            button = bt;
                    InteractionHook hook = buttonMap.get(button);
                    buttonMap.remove(button);
                    List<Button> list = new ArrayList<>(Arrays.asList(Poll.getFromMember(event.getMember()).getButtons()));
                    int index = list.indexOf(button);
                    list.remove(button);
                    button = button.withLabel(event.getValue("content").getAsString());
                    list.add(index, button);
                    MessageEmbed msgEmbed = hook.retrieveOriginal().complete().getEmbeds().get(0);
                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(msgEmbed.getColor());
                    embed.setTitle(msgEmbed.getTitle());
                    embed.addField(msgEmbed.getFields().get(0).getName(), event.getValue("content").getAsString(), true);
                    embed.addBlankField(true);
                    embed.addField(msgEmbed.getFields().get(1).getName(), getColorIcon(button.getStyle()) + " " + getColorName(button.getStyle()), true);
                    hook.editOriginalEmbeds(embed.build()).queue();
                    Poll.getFromMember(event.getMember()).setButtons(list.toArray(new Button[0]));
                    event.deferEdit().queue();
                    updateHook(event.getMember());
                    buttonMap.put(button, hook);
                }
            }
        }
    }

    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getSelectMenu().getId().startsWith("poll.create")) {
            EmbedBuilder embed;
            Button button = null;
            List<Button> list;
            switch (event.getValues().get(0).split("\\.")[2]) {
                case "buttonEdit":
                    for (Button bt : Poll.getFromMember(event.getMember()).getButtons())
                        if (bt.getId().equals(event.getValues().get(0).split("\\.")[3]))
                            button = bt;
                    embed = new EmbedBuilder();
                    embed.setColor(0x28346d);
                    embed.setTitle("Button bearbeiten");
                    embed.addField("Inhalt", button.getLabel(), true);
                    embed.addBlankField(true);
                    embed.addField("Farbe", getColorIcon(button.getStyle()) + " " + getColorName(button.getStyle()), true);
                    buttonMap.put(button, event.replyEmbeds(embed.build()).setEphemeral(true).addActionRow(
                            Button.secondary("poll.create.buttonContent." + button.getId(), "Inhalt bearbeiten"),
                            Button.secondary("poll.create.buttonColor." + button.getId(), "Farbe ändern")
                    ).complete());
                    break;
                case "buttonAdd":
                    if (!(Poll.getFromMember(event.getMember()).getButtons().length >= 5)) {
                        embed = new EmbedBuilder();
                        embed.setColor(0x28346d);
                        embed.setTitle("Button erstellen");
                        embed.addField("Inhalt", "*Nicht angegeben*", true);
                        embed.addBlankField(true);
                        embed.addField("Farbe", "*Nicht angegeben*", true);
                        list = new ArrayList<>(Arrays.asList(Poll.getFromMember(event.getMember()).getButtons()));
                        button = Button.secondary("button" + (list.size() + 1), "default");
                        list.add(button);
                        Poll.getFromMember(event.getMember()).setButtons(list.toArray(new Button[list.size()]));
                        buttonMap.put(button, event.replyEmbeds(embed.build()).setEphemeral(true).addActionRow(
                                Button.secondary("poll.create.buttonContent." + button.getId(), "Inhalt festlegen"),
                                Button.secondary("poll.create.buttonColor." + button.getId(), "Farbe wählen")
                        ).complete());
                    } else
                        event.replyEmbeds(DefaultMessage.error("Buttonlimit erreicht!", "Du kannst maximal **5** Buttons in einer Umfrage hinzufügen.")).setEphemeral(true).queue();
                    break;
                case "buttonColor":
                    for (Button bt : Poll.getFromMember(event.getMember()).getButtons())
                        if (bt.getId().equals(event.getSelectMenu().getId().split("\\.")[3]))
                            button = bt;
                    InteractionHook hook = buttonMap.get(button);
                    buttonMap.remove(button);
                    list = new ArrayList<>(Arrays.asList(Poll.getFromMember(event.getMember()).getButtons()));
                    int index = list.indexOf(button);
                    list.remove(button);
                    for (ButtonStyle bt : ButtonStyle.values())
                        if (bt.name().equalsIgnoreCase(event.getValues().get(0).split("\\.")[3]))
                            button = button.withStyle(bt);
                    list.add(index, button);
                    MessageEmbed msgEmbed = hook.retrieveOriginal().complete().getEmbeds().get(0);
                    embed = new EmbedBuilder();
                    embed.setColor(msgEmbed.getColor());
                    embed.setTitle(msgEmbed.getTitle());
                    embed.addField(msgEmbed.getFields().get(0).getName(), button.getLabel(), true);
                    embed.addBlankField(true);
                    embed.addField(msgEmbed.getFields().get(1).getName(), getColorIcon(button.getStyle()) + " " + getColorName(button.getStyle()), true);
                    hook.editOriginalEmbeds(embed.build()).queue();
                    Poll.getFromMember(event.getMember()).setButtons(list.toArray(new Button[list.size()]));
                    event.deferEdit().queue();
                    updateHook(event.getMember());
                    buttonMap.put(button, hook);
                    event.getHook().deleteOriginal().queue();
                    break;
            }
        }
    }

    public List<MessageEmbed> getSetupEmbed(Poll poll) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(0x28346d);
        if (poll.getChannel() != null)
            embed.addField("<:channel:1001082478804615238> Kanal", poll.getChannel().getAsMention(), true);
        else
            embed.addField("<:channel:1001082478804615238> Kanal", "*Nicht ausgewählt*", true);
        if (poll.getTopic() != null)
            embed.addField("<:write:1001784497626435584> Thema", poll.getTopic(), true);
        else
            embed.addField("<:write:1001784497626435584> Thema", "*Nicht ausgewählt*", true);
        if (poll.getDescription() != null)
            embed.addField("<:list:1002591375960842300> Beschreibung", poll.getDescription(), true);
        else
            embed.addField("<:list:1002591375960842300> Beschreibung", "*Nicht ausgewählt*", true);
        embed.addBlankField(false);
        int count = 1;
        for (Button button : poll.getButtons()) {
            embed.addField(getColorIcon(button.getStyle()) + " " + count + ". Button", button.getLabel(), true);
            count++;
        }
        embed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
        return List.of(Embeds.BANNER_UMFRAGE, embed.build());
    }

    public List<ActionRow> getSetupActionRow(Poll poll) {
        List<ActionRow> actionRow = new ArrayList<>();
        switch (poll.getStep()) {
            case 1 -> actionRow.add(ActionRow.of(EntitySelectMenu.create("poll.create.channel", EntitySelectMenu.SelectTarget.CHANNEL).setChannelTypes(ChannelType.TEXT).build()));
            case 2 -> actionRow.add(ActionRow.of(Button.secondary("poll.create.topic", "Wähle das Thema")));
            case 3 -> actionRow.add(ActionRow.of(Button.secondary("poll.create.description", "Wähle eine Beschreibung")));
            case 4 -> {
                StringSelectMenu.Builder builder = StringSelectMenu.create("poll.create.buttons");
                for (Button button : poll.getButtons())
                    builder.addOption(button.getLabel(), "poll.create.buttonEdit." + button.getId(), "Klicke zum Bearbeiten!", Emoji.fromCustom("write", 1001784497626435584L, false));
                builder.addOption("Button hinzufügen", "poll.create.buttonAdd.", Emoji.fromCustom("icon-neu", 986654769101828166L, false));
                actionRow.add(ActionRow.of(builder.build()));
                actionRow.add(ActionRow.of(Button.primary("poll.create.buttonContinue", "Vervollständigen").withEmoji(Emoji.fromCustom("chat", 879356542791598160L, true))));
            }
        }
        return actionRow;
    }

    public void updateHook(Member member) {
        Poll poll = Poll.getFromMember(member);
        InteractionHook hook = interactionMap.get(member);
        hook.editOriginalEmbeds(getSetupEmbed(poll)).setComponents(getSetupActionRow(poll)).queue();
    }

    public String getColorName(ButtonStyle style) {
        HashMap<ButtonStyle, String> name = new HashMap<>(){{
            put(ButtonStyle.SUCCESS, "Grün");
            put(ButtonStyle.DANGER, "Rot");
            put(ButtonStyle.PRIMARY, "Blau");
            put(ButtonStyle.SECONDARY, "Grau");
        }};
        return name.get(style);
    }

    public String getColorIcon(ButtonStyle style) {
        HashMap<ButtonStyle, String> name = new HashMap<>(){{
            put(ButtonStyle.SUCCESS, "\uD83D\uDFE2");
            put(ButtonStyle.DANGER, "\uD83D\uDD34");
            put(ButtonStyle.PRIMARY, "\uD83D\uDD35");
            put(ButtonStyle.SECONDARY, "⚪");
        }};
        return name.get(style);
    }

    public static void checkTimeout(Member member) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if (interactionMap.containsKey(member)) {
                    interactionMap.get(member).editOriginalEmbeds(DefaultMessage.error("Vorgang abgebrochen!", "Aufgrund deiner Inaktivität wurde der Erstellvorgang des Giveaways abgebrochen!")).setComponents().queue();
                    interactionMap.remove(member);
                    buttonMap.forEach((bt, hook) -> {
                        if (Arrays.asList(Poll.getFromMember(member).getButtons()).contains(bt)) {
                            hook.deleteOriginal().queue();
                            buttonMap.remove(bt);
                        }
                    });
                }
            }
        }, 1000 * 60 * 14);
    }
}