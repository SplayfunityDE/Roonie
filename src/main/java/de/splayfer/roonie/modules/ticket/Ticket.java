package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.enums.Channels;
import de.splayfer.roonie.utils.enums.Embeds;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;

public class Ticket {

    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    public static HashMap<Integer, String> typeSymbol = new HashMap<>(){{
        put(1, "\uD83D\uDCAC");
        put(2, "\uD83D\uDCDB");
        put(3, "👮‍♂️");
    }};

    @Getter
    @Setter
    private ThreadChannel channel;
    @Getter
    @Setter
    private ThreadChannel post;
    @Getter
    private Member creator;
    @Getter
    @Setter
    private Member supporter;
    @Getter
    @Setter
    private int type;
    @Getter
    private Date createDate;

    public Ticket(ThreadChannel channel, ThreadChannel post, Member creator, Member supporter, int type, Date createDate) {
        this.channel = channel;
        this.post = post;
        this.creator = creator;
        this.supporter = supporter;
        this.type = type;
        this.createDate = createDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(channel, ticket.channel) && Objects.equals(post, ticket.post) && Objects.equals(creator, ticket.creator) && Objects.equals(supporter, ticket.supporter) && Objects.equals(type, ticket.type) && Objects.equals(createDate, ticket.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(channel, post, creator, supporter, type, createDate);
    }

    public static Ticket getFromDocument(Document document) {
        Member supporter = null;
        if (document.getString("supporter") != null)
            supporter = Roonie.mainGuild.getMemberById(document.getString("supporter"));
        return new Ticket(Roonie.mainGuild.getThreadChannelById(document.getString("channel")), Roonie.mainGuild.getThreadChannelById(document.getString("post")), Roonie.mainGuild.getMemberById(document.getString("creator")), supporter, document.getInteger("type"), document.getDate("createDate"));
    }
    public static Ticket getFromChannel(String id) {
        return Ticket.getFromDocument(mongoDB.find("ticket", "channel", id).first());
    }
    public static Ticket getFromPost(String id) {
        return Ticket.getFromDocument(mongoDB.find("ticket", "post", id).first());
    }

    public static Ticket create(Member creator, int type) {
        ThreadChannel threadChannel;
        if (creator.getGuild().getBoostCount() >= 7) //check for server boost level
            threadChannel = Roonie.mainGuild.getTextChannelById(Channels.TICKETPANEL.getId()).createThreadChannel(typeSymbol.get(type) + "-" + creator.getEffectiveName(), true).setInvitable(false).complete();
        else
            threadChannel = Roonie.mainGuild.getTextChannelById(Channels.TICKETPANEL.getId()).createThreadChannel(typeSymbol.get(type) + "-" + creator.getEffectiveName(), true).complete();
        Ticket ticket = new Ticket(threadChannel, null, creator, null, type, new Date());
        ThreadChannel post = Roonie.mainGuild.getForumChannelById(Channels.TICKETFORUM.getId()).createForumPost(typeSymbol.get(type) + "-" + creator.getEffectiveName(), MessageCreateData.fromEmbeds(ticket.getPostEmbed())).addActionRow(Button.primary("ticket.claim", "Ticket claimen").withEmoji(Emoji.fromFormatted("\uD83D\uDD12"))).complete().getThreadChannel();
        ticket.setPost(post);
        mongoDB.insert("ticket", ticket.getAsDocument());

        //create embeds & update permissions
        ticket.channel.addThreadMember(creator).queue();
        ticket.channel.sendMessageEmbeds(ticket.getMainEmbeds()).setActionRow(
                Button.danger("ticket.close", "Ticket schließen").withEmoji(Emoji.fromFormatted("\uD83D\uDD12")),
                Button.secondary("ticket.export", "Chatverlauf exportieren").withEmoji(Emoji.fromCustom(Roonie.emojiServerGuild.getEmojiById("878586042821787658"))),
                Button.success("ticket.archiv", "Ticket archivieren").withEmoji(Emoji.fromCustom(Roonie.emojiServerGuild.getEmojiById("883415478700232735")))
        ).queue();
        return ticket;
    }

    public static Ticket fromUser(Member creator) {;
        if (mongoDB.exists("ticket", "creator", creator.getId()))
            return Ticket.getFromDocument(mongoDB.find("ticket", "creator", creator.getId()).first());
        return null;
    }

    public void close(String reason) {
        if (channel != null)
            channel.delete().queue();
        if (post != null)
            post.delete().queue();
        //move to ticket-closed
        mongoDB.insert("ticket-closed", mongoDB.find("ticket", "channel", channel.getId()).first());
        //delete from ticket
        mongoDB.drop("ticket", mongoDB.find("ticket", "channel", channel.getId()).first());
        try {
            creator.getUser().openPrivateChannel().complete().sendMessageEmbeds(Embeds.BANNER_TICKET, getCloseDmEmbed(reason)).queue();
        } catch (Exception exception) {
        }
    }

    public void prune(String channelId) {
        if (channel != null)
            channel.delete().queue();
        if (post != null)
            post.delete().queue();
        //move to ticket-closed
        mongoDB.insert("ticket-closed", mongoDB.find("ticket", "channel", channelId).first());
        //delete from ticket
        mongoDB.drop("ticket", mongoDB.find("ticket", "channel", channelId).first());
    }

    public Document getAsDocument() {
        Document doc = new Document()
                .append("channel", channel.getId())
                .append("post", post.getId())
                .append("creator", creator.getId())
                .append("type", type)
                .append("createDate", createDate)
                .append("channelTxt", channel.getName())
                .append("creatorTxt", creator.getEffectiveName());
        if (supporter != null)
            doc
                    .append("supporter", supporter.getId())
                    .append("supporterTxt", supporter.getEffectiveName());
        return doc;
    }

    public void updateMongoDB() {
        mongoDB.update("ticket", mongoDB.find("ticket", "channel", channel.getId()).first(), this.getAsDocument());
    }

    public static List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        for (Document doc : mongoDB.findAll("ticket"))
            tickets.add(Ticket.getFromDocument(doc));
        return tickets;
    }

    public static HashMap<Ticket, String> getAllTicketsWithId() {
        HashMap<Ticket, String> tickets = new HashMap<>();
        for (Document doc : mongoDB.findAll("ticket"))
            tickets.put(Ticket.getFromDocument(doc), doc.getString("channel"));
        return tickets;
    }

    public List<MessageEmbed> getMainEmbeds() {
        MessageEmbed banner;
        EmbedBuilder message = new EmbedBuilder();
        switch (type) {
            case 1:
                banner = Embeds.BANNER_TICKET;
                message.setColor(0x28346d);
                message.setTitle("\uD83D\uDCE8 Support Anfrage - Ticket");
                message.setDescription("Hey " + creator.getAsMention() + " !\n" +
                        "> dein Support Ticket wurde erfolgreich erstellt und ein zuständiges Teammtiglied kontaktiert. Überlege dir am besten schonmal im Voraus, wie du dein Anliegen formulieren möchtest!");
                message.addField("Ticket Details", "Thema: " + "`\uD83D\uDCE8 Allgemeiner Support\n`" +
                        "Bearbeitet von: " + "`NOCH NICHT BEARBEITET`\n" +
                        "Ersteller: " + "`" + creator.getUser().getAsTag() + "`", false);
                message.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
                break;
            case 2:
                banner = Embeds.BANNER_TICKET_BUG;
                message.setColor(0xFFD764);
                message.setTitle("<:warning:877158816419020820> Meldung eines Bugs - Ticket");
                message.setDescription("Hey " + creator.getAsMention() + " !\n" +
                        "> dein Support Ticket wurde erfolgreich erstellt und ein zuständiges Teammtiglied kontaktiert. Überlege dir am besten schonmal im Voraus, wie du dein Anliegen formulieren möchtest!");

                message.addField("Ticket Details", "Thema: " + "`\uD83D\uDCE8 Meldung eines Bugs\n`" +
                        "Bearbeitet von: " + "`NOCH NICHT BEARBEITET`\n" +
                        "Ersteller: " + "`" + creator.getUser().getAsTag() + "`", false);

                message.addField(":question: Wie melde ich einen Bug?", "**Schritt 1:** Teile uns ganz genau mit, wie du diesen Fehler ausgelöst hast\n" +
                        "**Schritt 2:** Wenn nötig, sende uns einen Screenshot als Beweis, dass dieser Bug tatsächlich funktioniert", false);
                message.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
                break;
            case 3:
                banner = Embeds.BANNER_TICKET_REPORT;
                message.setColor(0xDD2E44);
                message.setTitle("\uD83D\uDCE2 Meldung eines Nutzers - Ticket");
                message.setDescription("Hey " + creator.getAsMention() + " !\n" +
                        "> dein Support Ticket wurde erfolgreich erstellt und ein zuständiges Teammtiglied kontaktiert. Überlege dir am besten schonmal im Voraus, wie du dein Anliegen formulieren möchtest!");
                message.addField("Ticket Details", "Thema: " + "`\uD83D\uDCE8 Meldung eines Nutzers\n`" +
                        "Bearbeitet von: " + "`NOCH NICHT BEARBEITET`\n" +
                        "Ersteller: " + "`" + creator.getUser().getAsTag() + "`", false);
                message.addField(":question: Wie melde ich einen Nutzer?", "**Schritt 1:** Teile uns ganz genau mit, wie dich der Nutzer belästigt hat\n" +
                        "**Schritt 2:** Stelle sicher, dass sich der angegebene Nutzer auf unserem Server befindet\n" +
                        "**Schritt 3:** Sende uns einen Screenshot von der Belsätigung als Beweis", false);
                message.addField("⛔ Achtung!", "Das Fragen nach einer Servereinladung / Website ist nicht strafbar!", false);
                message.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
                break;
            default:
                banner = Embeds.BANNER_TICKET;
        }
        return List.of(banner, message.build());
    }

    public MessageEmbed getPostEmbed() {
        String typeTxt = "";
        switch (type) {
            case 1:
                typeTxt = "Frage / Allgemein";
                break;
            case 2:
                typeTxt = "Meldung Bug";
                break;
            case 3:
                typeTxt = "Meldung Nutzer";
                break;
        }
        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0x4DD0E1);
        message.setTitle(":ticket: Support Ticket von " + creator.getEffectiveName());
        message.setDescription("> Ein Ticket mit den folgenden Details wurde erstellt!");
        message.addField("Ersteller", creator.getAsMention(), true);
        message.addField("Typ", typeTxt, true);
        message.addField("Kanal", channel.getAsMention(), true);
        message.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
        return message.build();
    }

    public MessageEmbed getCloseDmEmbed(String reason) {
        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0x28346d);
        message.setTitle("TICKET GESCHLOSSEN");
        message.setDescription("> Dein Ticket auf SPLΛYFUNITY wurde mit den folgenden Details geschlossen");
        if (supporter != null)
            message.addField("Bearbeiter", supporter.getAsMention(), true);
        else
            message.addField("Bearbeiter", "`KEINER`", true);
        message.addField("Erstelldatum", new SimpleDateFormat("dd.MM.yyyy hh:mm").format(createDate), true);
        if (reason != null)
            message.addField("Grund", reason, true);
        else
            message.addField("Grund", "`KEINER`", true);
        message.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
        return message.build();
    }
}
