package de.splayfer.roonie.modules.response;

import com.vdurmont.emoji.EmojiParser;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.CustomEmoji;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.util.*;

public class ResponseAddCommand extends ListenerAdapter {

    HashMap<String, String> commandList = new HashMap<>();

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {

        if (event.getName().equals("response") && event.getSubcommandName().equals("add")) {

            String response = event.getOptionsByName("nachricht").get(0).getAsString().toLowerCase(Locale.ROOT);

            EmbedBuilder banner = new EmbedBuilder();
            banner.setColor(0xffffff);
            banner.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986195154954231868/banner_response.png");

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0xffffff);
            embedBuilder.setTitle(":mag: Wähle deine Aktion für **" + response + "**!");
            embedBuilder.setDescription("> Wähle im Menü unter dieser Nachricht aus, wie der Bot auf diese Nachricht reagieren soll!");
            embedBuilder.addField("Details", "<:text:886623802954498069> Ausgewählter Begriff: `" + response + "`", false);
            embedBuilder.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

            event.getChannel().sendTyping().queue();
            event.replyEmbeds(banner.build(), embedBuilder.build()).addActionRow(StringSelectMenu.create("response" + event.getOptionsByName("nachricht").get(0).getAsString())
                    .setRequiredRange(1, 1)
                    .setPlaceholder("Lege deine Aktion fest!")
                    .addOption("Nachricht senden", "message", "Sende eine Nachricht in den Chat!", Emoji.fromCustom(event.getJDA().getEmojiById("879356542791598160")))
                    .addOption("Reaktion hinzufügen", "reaction", "Füge eine Reaktion hinzu!", Emoji.fromCustom(event.getJDA().getEmojiById("894209540680187904")))
                    .addOption("Bild senden", "picture", "Poste ein Bild in den Chat!", Emoji.fromCustom(event.getJDA().getEmojiById("894211429094285354")))
                    .addOption("Link senden", "link", "Sende einen Link in den Chat!", Emoji.fromFormatted("\uD83D\uDD17"))
                    .build()).setEphemeral(true).complete();

        }

    }

    public void onStringSelectInteraction (StringSelectInteractionEvent event) {

        if (event.getSelectMenu().getId().startsWith("response")) {

            String message = event.getSelectMenu().getId().substring(8);

            TextInput body;
            Modal modal;

            switch (event.getValues().get(0)) {

                case "message":

                    body = TextInput.create("body", "Nachricht", TextInputStyle.PARAGRAPH)
                            .setPlaceholder("Gib die Nachricht an!")
                            .setMaxLength(88) // or setRequiredRange(10, 100)
                            .setRequired(true)
                            .build();

                    modal = Modal.create("response.msg" + message, "\uD83D\uDCE2〣Füge eine Nachricht hinzu")
                            .addActionRows(ActionRow.of(body))
                            .build();

                    event.replyModal(modal).queue();

                    break;

                case "reaction":

                    EmbedBuilder embed = new EmbedBuilder();
                    embed.setColor(0xed4245);
                    embed.setTitle(":exclamation: Schreibe die Emojis in deine Nachricht!");

                    event.replyEmbeds(embed.build()).setEphemeral(true).queue();

                    commandList.put(event.getMember().getId(), message);

                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {

                            if (commandList.containsKey(event.getMember().getId())) {

                                commandList.remove(event.getMember().getId());

                                EmbedBuilder embed = new EmbedBuilder();
                                embed.setColor(0xed4245);
                                embed.setTitle(":exclamation: Die Aktion wurde abgebrochen");

                                Message m = event.getChannel().sendMessageEmbeds(embed.build()).complete();

                                Timer t2 = new Timer();
                                t2.schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        m.delete().queue();

                                        t2.cancel();
                                    }
                                }, 8000);

                            }

                            t.cancel();
                        }
                    }, 20000);

                    break;

                case "picture":

                    body = TextInput.create("body", "Link zum Bild", TextInputStyle.PARAGRAPH)
                            .setPlaceholder("Gib den Link zum Bild an!")
                            .setMaxLength(88) // or setRequiredRange(10, 100)
                            .setRequired(true)
                            .build();

                    modal = Modal.create("response.pic" + message, "\uD83D\uDCE2〣Füge ein Bild hinzu")
                            .addActionRows(ActionRow.of(body))
                            .build();

                    event.replyModal(modal).queue();

                    break;

                case "link":

                    body = TextInput.create("body", "Link zum Url", TextInputStyle.PARAGRAPH)
                            .setPlaceholder("Gib den Link zum Url an!")
                            .setMaxLength(88) // or setRequiredRange(10, 100)
                            .setRequired(true)
                            .build();

                    modal = Modal.create("response.url" + message, "\uD83D\uDCE2〣Füge einen Link hinzu")
                            .addActionRows(ActionRow.of(body))
                            .build();

                    event.replyModal(modal).queue();

                    break;

            }

        }

    }

    public void onModalInteraction (ModalInteractionEvent event) {

        if (event.getModalId().startsWith("response.")) {

            ResponseManager.createResponse(event.getModalId().substring(12), event.getMember(), event.getModalId().substring(9, 12), event.getValue("body").getAsString());

            EmbedBuilder bannerEmbed = new EmbedBuilder();
            bannerEmbed.setColor(0x43b480);
            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

            EmbedBuilder reply = new EmbedBuilder();
            reply.setColor(0x43b480);
            reply.setTitle("✅ **RESPONSE ERFOLGREICH HINZUGEFÜGT!**");
            reply.setDescription("> Du hast die Response erfolgreich hinzugefügt!");
            reply.addField("<:text:886623802954498069> Ausgewählter Begriff", event.getModalId().substring(12), true);
            reply.addField("<:list:1002591375960842300> Reaktionsart", "**`" + event.getModalId().substring(9, 12) + "`**", true);
            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

            event.replyEmbeds(bannerEmbed.build(), reply.build()).setEphemeral(true).queue();

        }

    }

    public void onMessageReceived (MessageReceivedEvent event) {

        if (event.isFromGuild()) {

            if (commandList.containsKey(event.getMember().getId())) {

                String unicodeEmojis = EmojiParser.parseToUnicode(event.getMessage().getContentStripped());
                List<String> emojis = EmojiParser.extractEmojis(unicodeEmojis);

                List<CustomEmoji> rawEmotes = event.getMessage().getMentions().getCustomEmojis();

                List<String> emotes = new ArrayList<>();

                if (!(emojis.isEmpty() && rawEmotes.isEmpty())) {

                    for (CustomEmoji e : rawEmotes) {

                        emotes.add(e.getId());

                    }

                    emotes.addAll(emojis);

                    ResponseManager.createResponse(commandList.get(event.getMember().getId()), event.getMember(), "reaction", emotes.get(0));

                    EmbedBuilder bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0x43b480);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                    EmbedBuilder reply = new EmbedBuilder();
                    reply.setColor(0x43b480);
                    reply.setTitle("✅ **RESPONSE ERFOLGREICH HINZUGEFÜGT!**");
                    reply.setDescription("> Du hast die Response erfolgreich hinzugefügt!");
                    reply.addField("<:text:886623802954498069> Ausgewählter Begriff", commandList.get(event.getMember().getId()), false);
                    reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                    event.getChannel().sendMessageEmbeds(bannerEmbed.build(), reply.build()).queue();

                    commandList.remove(event.getMember().getId());

                }

            }

        }

    }

}
