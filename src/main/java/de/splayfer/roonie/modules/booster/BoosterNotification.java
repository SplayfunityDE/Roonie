package de.splayfer.roonie.modules.booster;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.modules.response.Response;
import de.splayfer.roonie.utils.DefaultMessage;
import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.ArrayList;
import java.util.List;

public class BoosterNotification extends ListenerAdapter {

    public void onGuildMemberRoleAdd (GuildMemberRoleAddEvent event) {
        if (event.getGuild().equals(Roonie.mainGuild)) {
            if (event.getRoles().get(0).equals(event.getGuild().getBoostRole())) {
                BoosterWall.updateBoosterStats();
                EmbedBuilder message = new EmbedBuilder();
                message.setColor(0x28346d);
                message.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/984902617836712016/unknown.png");
                message.setTitle("Danke für deinen Boost!");
                message.setDescription("> Danke, dass du Splayfunity mithilfe deines Boosts unterstützt hast! Nun hast du Anspruch auf folgende Vorteile:");
                message.addField("<a:giveaway:898562734680064082>〣Deine Vorteile", "<:point:940963558693425172> Den exklusiven Booster Rang \n" +
                        "<:point:940963558693425172> Erstelle Diskussionen (Threads) \n" +
                        "<:point:940963558693425172> Dein eigener Command \n" +
                        "<:point:940963558693425172> Erhalte 2x Level XP \n" +
                        "<:point:940963558693425172> Gewinnspiele ohne Bedingungen", false);
                message.addField("<a:chat:880875728915275786> Wie beantrage ich meine Vorteile?", "Klicke hierzu einfach auf den Button unter dieser Nachricht!", false);
                message.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
                event.getUser().openPrivateChannel().complete().sendMessageEmbeds(Embeds.BANNER_BOOSTER, message.build()).setActionRow(Button.secondary("boost.vorteile", "Hol dir deine Vorteile!").withEmoji(Emoji.fromCustom("boost_icon", 985150841830899752L, false))).queue();
            }
        }
    }

    public void onGuildMemberRoleRemove (GuildMemberRoleRemoveEvent event) {
        if (event.getGuild().equals(Roonie.mainGuild)) {
            if (event.getRoles().get(0).equals(event.getGuild().getBoostRole())) {

                EmbedBuilder message = new EmbedBuilder();
                message.setColor(0x28346d);
                message.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/984902617836712016/unknown.png");
                message.setTitle("<a:cross:880711722288169032>〣Boost entfernt!");
                message.setDescription("> Es scheint als hättest du deinen Boost von Splayfunity entfernt!");
                message.addField("<a:cross:880711722288169032>〣Verlorene Vorteile", "<:point:940963558693425172> Den exklusiven Booster Rang \n" +
                        "<:point:940963558693425172> Erstelle Diskussionen (Threads) \n" +
                        "<:point:940963558693425172> Dein eigener Command \n" +
                        "<:point:940963558693425172> Erhalte 2x Level XP \n" +
                        "<:point:940963558693425172> Gewinnspiele ohne Bedingungen", false);
                message.addField("<a:chat:880875728915275786> Gib uns dein Feedback!", "Klicke auf den Button unter dieser Nachricht und teile uns mit, wie dir die Zeit als Booster bei uns gefallen hat!", false);
                message.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
                event.getUser().openPrivateChannel().complete().sendMessageEmbeds(Embeds.BANNER_BOOSTER, message.build()).setActionRow(Button.secondary("boost.feedback", "Gib uns Feedback!").withEmoji(Emoji.fromCustom("level", 909085962934562896L, false))).queue();
            }
        }
    }

    public void onModalInteraction (ModalInteractionEvent event) {
        if (event.getModalId().startsWith("boost.")) {
            switch (event.getModalId()) {
                case "boost.feedback":
                    event.replyEmbeds(DefaultMessage.success("Feedback erfolgreich gesendet!", "Du hast das Feedback erfolgreich gesendet!")).setEphemeral(true).queue();
                    //:eyes: xddddddd
                    break;
                case "boost.claim.command.modal":
                    String command = event.getValue("command").getAsString();
                    String reaction = event.getValue("reaction").getAsString();
                    if (!Response.existsResponse(command)) {
                        Response.create(command, event.getMember(), "msg", reaction);
                        event.replyEmbeds(DefaultMessage.success("Response erfolgreich hinzugefügt", "Du hast die Resposne erfolgreich hinzugefügt!", new MessageEmbed.Field("<:text:886623802954498069> TextCommand", command, true), new MessageEmbed.Field("<:text:886623802954498069> Antwort des Bots", reaction, true))).setEphemeral(true).queue();
                    } else
                        event.replyEmbeds(DefaultMessage.error("Command bereits vergeben", "Der Command " + command + " wurde bereits von einem anderem Nutzer/Teammitglied vergeben!")).setEphemeral(true).queue();
                    break;
            }
        }
    }

    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getButton().getId().startsWith("boost.")) {
            switch (event.getButton().getId().split("\\.")[1]) {
                case "vorteile":
                    EmbedBuilder message = new EmbedBuilder();
                    message.setColor(0x28346d);
                    message.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/926532656030683206/3391ce4715f3c814d6067911438e5bf7.png");
                    message.setTitle("Wähle deine Vorteile");
                    message.setDescription("> Wähle im Menü unter dieser Nachricht, den Vorteil aus, zu dem du Hilfe benötigst!");
                    message.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
                    event.replyEmbeds(Embeds.BANNER_BOOSTER_PERKS, message.build()).setEphemeral(true).addActionRow(StringSelectMenu.create("boost.select")

                            .addOption("Exklusiver Rang", "rang", "Erhalte deinen eigenen Rang und werde höher angezeigt!", Emoji.fromCustom("name", 878587314367000606L, false))
                            .addOption("Starte eine Diskussion", "diskussion", "Erstelle einen Thread unter einem Textkanal!", Emoji.fromCustom("promotion", 893878976236359801L, false))
                            .addOption("Erstelle einen Command", "command", "Richte deinen eigenen Chat Command ein!", Emoji.fromCustom("coding", 885800924856070185L, true))
                            .addOption("Bekomme 2x Level XP", "level", "Du erhältest automatisch doppelte XP Anzahl", Emoji.fromCustom("level", 909085962934562896L, false))
                            .addOption("Bedingungslose Giveaways", "giveaway", "Nimm an Giveaways von Vorraussetzung teil!", Emoji.fromCustom("giveaway", 898562734680064082L, true))

                            .setPlaceholder("Wähle deine Vorteile aus!")
                            .setMaxValues(1)
                            .build()).queue();
                    break;
                case "claim":
                    switch (event.getButton().getId().split("\\.")[2]) {
                        case "command":
                            event.replyModal(Modal.create("boost.claim.command.modal", "\uD83D\uDCE2〣Lege die Nachricht fest!")
                                    .addActionRow(
                                            TextInput.create("command", "Command", TextInputStyle.PARAGRAPH)
                                                    .setPlaceholder("Gib den Textcommand an!")
                                                    .setMaxLength(200)
                                                    .setRequired(true)
                                                    .build(),
                                            TextInput.create("reaction", "Reaktion", TextInputStyle.PARAGRAPH)
                                                    .setPlaceholder("Gib die Reaktion auf den Command an!")
                                                    .setMaxLength(200)
                                                    .setRequired(true)
                                                    .build())
                                    .build()).queue();
                            break;
                    }
                    break;
                case "feedback":
                    event.replyModal(Modal.create("boost.feedback", "✨〣Feedback abgeben!")
                            .addActionRow(
                                    TextInput.create("feedback", "Feedback", TextInputStyle.PARAGRAPH)
                                    .setPlaceholder("Gib uns dein Feedback!")
                                    .setMaxLength(200)
                                    .setRequired(true)
                                    .build())
                            .build()).queue();
                    break;
            }
        }
    }

    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getSelectMenu().getId().equals("boost.select")) {
            EmbedBuilder message;
            switch (event.getValues().get(0)) {
                case "rang":
                    message = new EmbedBuilder();
                    message.setColor(0xff73fa);
                    message.setTitle("Exklusiver Booster Rang");
                    message.setDescription("> Die exklusive Booster Rolle wurde dir vollautomatisch hingezufügt, als du deinen Boost auf Splayfunity übertragen hast!");
                    message.setImage("https://cdn.discordapp.com/attachments/985551183479463998/985551358440652810/tutorial_booster.png");
                    event.replyEmbeds(Embeds.BANNER_BOOSTER_PERKS_ROLE, message.build()).setEphemeral(true).queue();
                    break;
                case "diskussion":
                    message = new EmbedBuilder();
                    message.setColor(0x28346d);
                    message.setTitle("Erstelle eine Diskussion");
                    message.setDescription("> Mithilfe des Booster Ranges bist du in der Lage, Diskussionen auf unserem Server zu erstellen!");
                    message.addField("<a:chat:879356542791598160> Wie erstelle ich eine Diskussion?", "Klicke hierfür einfach rechts oben bei dir Nachricht auf das Symbol \"Thread erstellen\"", false);
                    message.setImage("https://cdn.discordapp.com/attachments/985551183479463998/985598971714101288/tutorial_booster_2.png");
                    event.replyEmbeds(Embeds.BANNER_BOOSTER_PERKS_THREADS, message.build()).setEphemeral(true).queue();
                    break;
                case "command":
                    if (!Response.existsCreator(event.getMember())) {
                        message = new EmbedBuilder();
                        message.setColor(0xff73fa);
                        message.setTitle("Richte deinen Command ein");
                        message.setDescription("> Richte als Booster deinen eigenen Textcommand ein!");
                        message.addField("<a:chat:879356542791598160> Wie erstelle ich den Command?", "Klicke einfach auf den Button, und du kannst direkt loslegen!", false);
                        message.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
                        event.replyEmbeds(Embeds.BANNER_BOOSTER_PERKS_COMMNAD, message.build()).addActionRow(Button.primary("boost.claim.command", "Starte jetzt!").withEmoji(Emoji.fromCustom("text", 877158818088386580L, false))).setEphemeral(true).queue();
                    } else
                        event.replyEmbeds(DefaultMessage.error("Bereits verwendet", "Du kannst maximal einen Command als Booster einrichten! Wenn du deinen Command ändern möchtest, wende dich bitte an den Support!")).setEphemeral(true).queue();
                    break;
                case "level":
                    message = new EmbedBuilder();
                    message.setColor(0xff73fa);
                    message.setTitle("Erhalte einen Level Boost");
                    message.setDescription("> Du erhältest automatisch die doppelte Anzahl an XP, wenn du im Chat oder Talk aktiv bist!");
                    message.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
                    event.replyEmbeds(Embeds.BANNER_BOOSTER_PERKS_LEVEL, message.build()).setEphemeral(true).queue();
                    break;
                case "giveaway":
                    message = new EmbedBuilder();
                    message.setColor(0xff73fa);
                    message.setTitle("Giveaways ohne Vorraussetzungen");
                    message.setDescription("> Du kannst an Givewaways nun teilnehmen, auch wenn die dafür benötigten Vorraussetzungen nicht erfüllst!");
                    message.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
                    event.replyEmbeds(Embeds.BANNER_BOOSTER_PERKS_GIVEAWAY, message.build()).addActionRow(Button.secondary("link", "Springe zu den Giveaways!").withUrl("https://discord.com/channels/873506353551925308/905384567077228586").withEmoji(Emoji.fromCustom("giveaway", 898562734680064082L, true))).setEphemeral(true).queue();
                    break;
            }
        }
    }
}