package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.enums.Channels;
import de.splayfer.roonie.utils.enums.Embeds;
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

    private ThreadChannel channel;
    private ThreadChannel post;
    private Member creator;
    private Member supporter;
    private int type;
    private Date createDate;

    public ThreadChannel getChannel() {
        return channel;
    }

    public void setChannel(ThreadChannel channel) {
        this.channel = channel;
    }

    public ThreadChannel getPost() {
        return post;
    }

    public void setPost(ThreadChannel post) {
        this.post = post;
    }

    public Member getCreator() {
        return creator;
    }

    public void setCreator(Member creator) {
        this.creator = creator;
    }

    public Member getSupporter() {
        return supporter;
    }

    public void setSupporter(Member supporter) {
        this.supporter = supporter;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

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
        if (document.getLong("supporter") != null)
            supporter = Roonie.mainGuild.getMemberById(document.getLong("supporter"));
        return new Ticket(Roonie.mainGuild.getThreadChannelById(document.getLong("channel")), Roonie.mainGuild.getThreadChannelById(document.getLong("post")), Roonie.mainGuild.getMemberById(document.getLong("creator")), supporter, document.getInteger("type"), document.getDate("createDate"));
    }
    public static Ticket getFromChannel(long id) {
        return Ticket.getFromDocument(mongoDB.find("ticket", "channel", id).first());
    }
    public static Ticket getFromPost(long id) {
        return Ticket.getFromDocument(mongoDB.find("ticket", "post", id).first());
    }

    public static Ticket create(Member creator, int type) {
        ThreadChannel threadChannel;
        if (creator.getGuild().getBoostCount() >= 7) //check for server boost level
            threadChannel = Roonie.mainGuild.getTextChannelById(Channels.TICKETPANEL.getId()).createThreadChannel(typeSymbol.get(type) + "-" + creator.getEffectiveName(), true).setInvitable(false).complete();
        else
            threadChannel = Roonie.mainGuild.getTextChannelById(Channels.TICKETPANEL.getId()).createThreadChannel(typeSymbol.get(type) + "-" + creator.getEffectiveName(), true).complete();
        Ticket ticket = new Ticket(threadChannel, Roonie.mainGuild.getForumChannelById(Channels.TICKETFORUM.getId()).createForumPost(typeSymbol.get(type) + "-" + creator.getEffectiveName(), MessageCreateData.fromContent("\u200E")).complete().getThreadChannel(), creator, null, type, new Date());
        mongoDB.insert("ticket", ticket.getAsDocument());

        //create embeds & update permissions
        ticket.channel.addThreadMember(creator).queue();
        ticket.channel.sendMessageEmbeds(ticket.getMainEmbeds()).setActionRow(
                Button.danger("ticket.close", "Ticket schließen").withEmoji(Emoji.fromFormatted("\uD83D\uDD12")),
                Button.secondary("ticket.export", "Chatverlauf exportieren").withEmoji(Emoji.fromCustom(Roonie.emojiServerGuild.getEmojiById("878586042821787658"))),
                Button.success("ticket.archiv", "Ticket archivieren").withEmoji(Emoji.fromCustom(Roonie.emojiServerGuild.getEmojiById("883415478700232735")))
        ).queue();
        ticket.post.sendMessageEmbeds(ticket.getPostEmbed()).setActionRow(Button.primary("ticket.claim", "Ticket claimen").withEmoji(Emoji.fromFormatted("\uD83D\uDD12"))).queue();
        return ticket;
    }

    public void close(String reason) {
        channel.delete().queue();
        post.delete().queue();
        mongoDB.drop("ticket", mongoDB.find("ticket", "channel", channel.getIdLong()).first());
        creator.getUser().openPrivateChannel().complete().sendMessageEmbeds(Embeds.BANNER_TICKET, getCloseDmEmbed(reason)).queue();
    }

    public Document getAsDocument() {
        Document doc = new Document()
                .append("channel", channel.getIdLong())
                .append("post", post.getIdLong())
                .append("creator", creator.getIdLong())
                .append("type", type)
                .append("createDate", createDate);
        if (supporter != null)
            doc.append("supporter", supporter.getIdLong());
        return doc;
    }

    public void updateMongoDB() {
        mongoDB.update("ticket", mongoDB.find("ticket", "channel", channel.getIdLong()).first(), this.getAsDocument());
    }

    public static List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        for (Document doc : mongoDB.findAll("ticket"))
            tickets.add(Ticket.getFromDocument(doc));
        return tickets;
    }

    public List<MessageEmbed> getMainEmbeds() {
        MessageEmbed banner;
        EmbedBuilder message = new EmbedBuilder();
        switch (type) {
            case 1:
                banner = Embeds.BANNER_TICKET;
                message.setColor(0x4DD0E1);
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