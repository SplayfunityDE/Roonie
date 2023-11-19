package de.splayfer.roonie.poll;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.general.Roles;
import de.splayfer.roonie.messages.DefaultMessage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class PollCreateCommand extends ListenerAdapter {

    static HashMap<Member, InteractionHook> interactionmap = new HashMap<>();

    protected List<String> umfrageList = new ArrayList<>();

    SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd - HH:mm ");
    SimpleDateFormat formatterOnlyDate = new SimpleDateFormat("yyyy.MM.dd ");
    Date currentTime = new Date();

    public void onMessageReceived (MessageReceivedEvent event) {

        if (event.isFromGuild()) {

            TextChannel umfrageChannel = event.getGuild().getTextChannelById("880717025255751721");

            if (event.getMessage().getContentStripped().startsWith(Roonie.prefix + "umfrage create")) {

                if (event.getMember().getPermissions().contains(Permission.ADMINISTRATOR) || event.getMember().getRoles().contains(Roles.CONTENT.getRole(event.getGuild()))) {

                    String[] args = event.getMessage().getContentStripped().split(" ");

                    if (args.length == 2) {

                        EmbedBuilder step1 = new EmbedBuilder();
                        step1.setColor(0x3aa65b);
                        step1.setTitle(":bar_chart: **Gib den Kanal für die Umfragen an!**");

                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessageEmbeds(step1.build()).queue();

                    }

                }

            } else {

                if (Poll.existsPoll(event.getMember())) {

                    Member member = event.getMember();

                    EmbedBuilder step;
                    List<Button> buttons;
                    Poll poll = Poll.getFromMember(member);

                    switch (poll.getStep()) {

                        case 1:

                            MessageChannel channel = null;
                            if (event.getMessage().getMentions().getChannels() != null) {
                                if (event.getMessage().getMentions().getChannels().size() == 1) {
                                    if (event.getMessage().getMentions().getChannels().get(0).getType().isMessage()) {
                                        channel = (MessageChannel) event.getMessage().getMentions().getChannels().get(0);
                                    } else {
                                        interactionmap.get(member).sendMessageEmbeds(DefaultMessage.error("Du musst einen Nachrichtenkanal angeben!")).setEphemeral(true).queue();
                                    }
                                } else {
                                    interactionmap.get(member).sendMessageEmbeds(DefaultMessage.error("Du kannst nur einen Kanal erwähnen!")).setEphemeral(true).queue();
                                }
                            } else {
                                //id
                                try {
                                    double d = Double.parseDouble(event.getMessage().getContentStripped());
                                    GuildChannel guildChannel = event.getGuild().getGuildChannelById(Double.toString(d));
                                    if (guildChannel.getType().isMessage()) {
                                        channel = (MessageChannel) guildChannel;
                                    }

                                } catch (Exception e) {
                                    interactionmap.get(member).sendMessageEmbeds(DefaultMessage.error("Du musst den Kanal erwähnen oder seine ID angeben!")).setEphemeral(true).queue();
                                }
                            }

                            if (channel != null) {

                                step = new EmbedBuilder();
                                step.setColor(0x3aa65b);
                                step.setTitle(":bar_chart: **Gib das Thema der Umfrage an!**");

                                buttons = new ArrayList<>();
                                buttons.add(Button.secondary("poll.setup.topic", "Gib das Thema der Umfrage an!").withEmoji(Emoji.fromCustom("chat", 879356542791598160L, true)));

                                event.getChannel().sendTyping().queue();
                                interactionmap.get(member).sendMessageEmbeds(step.build()).setEphemeral(true).addActionRow(buttons).queue();

                                poll.setChannel(channel);

                            }

                            break;

                            //test

                        case 2:

                            break;

                    }

                    /*

                    switch (Poll.getFromMember(event.getMember()).getStep()) {

                        case 1:

                            thema.put(event.getMember().getId(), event.getMessage().getContentStripped());

                            EmbedBuilder step1 = new EmbedBuilder();
                            step1.setColor(0x3aa65b);
                            step1.setTitle(":bar_chart: **Gib die Beschreibung für diese Umfrage ein!**");

                            event.getChannel().sendTyping().queue();
                            event.getChannel().sendMessageEmbeds(step1.build()).queue();

                            stepInfo.put(event.getMember().getId(), "step2");


                            break;

                        case 2:

                            description.put(event.getMember().getId(), event.getMessage().getContentStripped());

                            EmbedBuilder step2 = new EmbedBuilder();
                            step2.setColor(0x3aa65b);
                            step2.setTitle(":bar_chart: **Gib den Inhalt des 1. Buttons an!**");

                            event.getChannel().sendTyping().queue();
                            event.getChannel().sendMessageEmbeds(step2.build()).queue();

                            stepInfo.put(event.getMember().getId(), "step3");

                            break;

                        case 3:

                            if (!(event.getMessage().getContentStripped().length() > 25)) {

                                buttontrue.put(event.getMember().getId(), event.getMessage().getContentStripped());

                                EmbedBuilder step3 = new EmbedBuilder();
                                step3.setColor(0x3aa65b);
                                step3.setTitle(":bar_chart: **Gib den Inhalt des 2. Buttons an!**");

                                event.getChannel().sendTyping().queue();
                                event.getChannel().sendMessageEmbeds(step3.build()).queue();

                                stepInfo.put(event.getMember().getId(), "step4");

                            } else {

                                //building Embed

                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle(":exclamation: **Der Inhalt des Buttons darf maximal 25 Zeichen besitzen!**");
                                embedBuilder.setColor(0xc01c34);

                                event.getChannel().sendTyping().queue();
                                Message m = event.getChannel().sendMessageEmbeds(embedBuilder.build()).complete();
                                embedBuilder.clear();

                                Timer t = new Timer();
                                t.schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        m.delete().queue();

                                        t.cancel();

                                    }
                                }, 8000);

                            }

                            break;

                        case 4:

                            if (!(event.getMessage().getContentStripped().length() > 25)) {

                                buttonnone.put(event.getMember().getId(), event.getMessage().getContentStripped());

                                EmbedBuilder step4 = new EmbedBuilder();
                                step4.setColor(0x3aa65b);
                                step4.setTitle(":bar_chart: **Gib den Inhalt des 3. Buttons an!**");

                                event.getChannel().sendTyping().queue();
                                event.getChannel().sendMessageEmbeds(step4.build()).queue();

                                stepInfo.put(event.getMember().getId(), "step5");

                            } else {

                                //building Embed

                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle(":exclamation: **Der Inhalt des Buttons darf maximal 25 Zeichen besitzen!**");
                                embedBuilder.setColor(0xc01c34);

                                event.getChannel().sendTyping().queue();
                                Message m = event.getChannel().sendMessageEmbeds(embedBuilder.build()).complete();
                                embedBuilder.clear();

                                Timer t = new Timer();
                                t.schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        m.delete().queue();

                                        t.cancel();

                                    }
                                }, 8000);

                            }

                            break;

                        case 5:


                            if (!(event.getMessage().getContentStripped().length() > 25)) {

                                buttonfalse.put(event.getMember().getId(), event.getMessage().getContentStripped());

                                EmbedBuilder step5 = new EmbedBuilder();
                                step5.setColor(0x3aa65b);
                                step5.setTitle(":bar_chart: **Sollen die Ergebnisse der Umfrage öffentlich sein?**");
                                step5.setDescription("Schreibe `JA` oder `NEIN` in den Chat!");

                                event.getChannel().sendTyping().queue();
                                event.getChannel().sendMessageEmbeds(step5.build()).queue();

                                stepInfo.put(event.getMember().getId(), "step6");

                            } else {

                                //building Embed

                                EmbedBuilder embedBuilder = new EmbedBuilder();
                                embedBuilder.setTitle(":exclamation: **Der Inhalt des Buttons darf maximal 25 Zeichen besitzen!**");
                                embedBuilder.setColor(0xc01c34);

                                event.getChannel().sendTyping().queue();
                                Message m = event.getChannel().sendMessageEmbeds(embedBuilder.build()).complete();
                                embedBuilder.clear();

                                Timer t = new Timer();
                                t.schedule(new TimerTask() {
                                    @Override
                                    public void run() {

                                        m.delete().queue();

                                        t.cancel();

                                    }
                                }, 8000);

                            }

                            break;

                        case 6:

                            EmbedBuilder step5;
                            EmbedBuilder bannerEmbed;
                            EmbedBuilder debug;
                            List<Button> buttons;
                            Message m;
                            EmbedBuilder information;

                            switch (event.getMessage().getContentStripped().toLowerCase(Locale.ROOT)) {

                                case "ja":

                                    showvoting.put(event.getMember().getId(), true);
                                    stepInfo.remove(event.getMember().getId());

                                    step5 = new EmbedBuilder();
                                    step5.setColor(0x3aa65b);
                                    step5.setTitle(":bar_chart: **Umfrage erfolgreich erstellt!**");
                                    step5.setDescription("Die Umfrage wurde mithilfe der angegebenen Daten erfolgreich in <#880717025255751721> gestartet!");
                                    step5.addField("Details zu dieser Aktion", "<:text:877158818088386580> Kanal: <#880717025255751721>\n" +
                                            "<:member:880022583947431947> Ersteller: `" + event.getMember().getUser().getAsTag() + "`\n" +
                                            "<a:chat:879356542791598160> Thema: `" + thema.get(event.getMember().getId()) + "`\n" +
                                            ":clock10: Uhrzeit: `" + formatter.format(currentTime) + "Uhr`", false);

                                    event.getChannel().sendTyping().queue();
                                    event.getChannel().sendMessageEmbeds(step5.build()).queue();

                                    //sending umfrage

                                    bannerEmbed = new EmbedBuilder();
                                    bannerEmbed.setColor(0x28346d);
                                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/910194455494144021/banner_umfrage.png");

                                    buttons = new ArrayList<>();

                                    buttons.add(Button.success("true", buttontrue.get(event.getMember().getId()) + " (0)").withEmoji(Emoji.fromCustom("voting_true", Long.parseLong("910240688690585630"), false)));
                                    buttons.add(Button.secondary("none", buttonnone.get(event.getMember().getId()) + " (0)").withEmoji(Emoji.fromCustom("voting_none", Long.parseLong("910240688489242675"), false)));
                                    buttons.add(Button.danger("false", buttonfalse.get(event.getMember().getId()) + " (0)").withEmoji(Emoji.fromCustom("voting_false", Long.parseLong("910240688627671040"), false)));

                                    information = new EmbedBuilder();
                                    information.setColor(0x28346d);
                                    information.setAuthor("Umfrage am " + formatterOnlyDate.format(currentTime), "https://discord.gg/GAPAnSxKbM", "https://cdn.discordapp.com/attachments/883278317753626655/910245826020921374/38bfa82363472a2ff8e43b6559b9a9a6.png");
                                    information.setTitle("Umfrage zum Thema " + thema.get(event.getMember().getId()));
                                    information.addField("Details", description.get(event.getMember().getId()), false);
                                    information.addField(":clock10: Dauer der Umfrage", "`UNBEGRENZT`", true);
                                    information.addField(":clock10: Erstellt am", formatter.format(currentTime), true);
                                    information.addField("Klicke auf einen der unteren Button um abzustimmen!", "", false);
                                    information.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                                    umfrageChannel.sendTyping().queue();
                                    m = umfrageChannel.sendMessageEmbeds(bannerEmbed.build(), information.build()).setActionRow(buttons).complete();

                                    //save to yml

                                    yml.set(m.getId() + ".thema", thema.get(event.getMember().getId()));
                                    yml.set(m.getId() + ".description", description.get(event.getMember().getId()));
                                    yml.set(m.getId() + ".buttontrue", buttontrue.get(event.getMember().getId()));
                                    yml.set(m.getId() + ".buttonnone", buttonnone.get(event.getMember().getId()));
                                    yml.set(m.getId() + ".buttonfalse", buttonfalse.get(event.getMember().getId()));
                                    yml.set(m.getId() + ".showvoting", showvoting.get(event.getMember().getId()));

                                    try {
                                        yml.save(umfrageLog);
                                    } catch (IOException exception) {
                                        exception.printStackTrace();
                                    }

                                    thema.remove(event.getMember().getId());
                                    description.remove(event.getMember().getId());
                                    buttontrue.remove(event.getMember().getId());
                                    buttonnone.remove(event.getMember().getId());
                                    buttonfalse.remove(event.getMember().getId());
                                    showvoting.remove(event.getMember().getId());

                                    break;

                                case "nein":

                                    showvoting.put(event.getMember().getId(), false);
                                    stepInfo.remove(event.getMember().getId());

                                    step5 = new EmbedBuilder();
                                    step5.setColor(0x3aa65b);
                                    step5.setTitle(":bar_chart: **Umfrage erfolgreich erstellt!**");
                                    step5.setDescription("Die Umfrage wurde mithilfe der angegebenen Daten erfolgreich in <#880717025255751721> gestartet!");
                                    step5.addField("Details zu dieser Aktion", "<:text:877158818088386580> Kanal: <#880717025255751721>\n" +
                                            "<:member:880022583947431947> Ersteller: `" + event.getMember().getUser().getAsTag() + "`\n" +
                                            "<a:chat:879356542791598160> Thema: `" + thema.get(event.getMember().getId()) + "`\n" +
                                            ":clock10: Uhrzeit: `" + formatter.format(currentTime) + "Uhr`", false);

                                    event.getChannel().sendTyping().queue();
                                    event.getChannel().sendMessageEmbeds(step5.build()).queue();

                                    //sending umfrage

                                    bannerEmbed = new EmbedBuilder();
                                    bannerEmbed.setColor(0x28346d);
                                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/910194455494144021/banner_umfrage.png");

                                    buttons = new ArrayList<>();

                                    buttons.add(Button.success("true", buttontrue.get(event.getMember().getId())).withEmoji(Emoji.fromCustom("voting_true", Long.parseLong("910240688690585630"), false)));
                                    buttons.add(Button.secondary("none", buttonnone.get(event.getMember().getId())).withEmoji(Emoji.fromCustom("voting_none", Long.parseLong("910240688489242675"), false)));
                                    buttons.add(Button.danger("false", buttonfalse.get(event.getMember().getId())).withEmoji(Emoji.fromCustom("voting_false", Long.parseLong("910240688627671040"), false)));

                                    information = new EmbedBuilder();
                                    information.setColor(0x3aa65b);
                                    information.setAuthor("Umfrage am " + formatterOnlyDate.format(currentTime), "https://discord.gg/GAPAnSxKbM", "https://cdn.discordapp.com/attachments/883278317753626655/910245826020921374/38bfa82363472a2ff8e43b6559b9a9a6.png");
                                    information.setTitle("Umfrage zum Thema " + thema.get(event.getMember().getId()));
                                    information.addField("Details", description.get(event.getMember().getId()), false);
                                    information.addField(":clock10: Dauer der Umfrage", "`UNBEGRENZT`", true);
                                    information.addField(":clock10: Erstellt am", formatter.format(currentTime), true);
                                    information.addField("Klicke auf einen der unteren Button um abzustimmen!", "", false);
                                    information.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                                    umfrageChannel.sendTyping().queue();
                                    m = umfrageChannel.sendMessageEmbeds(bannerEmbed.build(), information.build()).setActionRow(buttons).complete();

                                    //save to yml

                                    yml.set(m.getId() + ".thema", thema.get(event.getMember().getId()));
                                    yml.set(m.getId() + ".description", description.get(event.getMember().getId()));
                                    yml.set(m.getId() + ".buttontrue", buttontrue.get(event.getMember().getId()));
                                    yml.set(m.getId() + ".buttonnone", buttonnone.get(event.getMember().getId()));
                                    yml.set(m.getId() + ".buttonfalse", buttonfalse.get(event.getMember().getId()));
                                    yml.set(m.getId() + ".showvoting", showvoting.get(event.getMember().getId()));

                                    try {
                                        yml.save(umfrageLog);
                                    } catch (IOException exception) {
                                        exception.printStackTrace();
                                    }

                                    thema.remove(event.getMember().getId());
                                    description.remove(event.getMember().getId());
                                    buttontrue.remove(event.getMember().getId());
                                    buttonnone.remove(event.getMember().getId());
                                    buttonfalse.remove(event.getMember().getId());
                                    showvoting.remove(event.getMember().getId());

                                    break;

                                default:

                                    //building Embed

                                    EmbedBuilder embedBuilder = new EmbedBuilder();
                                    embedBuilder.setTitle(":exclamation: **Du musst `JA` oder `NEIN` angeben!**");
                                    embedBuilder.setColor(0xc01c34);

                                    event.getChannel().sendTyping().queue();
                                    Message m2 = event.getChannel().sendMessageEmbeds(embedBuilder.build()).complete();
                                    embedBuilder.clear();

                                    Timer t = new Timer();
                                    t.schedule(new TimerTask() {
                                        @Override
                                        public void run() {

                                            m2.delete().queue();

                                            t.cancel();

                                        }
                                    }, 8000);

                                    break;

                            }

                            break;

                        default:

                            if (Poll.getFromMember(event.getMember()).getStep() >= 7) {


                            }

                            break;

                    }

                     */



                }

            }

        }



    }

    public void onButtonInteraction(ButtonInteractionEvent event) {

        if (Poll.existsPoll(event.getMember())) {
            if (event.getButton().getId().startsWith("poll.setup")) {

                Member member = event.getMember();
                Poll poll = Poll.getFromMember(member);

                switch (event.getButton().getId().split("\\.")[2]) {

                    case "topic":

                        if (poll.getStep() == 2) {

                            TextInput topic = TextInput.create("topic", "Thema der Umfrage", TextInputStyle.SHORT)
                                    .setPlaceholder("Gib das Thema an!")
                                    .setMinLength(1)
                                    .setMaxLength(50) // or setRequiredRange(10, 100)
                                    .build();

                            Modal modal = Modal.create("poll.setup.topic", "\uD83D\uDCDC〣Gib das Thema an!")
                                    .addActionRows(ActionRow.of(topic))
                                    .build();

                            event.replyModal(modal).queue();

                        }

                        break;

                    case "description":

                        if (poll.getStep() == 3) {

                            TextInput description = TextInput.create("description", "Beschreibung der Umfrage", TextInputStyle.SHORT)
                                    .setPlaceholder("Gib die Beschreibung an!")
                                    .setMinLength(1)
                                    .setMaxLength(50) // or setRequiredRange(10, 100)
                                    .build();

                            Modal modal = Modal.create("poll.setup.description", "\uD83D\uDCDC〣Gib die Beschreibung an!")
                                    .addActionRows(ActionRow.of(description))
                                    .build();

                            event.replyModal(modal).queue();

                        }

                        break;

                    case "duration":



                        break;

                }

            }
        }

    }

    public void onModalInteraction(ModalInteractionEvent event) {

        if (event.getModalId().startsWith("poll.setup")) {

            Member member = event.getMember();
            Poll poll = Poll.getFromMember(member);

            EmbedBuilder step;
            List<Button> buttons;

            switch (event.getModalId().split("\\.")[2]) {

                case "topic":

                    step = new EmbedBuilder();
                    step.setColor(0x3aa65b);
                    step.setTitle(":bar_chart: **Gib die Beschreibung für diese Umfrage ein!**");

                    buttons = new ArrayList<>();
                    buttons.add(Button.secondary("poll.setup.description", "Gib die Beschreibung der Umfrage an!").withEmoji(Emoji.fromCustom("script", 878586042821787658L, false)));

                    event.getChannel().sendTyping().queue();
                    interactionmap.get(member).sendMessageEmbeds(step.build()).setEphemeral(true).addActionRow(buttons).queue();

                    poll.setTopic(event.getValue("topic").getAsString());

                    break;

                case "description":

                    step = new EmbedBuilder();
                    step.setColor(0x3aa65b);
                    step.setTitle(":bar_chart: **Gib die Dauer der Umfrage an!**");

                    buttons = new ArrayList<>();
                    buttons.add(Button.secondary("poll.setup.duration", "Gib die Dauer der Umfrage an!").withEmoji(Emoji.fromFormatted("\uD83D\uDD51")));

                    event.getChannel().sendTyping().queue();
                    interactionmap.get(member).sendMessageEmbeds(step.build()).setEphemeral(true).addActionRow(buttons).queue();

                    poll.setDescription(event.getValue("description").getAsString());

                    break;

            }
        }

    }
}
