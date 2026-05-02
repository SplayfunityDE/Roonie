package de.splayfer.roonie.modules.ticket;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.enums.Channels;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.bson.Document;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Component
public class TicketManager {

    private final Roonie roonie;

    public TicketManager(@Lazy Roonie roonie) {
        this.roonie = roonie;
    }

    public static HashMap<Integer, String> typeSymbol = new HashMap<>(){{
        put(1, "\uD83D\uDCAC");
        put(2, "\uD83D\uDCDB");
        put(3, "👮‍♂️");
    }};

    MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    public Ticket create(Member creator, int type) {
        ThreadChannel threadChannel;
        if (creator.getGuild().getBoostCount() >= 7) //check for server boost level
            threadChannel = roonie.getMainGuild().getTextChannelById(Channels.TICKETPANEL.getId()).createThreadChannel(typeSymbol.get(type) + "-" + creator.getEffectiveName(), true).setInvitable(false).complete();
        else
            threadChannel = roonie.getMainGuild().getTextChannelById(Channels.TICKETPANEL.getId()).createThreadChannel(typeSymbol.get(type) + "-" + creator.getEffectiveName(), true).complete();
        Ticket ticket = new Ticket(threadChannel, null, creator, null, type, new Date());
        ThreadChannel post = roonie.getMainGuild().getForumChannelById(Channels.TICKETFORUM.getId()).createForumPost(typeSymbol.get(type) + "-" + creator.getEffectiveName(), MessageCreateData.fromEmbeds(ticket.getPostEmbed())).addComponents(ActionRow.of(Button.primary("ticket.claim", "Ticket claimen").withEmoji(Emoji.fromFormatted("\uD83D\uDD12")))).complete().getThreadChannel();
        ticket.setPost(post);
        mongoDB.insert("ticket", ticket.getAsDocument());

        //create embeds & update permissions
        ticket.getChannel().addThreadMember(creator).queue();
        ticket.getChannel().sendMessageEmbeds(ticket.getMainEmbeds()).setComponents(ActionRow.of(
                Button.danger("ticket.close", "Ticket schließen").withEmoji(Emoji.fromFormatted("\uD83D\uDD12")),
                Button.secondary("ticket.export", "Chatverlauf exportieren").withEmoji(Emoji.fromCustom(roonie.getEmojiServerGuild().getEmojiById("878586042821787658"))),
                Button.success("ticket.archiv", "Ticket archivieren").withEmoji(Emoji.fromCustom(roonie.getEmojiServerGuild().getEmojiById("883415478700232735")))
        )).queue();
        return ticket;
    }

    public Ticket getFromDocument(Document document) {
        Member supporter = null;
        if (document.getString("supporter") != null)
            supporter = roonie.getMainGuild().getMemberById(document.getString("supporter"));
        return new Ticket(roonie.getMainGuild().getThreadChannelById(document.getString("channel")), roonie.getMainGuild().getThreadChannelById(document.getString("post")), roonie.getMainGuild().getMemberById(document.getString("creator")), supporter, document.getInteger("type"), document.getDate("createDate"));
    }
    public Ticket getFromChannel(String id) {
        return this.getFromDocument(mongoDB.find("ticket", "channel", id).first());
    }
    public Ticket getFromPost(String id) {
        return this.getFromDocument(mongoDB.find("ticket", "post", id).first());
    }

    public Ticket fromUser(Member creator) {;
        if (mongoDB.exists("ticket", "creator", creator.getId()))
            return this.getFromDocument(mongoDB.find("ticket", "creator", creator.getId()).first());
        return null;
    }

    public void updateMongoDB(Ticket ticket) {
        mongoDB.update("ticket", mongoDB.find("ticket", "channel", ticket.getChannel().getId()).first(), ticket.getAsDocument());
    }

    public List<Ticket> getAllTickets() {
        List<Ticket> tickets = new ArrayList<>();
        for (Document doc : mongoDB.findAll("ticket"))
            tickets.add(this.getFromDocument(doc));
        return tickets;
    }

    public HashMap<Ticket, String> getAllTicketsWithId() {
        HashMap<Ticket, String> tickets = new HashMap<>();
        for (Document doc : mongoDB.findAll("ticket"))
            tickets.put(this.getFromDocument(doc), doc.getString("channel"));
        return tickets;
    }
}