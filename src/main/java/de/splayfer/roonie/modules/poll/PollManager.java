package de.splayfer.roonie.modules.poll;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.SlashCommandManager;
import de.splayfer.roonie.utils.enums.Guilds;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.bson.Document;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PollManager implements SlashCommandManager {

    MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("splayfunity");

    public static HashMap<Member, Poll> polls = new HashMap<>();

    @Override
    public SlashCommandData[] slashCommands() {
        return new SlashCommandData[]{
                Commands.slash("umfrage", "\uD83D\uDCCA │ Verwaltet die gesamten Umfragen des Servers")
                        .addSubcommands(new SubcommandData("create", "➕ │ Erstelle eine neue Umfrage"))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        };
    }

    public void insertToMongoDB(Poll poll) {
        List<String> values = new ArrayList<>();
        poll.getMessage().getComponents().forEach(component -> component.asActionRow().getButtons().forEach(button -> values.add(button.getCustomId())));
        mongoDB.insert("poll", new Document()
                .append("channel", poll.getChannel().getIdLong())
                .append("topic", poll.getTopic())
                .append("description", poll.getDescription())
                .append("message", poll.getMessage().getIdLong())
                .append("buttons", values));
        Document document = new Document().append("channel", poll.getChannel().getIdLong()).append("message", poll.getMessage().getIdLong());
        List<Long> emptyList = new ArrayList<>();
        for (String id : values)
            document.append(id, emptyList);
        mongoDB.insert("pollEntrys", document);
        for (Member m : polls.keySet())
            if (polls.get(m).equals(this))
                polls.remove(m);
    }

    public Poll getFromMongoBD(MessageChannel channel, Message message) {
        Document document = mongoDB.find("poll", new Document().append("channel", channel.getIdLong()).append("message", message.getIdLong())).first();
        MessageChannel msgChannel = (MessageChannel) message.getGuild().getGuildChannelById(document.getLong("channel"));
        Message msg = msgChannel.retrieveMessageById(document.getLong("message")).complete();
        return new Poll(msgChannel, document.getString("topic"), document.getString("description"), msg, message.getComponents().get(0).asActionRow().getButtons().toArray(new Button[message.getComponents().get(0).asActionRow().getButtons().size()]));
    }

    public boolean isPoll(MessageChannel channel, Message message) {
        return mongoDB.exists("poll", new Document().append("channel", channel.getIdLong()).append("message", message.getIdLong()));
    }
}
