package de.splayfer.roonie.giveaway;

import de.splayfer.roonie.messages.DefaultMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class GiveawayCreateCommand extends ListenerAdapter {

    static HashMap<Member, InteractionHook> interactionList = new HashMap<>();
    static List<Member> reqList = new ArrayList<>();

    static HashMap<Member, Long> timeoutList = new HashMap<>();

    public static HashMap<String, String> unixCode = new HashMap<>(){{
        put("format1", "<t: :d>");
        put("format2", "<t: :R>");
        put("format3", "<t: :t>");
        put("format4", "<t: :T>");
        put("format5", "<t: :f>");
        put("format6", "<t: :D>");
    }};

    protected LocalDateTime time;

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {

        if (event.getName().equals("giveaway") && event.getSubcommandName().equals("create")) {

            if (!Giveaway.existsGiveaway(event.getMember())) {

                Giveaway giveaway = Giveaway.create(event.getMember());

                EmbedBuilder banner = new EmbedBuilder();
                banner.setColor(0xeed147);
                banner.setImage("https://cdn.discordapp.com/attachments/985551183479463998/1012676337359650866/banner_giveaway.png");

                EmbedBuilder main = new EmbedBuilder();
                main.setColor(0xeed147);
                main.setTitle("<a:giveaway:898562734680064082> **GIVEAWAY ERSTELLEN**");
                main.setDescription("> Dein Giveaway besitzt momentan die folgenden Details");
                main.addField("Kanal", "**´NICHT ANGEGEBEN´**", false);

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(0xeed147);
                embedBuilder.setTitle(":tada: Gib den Kanal für das Giveaway an!");
                embedBuilder.setDescription("Gib zuerst an, in welchem Textkanal des Servers das Giveaway starten soll!");

                event.getChannel().sendTyping().queue();
                event.replyEmbeds(embedBuilder.build()).setEphemeral(true).queue();

                interactionList.put(event.getMember(), event.getHook());
                checkTimeout(event.getMember());

            } else {

                //building embed

                EmbedBuilder banner = new EmbedBuilder();
                banner.setColor(0xed4245);
                banner.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");

                EmbedBuilder main = new EmbedBuilder();
                main.setColor(0xed4245);
                main.setTitle(":no_entry_sign: **BEREITS ERSTELLT**");
                main.setDescription("> Es scheint als hast du bereits mit dem Erstellen eines neuen Giveaways begonnen!");
                main.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                event.replyEmbeds(banner.build(), main.build()).setEphemeral(true).queue();

            }

        }

    }

    public void onMessageReceived(MessageReceivedEvent event) {

        if (Giveaway.existsGiveaway(event.getMember())) {

            if (!reqList.contains(event.getMember())) {

                String[] args = event.getMessage().getContentStripped().split(" ");

                EmbedBuilder main;
                List<Button> buttons;

                switch (Giveaway.getStep(event.getMember())) {

                    case 1:

                        if (!event.getMessage().getMentions().getChannels().isEmpty()) {

                            //wenn erwähnung

                            if (event.getMessage().getMentions().getChannels().size() == 1) {

                                if (event.getMessage().getMentions().getChannels().get(0).getType().isMessage()) {

                                    MessageChannel channel = (MessageChannel) event.getMessage().getMentions().getChannels().get(0);

                                    main = new EmbedBuilder();
                                    main.setColor(0xeed147);
                                    main.setTitle(":trophy: Welchen Preis/Gewinn soll es geben?");
                                    main.setDescription("Gib nun den Preis an, den der Gewinner des Gewinnspiels am Ende erhalten soll!");

                                    buttons = new ArrayList<>();
                                    buttons.add(Button.secondary("giveaway.setup.prize", "Leg ihn jetzt fest!").withEmoji(Emoji.fromFormatted("🏆")));

                                    event.getChannel().sendTyping().queue();
                                    interactionList.get(event.getMember()).sendMessageEmbeds(main.build()).addActionRow(buttons).setEphemeral(true).queue();
                                    checkTimeout(event.getMember());

                                    Giveaway.getFromMember(event.getMember()).setChannel(channel);

                                } else {

                                    event.getChannel().sendTyping().queue();
                                    interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Du musst einen Nachrichtenkanal angeben!")).setEphemeral(true).queue();

                                }

                            } else {

                                event.getChannel().sendTyping().queue();
                                interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Bitte gib nur einen Kanal an!")).setEphemeral(true).queue();

                            }

                        } else {

                            try {

                                Double d = Double.parseDouble(event.getMessage().getContentStripped());

                                //wenn ID

                                boolean checkID = false;

                                for (GuildChannel searchChannel : event.getGuild().getChannels()) {

                                    if (searchChannel.getId().equals(d)) {

                                        if (searchChannel.getType().isMessage()) {

                                            checkID = true;

                                        }

                                    }

                                }

                                if (checkID == true) {

                                    MessageChannel channel = (MessageChannel) event.getGuild().getGuildChannelById(String.valueOf(d));

                                    main = new EmbedBuilder();
                                    main.setColor(0xeed147);
                                    main.setTitle(":trophy: Welchen Preis/Gewinn soll es geben?");
                                    main.setDescription("Gib nun den Preis an, den der Gewinner des Gewinnspiels am Ende erhalten soll!");

                                    event.getChannel().sendTyping().queue();
                                    interactionList.get(event.getMember()).sendMessageEmbeds(main.build()).setEphemeral(true).queue();

                                    Giveaway.getFromMember(event.getMember()).setChannel(channel);
                                    checkTimeout(event.getMember());

                                } else {

                                    event.getChannel().sendTyping().queue();
                                    interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Bitte gib eine gültige ID an!")).setEphemeral(true).queue();

                                }

                            } catch (Exception exception) {

                                event.getChannel().sendTyping().queue();
                                interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Bitte gib den Namen oder die ID des Kanals an!")).setEphemeral(true).queue();

                            }

                        }

                        break;

                    case 2:

                    case 3:

                    case 4:

                    case 5:

                        //do nothing...

                        break;

                    case 6:

                        if (args.length == 1) {

                            try {

                                Double d = Double.parseDouble(args[0]);

                                //wenn Zahl genannt

                                Giveaway.getFromMember(event.getMember()).setAmount(d.intValue());
                                checkTimeout(event.getMember());

                                main = new EmbedBuilder();
                                main.setColor(0xeed147);
                                main.setTitle(":frame_photo: Welches Bild so angezeigt werden?");
                                main.setDescription("Gib an, welches Bild in der Giveaway Nachricht zu sehen sein soll! Füge dazu das Bild als Link oder Anhang hier ein!");

                                buttons = new ArrayList<>();
                                buttons.add(Button.secondary("giveaway.noimage", "Fahre ohne Bild fort!").withEmoji(Emoji.fromCustom("cancel", Long.parseLong("877158821779345428"), false)));

                                event.getChannel().sendTyping().queue();
                                interactionList.get(event.getMember()).sendMessageEmbeds(main.build()).setEphemeral(true).addActionRow(buttons).queue();

                            } catch (Exception exception) {

                                event.getChannel().sendTyping().queue();
                                interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Dies ist keine gültige Zahl!", "Gültiger Zahlenraum: `1 - 100`")).setEphemeral(true).queue();

                            }

                        } else {

                            event.getChannel().sendTyping().queue();
                            interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Bitte gib die Anzahl der Gewinner an!")).setEphemeral(true).queue();

                        }

                        break;

                    case 7:

                        if (!event.getMessage().getAttachments().isEmpty()) {

                            //wenn attachment

                            if (event.getMessage().getAttachments().get(0).isImage()) {

                                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE dd. MMM YYY / HH:mm");

                                Giveaway giveaway = Giveaway.getFromMember(event.getMember());
                                giveaway.setPicture(event.getMessage().getAttachments().get(0).getUrl());
                                checkTimeout(event.getMember());

                                main = new EmbedBuilder();
                                main.setColor(0x3aa65b);
                                main.setTitle(":tada:  Geschafft!");
                                main.setDescription("Das Giveaway mit den foldende Details wurde erfolgreich erstellt!");
                                main.addField("<:text:877158818088386580> Kanal", giveaway.getChannel().getAsMention(), true);
                                main.addField("<a:wettbewerb:898566916958978078> Preis / Gewinn", giveaway.getPrize(), true);
                                main.addField(":clock10: Dauer", time.format(dateTimeFormatter), false);
                                main.addField(":clock10: Format", "<t:" + giveaway.getDuration() + ":R>", true);
                                main.addField(":busts_in_silhouette: Anzahl der Gewinner", giveaway.getAmount().toString(), false);
                                main.addField(":link: Url des Bildes", "<:cancel:877158821779345428> " + giveaway.getPicture(), true);

                                event.getChannel().sendTyping().queue();
                                interactionList.get(event.getMember()).sendMessageEmbeds(main.build()).setEphemeral(true).queue();

                                timeoutList.remove(event.getMember());
                                startGiveaway(giveaway);
                                interactionList.remove(event.getMember());

                            } else {

                                event.getChannel().sendTyping().queue();
                                interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Du musst ein Bild oder einen Link an die Nachricht anhängen!")).setEphemeral(true).queue();

                            }

                        } else if (args.length == 1) {

                            //wenn link

                            try {

                                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE dd. MMM YYY / HH:mm");

                                Giveaway giveaway = Giveaway.getFromMember(event.getMember());
                                giveaway.setPicture(args[0]);
                                checkTimeout(event.getMember());

                                main = new EmbedBuilder();
                                main.setColor(0x3aa65b);
                                main.setTitle(":tada:  Geschafft!");
                                main.setDescription("Das Giveaway mit den foldende Details wurde erfolgreich erstellt!");
                                main.addField("<:text:877158818088386580> Kanal", giveaway.getChannel().getAsMention(), true);
                                main.addField("<a:wettbewerb:898566916958978078> Preis / Gewinn", giveaway.getPrize(), true);
                                main.addField(":clock10: Dauer", time.format(dateTimeFormatter), false);
                                main.addField(":clock10: Format", "<t:" + giveaway.getDuration() + ":R>", true);
                                main.addField(":busts_in_silhouette: Anzahl der Gewinner", giveaway.getAmount().toString(), false);
                                main.addField(":link: Url des Bildes", "<:cancel:877158821779345428> " + giveaway.getPicture(), true);

                                event.getChannel().sendTyping().queue();
                                interactionList.get(event.getMember()).sendMessageEmbeds(main.build()).setEphemeral(true).queue();

                                timeoutList.remove(event.getMember());
                                startGiveaway(giveaway);
                                interactionList.remove(event.getMember());

                            } catch (Exception exception) {

                                event.getChannel().sendTyping().queue();
                                interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Bitte gib einen :link: Link an!")).setEphemeral(true).queue();

                            }

                        } else {

                            event.getChannel().sendTyping().queue();
                            interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Bitte gib einen :link: Link an!")).setEphemeral(true).queue();

                        }

                        break;

                }

            } else {

                EmbedBuilder main;

                String[] args = event.getMessage().getContentStripped().split(" ");

                Giveaway giveaway = Giveaway.getFromMember(event.getMember());

                switch (giveaway.getRequirement().keySet().toArray()[0].toString()) {

                    case "message":

                        if (args.length == 1) {

                            try {

                                Double d = Double.parseDouble(args[0]);

                                main = new EmbedBuilder();
                                main.setColor(0xeed147);
                                main.setTitle("\uD83D\uDC65 Wieviele Gewinner soll es geben?");
                                main.setDescription("Gib die Anzahl der Nutzer an, die den angebenen Preis gewinnen können!");

                                event.getChannel().sendTyping().queue();
                                interactionList.get(event.getMember()).sendMessageEmbeds(main.build()).setEphemeral(true).queue();

                                reqList.remove(event.getMember());
                                giveaway.setRequirement(new HashMap<String, String>(){{put(giveaway.getRequirement().keySet().toArray()[0].toString(), args[0]);}});
                                checkTimeout(event.getMember());

                            } catch (Exception exception) {

                                event.getChannel().sendTyping().queue();
                                interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Du musst eine Zahl angeben!")).setEphemeral(true).queue();

                            }

                        } else {

                            event.getChannel().sendTyping().queue();
                            interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Du musst eine Zahl angeben!")).setEphemeral(true).queue();

                        }

                        break;

                    case "voice":

                        if (args.length == 1) {

                            try {

                                Double d = Double.parseDouble(args[0]);

                                main = new EmbedBuilder();
                                main.setColor(0xeed147);
                                main.setTitle("\uD83D\uDC65 Wieviele Gewinner soll es geben?");
                                main.setDescription("Gib die Anzahl der Nutzer an, die den angebenen Preis gewinnen können!");

                                event.getChannel().sendTyping().queue();
                                interactionList.get(event.getMember()).sendMessageEmbeds(main.build()).setEphemeral(true).queue();

                                reqList.remove(event.getMember());
                                giveaway.setRequirement(new HashMap<String, String>(){{put(giveaway.getRequirement().keySet().toArray()[0].toString(), args[0]);}});
                                checkTimeout(event.getMember());

                            } catch (Exception exception) {

                                event.getChannel().sendTyping().queue();
                                interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Du musst eine Zahl angeben!")).setEphemeral(true).queue();

                            }

                        } else {

                            event.getChannel().sendTyping().queue();
                            interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Du musst eine Zahl angeben!")).setEphemeral(true).queue();

                        }

                        break;

                    case "invite":

                        if (args.length == 1) {

                            try {

                                Double d = Double.parseDouble(args[0]);

                                main = new EmbedBuilder();
                                main.setColor(0xeed147);
                                main.setTitle("\uD83D\uDC65 Wieviele Gewinner soll es geben?");
                                main.setDescription("Gib die Anzahl der Nutzer an, die den angebenen Preis gewinnen können!");

                                event.getChannel().sendTyping().queue();
                                interactionList.get(event.getMember()).sendMessageEmbeds(main.build()).setEphemeral(true).queue();

                                reqList.remove(event.getMember());
                                giveaway.setRequirement(new HashMap<String, String>(){{put(giveaway.getRequirement().keySet().toArray()[0].toString(), args[0]);}});
                                checkTimeout(event.getMember());

                            } catch (Exception exception) {

                                event.getChannel().sendTyping().queue();
                                interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Du musst eine Zahl angeben!")).setEphemeral(true).queue();

                            }

                        } else {

                            event.getChannel().sendTyping().queue();
                            interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Du musst eine Zahl angeben!")).setEphemeral(true).queue();

                        }

                        break;

                    case "role":

                        if (args.length == 1) {

                            if (!event.getMessage().getMentions().getRoles().isEmpty()) {

                                //Rolle per mention

                                if (event.getMessage().getMentions().getRoles().size() == 1) {

                                    main = new EmbedBuilder();
                                    main.setColor(0xeed147);
                                    main.setTitle("\uD83D\uDC65 Wieviele Gewinner soll es geben?");
                                    main.setDescription("Gib die Anzahl der Nutzer an, die den angebenen Preis gewinnen können!");

                                    event.getChannel().sendTyping().queue();
                                    interactionList.get(event.getMember()).sendMessageEmbeds(main.build()).setEphemeral(true).queue();

                                    reqList.remove(event.getMember());
                                    giveaway.setRequirement(new HashMap<String, String>(){{put(giveaway.getRequirement().keySet().toArray()[0].toString(), event.getMessage().getMentions().getRoles().get(0).getId());}});
                                    checkTimeout(event.getMember());

                                } else {

                                    event.getChannel().sendTyping().queue();
                                    interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Du kannst maximal eine Rolle erwähnen!")).setEphemeral(true).queue();

                                }

                            } else {

                                //Rolle per ID

                                boolean checkRole = false;

                                for (Role r : event.getGuild().getRoles()) {

                                    if (r.getId().equals(args[0])) {

                                        checkRole = true;

                                    }

                                }

                                if (checkRole = true) {

                                    main = new EmbedBuilder();
                                    main.setColor(0xeed147);
                                    main.setTitle("\uD83D\uDC65 Wieviele Gewinner soll es geben?");
                                    main.setDescription("Gib die Anzahl der Nutzer an, die den angebenen Preis gewinnen können!");

                                    event.getChannel().sendTyping().queue();
                                    interactionList.get(event.getMember()).sendMessageEmbeds(main.build()).setEphemeral(true).queue();

                                    reqList.remove(event.getMember());
                                    giveaway.setRequirement(new HashMap<String, String>(){{put(giveaway.getRequirement().keySet().toArray()[0].toString(), args[0]);}});
                                    checkTimeout(event.getMember());

                                } else {

                                    event.getChannel().sendTyping().queue();
                                    interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Dies ist keine gültige ID!")).setEphemeral(true).queue();

                                }

                            }

                        } else {

                            event.getChannel().sendTyping().queue();
                            interactionList.get(event.getMember()).sendMessageEmbeds(DefaultMessage.error("Gib eine Rolle oder eine ID an!")).setEphemeral(true).queue();

                        }

                        break;

                }

            }

        }

    }


    public void onSelectMenuInteraction (SelectMenuInteractionEvent event) {

        if (event.getSelectMenu().getId().equals("giveaway.setup.selectFormat")) {

            if (Giveaway.existsGiveaway(event.getMember())) {

                if (Giveaway.getStep(event.getMember()) == 4) {

                    Giveaway.getFromMember(event.getMember()).setTimeFormat(event.getValues().get(0));
                    checkTimeout(event.getMember());

                    EmbedBuilder success = new EmbedBuilder();
                    success.setColor(0xeed147);
                    success.setTitle(":lock: Welche Bedinungn soll gelten?");
                    success.setDescription("Gib an, welche Bedinungen die Nutzer erfüllen müssen, um am Giveaway teilnehmen zu können!");

                    event.getChannel().sendTyping().queue();
                    event.replyEmbeds(success.build()).setEphemeral(true).addActionRow(SelectMenu.create("giveaway.selectBedingung")

                            .setPlaceholder("Setze eine Bedingung für das Giveaway!")
                            .addOption("Keine Bedingung", "none", "Fahre ohne Bedingung fort!", Emoji.fromCustom("cancel", Long.parseLong("877158821779345428"), false))
                            .addOption("Nachrichten", "message", "Mindestanzahl an Nachrichten im Chat", Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))
                            .addOption("Sprachzeit", "voice", "Mindestdauer an Minuten im Voice", Emoji.fromCustom("voice", Long.parseLong("877158818189033502"), false))
                            .addOption("Einladungen", "invite", "Mindestanzahl an Servereinladungen", Emoji.fromFormatted("\uD83D\uDD17"))
                            .addOption("Rolle", "role", "Erforderliche Rolle", Emoji.fromCustom("role", Long.parseLong("913493284855382076"), false))

                            .build()).queue();

                    interactionList.put(event.getMember(), event.getHook());
                }
            }

        } else if (event.getSelectMenu().getId().equals("giveaway.selectBedingung")) {

            if (Giveaway.existsGiveaway(event.getMember())) {

                if (Giveaway.getStep(event.getMember()) == 5) {

                    if (event.getValues().get(0).equals("none")) {

                        Giveaway.getFromMember(event.getMember()).setRequirement(new HashMap<>(){{put(event.getValues().get(0), null);}});
                        checkTimeout(event.getMember());

                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0xeed147);
                        success.setTitle("\uD83D\uDC65 Wieviele Gewinner soll es geben?");
                        success.setDescription("Gib die Anzahl der Nutzer an, die den angebenen Preis gewinnen können!");

                        event.getChannel().sendTyping().queue();
                        event.replyEmbeds(success.build()).setEphemeral(true).queue();

                        interactionList.put(event.getMember(), event.getHook());

                    } else {

                        EmbedBuilder success;

                        switch (event.getValues().get(0)) {

                            case "message":

                                success = new EmbedBuilder();
                                success.setColor(0xeed147);
                                success.setTitle(":speech_balloon: Wieviele Nachrichten sollen benötigt werden?");
                                success.setDescription("Gib an, wieviele Nachrichten ein Nutzer benötigt, um an dem Giveaway teilnehmen zu können!");

                                event.getChannel().sendTyping().queue();
                                event.replyEmbeds(success.build()).queue();

                                Giveaway.getFromMember(event.getMember()).setRequirement(new HashMap<>(){{put(event.getValues().get(0), null);}});
                                checkTimeout(event.getMember());
                                reqList.add(event.getMember());

                                interactionList.put(event.getMember(), event.getHook());

                                break;

                            case "voice":

                                success = new EmbedBuilder();
                                success.setColor(0xeed147);
                                success.setTitle(":loud_sound: Wieviele Minuten im Voice sollen benötigt werden?");
                                success.setDescription("Gib an, wieviele Minuten lang ein Nutzer in einem Sprachkanal gewesen sein muss, um an dem Giveaway teilnehmen zu können!");

                                event.getChannel().sendTyping().queue();
                                event.replyEmbeds(success.build()).queue();

                                Giveaway.getFromMember(event.getMember()).setRequirement(new HashMap<>(){{put(event.getValues().get(0), null);}});
                                checkTimeout(event.getMember());
                                reqList.add(event.getMember());

                                interactionList.put(event.getMember(), event.getHook());

                                break;

                            case "invite":

                                success = new EmbedBuilder();
                                success.setColor(0xeed147);
                                success.setTitle(":link: Wieviele Einladungen sollen benötigt werden?");
                                success.setDescription("Gib an, wieviele Servereinladungen ein Nutzer benötigt, um an dem Giveaway teilnehmen zu können!");

                                event.getChannel().sendTyping().queue();
                                event.replyEmbeds(success.build()).queue();

                                Giveaway.getFromMember(event.getMember()).setRequirement(new HashMap<>(){{put(event.getValues().get(0), null);}});
                                checkTimeout(event.getMember());
                                reqList.add(event.getMember());

                                interactionList.put(event.getMember(), event.getHook());

                                break;

                            case "role":

                                success = new EmbedBuilder();
                                success.setColor(0xeed147);
                                success.setTitle(":busts_in_silhouette: Welche Rolle soll benötigt werden?");
                                success.setDescription("Gib an, welche Rolle der Nutzer besitzten muss, um an dem Giveaway teilnehmen zu können!");

                                event.getChannel().sendTyping().queue();
                                event.replyEmbeds(success.build()).queue();

                                Giveaway.getFromMember(event.getMember()).setRequirement(new HashMap<>(){{put(event.getValues().get(0), null);}});
                                checkTimeout(event.getMember());
                                reqList.add(event.getMember());

                                interactionList.put(event.getMember(), event.getHook());

                                break;

                        }

                    }

                }

            }

        }

    }

    public void onButtonInteraction (ButtonInteractionEvent event) {

        if (Giveaway.existsGiveaway(event.getMember())) {

            switch (event.getButton().getId()) {

                case "giveaway.noimage":

                    if (Giveaway.getStep(event.getMember()) == 7) {

                        Giveaway.getFromMember(event.getMember()).setPicture("NONE");
                        checkTimeout(event.getMember());

                        //wenn kein Bild vorhanden!

                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("EEEE dd. MMM YYY / HH:mm");

                        Giveaway giveaway = Giveaway.getFromMember(event.getMember());

                        EmbedBuilder success = new EmbedBuilder();
                        success.setColor(0x3aa65b);
                        success.setTitle(":tada:  Geschafft!");
                        success.setDescription("Das Giveaway mit den foldende Details wurde erfolgreich erstellt!");
                        success.addField("<:text:877158818088386580> Kanal", giveaway.getChannel().getAsMention(), true);
                        success.addField("<a:wettbewerb:898566916958978078> Preis / Gewinn", giveaway.getPrize(), true);
                        success.addField(":clock10: Dauer", time.format(dateTimeFormatter), false);
                        success.addField(":clock10: Format", "<t:" +giveaway.getDuration() + ":R>", true);
                        success.addField(":busts_in_silhouette: Anzahl der Gewinner", giveaway.getAmount().toString(), true);
                        success.addField(":link: Url des Bildes", "<:cancel:877158821779345428> KEIN BILD", false);

                        event.getChannel().sendTyping().queue();
                        event.replyEmbeds(success.build()).setEphemeral(true).queue();

                        timeoutList.remove(event.getMember());
                        startGiveaway(giveaway);
                        interactionList.remove(event.getMember());

                    }

                    break;

                case "giveaway.setup.prize":

                    if (Giveaway.getStep(event.getMember()) == 2) {

                        TextInput prize = TextInput.create("prize", "Preis/Gewinn", TextInputStyle.SHORT)
                                .setPlaceholder("Gib den Preis an!")
                                .setMinLength(1)
                                .setMaxLength(50) // or setRequiredRange(10, 100)
                                .build();

                        Modal modal = Modal.create("giveaway.setup.prize", "\uD83C\uDFC6〣Gib den Preis an!")
                                .addActionRows(ActionRow.of(prize))
                                .build();

                        event.replyModal(modal).queue();

                    }

                    break;

                case "giveaway.setup.duration":

                    if (Giveaway.getStep(event.getMember()) == 3) {

                        TextInput duration = TextInput.create("duration", "Dauer des Giveaways", TextInputStyle.SHORT)
                                .setPlaceholder("Gib die Dauer an!")
                                .setMinLength(1)
                                .setMaxLength(50) // or setRequiredRange(10, 100)
                                .build();

                        Modal modal = Modal.create("giveaway.setup.duration", "\uD83D\uDD53〣Gib die Dauer an!")
                                .addActionRows(ActionRow.of(duration))
                                .build();

                        event.replyModal(modal).queue();

                    }

                    break;

            }

        }

    }

    public void onModalInteraction(ModalInteractionEvent event) {

        if (Giveaway.existsGiveaway(event.getMember())) {

            EmbedBuilder main;
            List<Button> buttons;

            switch (event.getModalId()) {

                case "giveaway.setup.prize":

                    main = new EmbedBuilder();
                    main.setColor(0xeed147);
                    main.setTitle(":clock10: Wie lange soll das Giveaway dauern?");
                    main.setDescription("Gib an, wie lange das Giveaway, das du starten willst, dauern soll!");
                    main.addField("Beispiel zum Format", "5d 3h 43m", false);

                    buttons = new ArrayList<>();
                    buttons.add(Button.secondary("giveaway.setup.duration" ,"Leg die Dauer fest!").withEmoji(Emoji.fromFormatted("\uD83D\uDD53")));

                    event.getChannel().sendTyping().queue();
                    event.replyEmbeds(main.build()).addActionRow(buttons).setEphemeral(true).queue();

                    Giveaway.getFromMember(event.getMember()).setPrize(event.getValue("prize").getAsString());
                    checkTimeout(event.getMember());
                    interactionList.put(event.getMember(), event.getHook());

                    break;

                case "giveaway.setup.duration":

                    time = LocalDateTime.now();

                    for (String s : event.getValue("duration").getAsString().toLowerCase(Locale.ROOT).split(" ")) {

                        if (s.contains("d") || s.contains("h") || s.contains("m")) {

                            long l = Long.parseLong(s.substring(0, s.length() - 1));
                            if (s.contains("d")) {

                                time = time.plusDays(l);

                            } else if (s.contains("h")) {

                                time = time.plusHours(l);

                            } else if (s.contains("m")) {

                                time = time.plusMinutes(l);

                            }

                        } else {

                            //building Embed

                            EmbedBuilder error = new EmbedBuilder();
                            error.setTitle(":exclamation: **Bitte gib ein gültiges :clock10: Format an!**");
                            error.addField("Hilfe zum Format", "`d` = Tag \n" +
                                    "`h` = Stunden \n" +
                                    "`m` = Minuten", true);
                            error.addField("Beispiel", "5d 3h 43m", true);
                            error.setColor(0xc01c34);

                            buttons = new ArrayList<>();
                            buttons.add(Button.secondary("giveaway.setup.duration", "Versuch es nochmal!").withEmoji(Emoji.fromCustom("undo", 878590238782550076L, false)));

                            event.getChannel().sendTyping().queue();
                            event.replyEmbeds(error.build()).addActionRow(buttons).setEphemeral(true).queue();

                            interactionList.put(event.getMember(), event.getHook());

                        }

                    }

                    time = time.minusHours(1);

                    long unix = time.toEpochSecond(ZoneOffset.UTC);

                    event.getChannel().sendMessage("<t:" + unix + ":R>").queue();

                    main = new EmbedBuilder();
                    main.setColor(0xeed147);
                    main.setTitle(":clock10: In welchem Format soll die Zeit angezeigt werden?");
                    main.setDescription("Gib an, wie das Zeit Limit unter der Giveaway Benachrichtigung für die Nutzer sichtbar sein soll!");

                    event.getChannel().sendTyping().queue();
                    event.replyEmbeds(main.build()).setEphemeral(true).addActionRow(SelectMenu.create("giveaway.setup.selectFormat")

                            .setPlaceholder("Lege das Format der Zeit fest!")
                            .addOption("08.09.2021", "format1", "Klicke hier, um dieses Format zu wählen!", Emoji.fromFormatted("\uD83D\uDD59"))
                            .addOption("in 3 Tagen", "format2", "Klicke hier, um dieses Format zu wählen!", Emoji.fromFormatted("\uD83D\uDD59"))
                            .addOption("14:57", "format3", "Klicke hier, um dieses Format zu wählen!", Emoji.fromFormatted("\uD83D\uDD59"))
                            .addOption("14:57:00", "format4", "Klicke hier, um dieses Format zu wählen!", Emoji.fromFormatted("\uD83D\uDD59"))
                            .addOption("8. September 2021 14:57", "format5", "Klicke hier, um dieses Format zu wählen!", Emoji.fromFormatted("\uD83D\uDD59"))
                            .addOption("Mittwoch, 8. September", "format6", "Klicke hier, um dieses Format zu wählen!", Emoji.fromFormatted("\uD83D\uDD59"))
                            .addOption("8. September 2021", "format7", "Klicke hier, um dieses Format zu wählen!", Emoji.fromFormatted("\uD83D\uDD59"))

                            .build()).queue();

                    Giveaway.getFromMember(event.getMember()).setDuration(unix);
                    checkTimeout(event.getMember());
                    interactionList.put(event.getMember(), event.getHook());

                    break;

            }

        }

    }

    public static void startGiveaway(Giveaway giveaway) {

        MessageChannel channel = giveaway.getChannel();

        EmbedBuilder bannerEmbed = new EmbedBuilder();
        bannerEmbed.setColor(0x28346d);
        bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/913825741588803635/banner_giveaway.png");

        EmbedBuilder main = new EmbedBuilder();
        main.setColor(0x28346d);
        main.setTitle(":tada: Giveaway: " + giveaway.getPrize());
        main.setDescription("Ein weiteres Giveaway ist erschienen, an dem du nun teilnehmen kannst!");
        main.setThumbnail("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
        main.addField("<a:wettbewerb:898566916958978078> Preis", giveaway.getPrize(), true);
        main.addField(":clock10: Dauer", unixCode.get(giveaway.getTimeFormat()).split(" ")[0] + giveaway.getDuration() + unixCode.get(giveaway.getTimeFormat()).split(" ")[1], true);
        main.addField(":busts_in_silhouette: Anzahl der Gewinner", giveaway.getAmount().toString(), true);

        String requirement = "";
        for (String s : giveaway.getRequirement().keySet()) {
            requirement = s;
        }

        switch (requirement) {
            case "none" ->
                    main.addField("<:text:886623802954498069> Bedingung", "<:cancel:877158821779345428> **KEINE**", true);
            case "message" ->
                    main.addField("<:text:886623802954498069> Bedingung", "<a:chat:879356542791598160> Nachrichten: **" + giveaway.getRequirement().get(giveaway.getRequirement()) + "**", true);
            case "voice" ->
                    main.addField("<:text:886623802954498069> Bedingung", "<:voice:877158818189033502> Sprachminuten: **" + giveaway.getRequirement().get(giveaway.getRequirement()) + "**", true);
            case "invite" ->
                    main.addField("<:text:886623802954498069> Bedingung", ":link: Servereinladungen: **" + giveaway.getRequirement().get(giveaway.getRequirement()) + "**", true);
            case "role" ->
                    main.addField("<:text:886623802954498069> Bedingung", "<:role:913493284855382076> Rolle: **" + giveaway.getRequirement().get(giveaway.getRequirement()) + "**", true);
        }

        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.primary("giveaway.enter", "Nimm jetzt am Giveaway teil!").withEmoji(Emoji.fromCustom("giveaway", Long.parseLong("898562734680064082"), true)));

        channel.sendTyping().queue();
        Message m = channel.sendMessageEmbeds(bannerEmbed.build(), main.build()).setActionRow(buttons).complete();

        //sync with MySQL

        giveaway.setMessage(m);
        giveaway.insertToMySQL();

    }

    public static void checkTimeout(Member member) {

        long millis = System.currentTimeMillis();
        timeoutList.put(member, millis);

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {

                if (timeoutList.containsKey(member) && timeoutList.get(member).equals(millis)) {

                    EmbedBuilder bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0xed4245);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/985551183479463998/1012355494238761030/banner_timeout.png");

                    EmbedBuilder reply = new EmbedBuilder();
                    reply.setColor(0xed4245);
                    reply.setTitle(":no_entry_sign: **ZEIT ABGELAUFEN**");
                    reply.setDescription("> Das Erstellen des Giveaways wurde wegen Inaktivität abgebrochen!");
                    reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                    interactionList.get(member).sendMessageEmbeds(bannerEmbed.build(), reply.build()).setEphemeral(true).queue();

                    Giveaway.getFromMember(member).delete(member);

                }

            }
        }, 1000 * 60 * 14);

    }

}
