package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.enums.Channels;
import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bson.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Ticket {

    static MongoDBDatabase mongoDB = new MongoDBDatabase("splayfunity");

    private static HashMap<Integer, String> typeSymbol = new HashMap<>(){{
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
        return new Ticket(Roonie.mainGuild.getThreadChannelById(document.getLong("channel")), Roonie.mainGuild.getThreadChannelById(document.getLong("post")), Roonie.mainGuild.getMemberById(document.getLong("creator")), Roonie.mainGuild.getMemberById(document.getLong("supporter")), document.getInteger("type"), document.getDate("createDate"));
    }
    public static Ticket getFromChannel(String id) {
        return Ticket.getFromDocument(mongoDB.find("ticket", "channel", id).first());
    }

    public static Ticket create(Member creator, int type) {
        Ticket ticket = new Ticket(Roonie.mainGuild.getTextChannelById(Channels.TICKETPANEL.getId()).createThreadChannel(typeSymbol.get(type) + "-" + creator.getEffectiveName()).complete(), Roonie.mainGuild.getForumChannelById(Channels.TICKETFORUM.getId()).createThreadChannel(typeSymbol.get(type) + "-" + creator.getEffectiveName()).complete(), creator, null, type, new Date());
        mongoDB.insert("ticket", ticket.getAsDocument());

        //create embeds & update permissions
        ticket.channel.addThreadMember(creator).queue();
        ticket.channel.sendMessageEmbeds(ticket.getMainEmbeds()).queue();
        ticket.post.sendMessageEmbeds(ticket.getPostEmbed()).setActionRow(Button.primary("ticket.claim", "Ticket claimen")).queue();

        return ticket;
    }

    public void delete() {
        mongoDB.drop("ticket", mongoDB.find("ticket", "channel", channel.getIdLong()).first());
    }

    public Document getAsDocument() {
        return new Document()
                .append("channel", channel.getIdLong())
                .append("post", post.getIdLong())
                .append("creator", creator.getIdLong())
                .append("supporter", supporter.getIdLong())
                .append("type", type)
                .append("createDate", createDate);
    }

    public void updateMongoDB() {
        mongoDB.update("ticket", mongoDB.find("ticket", "channel", channel.getIdLong()).first(), getAsDocument());
    }

    private List<MessageEmbed> getMainEmbeds() {
        EmbedBuilder message = new EmbedBuilder();
        switch (type) {
            case 1:
                message.setColor(0x4DD0E1);
                message.setTitle("\uD83D\uDCE8 Support Anfrage - Ticket");
                message.setDescription("Hey " + creator.getAsMention() + " !\n" +
                        "> dein Support Ticket wurde erfolgreich erstellt und ein zuständiges Teammtiglied kontaktiert. Überlege dir am besten schonmal im Voraus, wie du dein Anliegen formulieren möchtest!");
                message.addField("Ticket Details", "Thema: " + "`\uD83D\uDCE8 Allgemeiner Support\n`" +
                        "Bearbeitet von: " + "`NOCH NICHT BEARBEITET`\n" +
                        "Ersteller: " + "`" + creator.getUser().getAsTag() + "`", false);
                break;
            case 2:
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
        }
        return List.of(Embeds.BANNER_TICKET_BUG, message.build());
    }

    private MessageEmbed getPostEmbed() {
        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0x4DD0E1);
        message.setTitle("Support Ticket von " + creator.getAsMention());
        message.setDescription("> Ein Ticket mit den folgenden Details wurde erstellt!");
        message.addField("Ersteller", creator.getAsMention(), true);
        message.addField("Typ", String.valueOf(type), true);
        message.addField("Kanal", channel.getAsMention(), true);
        message.setFooter("ID: " + channel.getId());
        return message.build();
    }
}