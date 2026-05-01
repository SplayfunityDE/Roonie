package de.splayfer.roonie.modules.giveaway;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.modules.level.LevelManager;
import de.splayfer.roonie.utils.SlashCommandManager;
import de.splayfer.roonie.utils.enums.Guilds;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.bson.Document;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
@DependsOn({"mongoDBDatabase"})
public class GiveawayManager implements SlashCommandManager {

    private final Roonie roonie;
    private final LevelManager levelManager;
    private final MongoDBDatabase mongoDB;

    public GiveawayManager(@Lazy Roonie roonie, LevelManager levelManager) {
        this.roonie = roonie;
        this.levelManager = levelManager;
        this.mongoDB = MongoDBDatabase.getDatabase("splayfunity");
    }

    public static HashMap<Member, Giveaway> giveaways = new HashMap<>();

    @Override
    public SlashCommandData[] slashCommands() {
        return new SlashCommandData[]{
                Commands.slash("giveaway", "\uD83C\uDF89 │ Verwalte die gesamten Giveaways des Servers")
                        .addSubcommands(new SubcommandData("create", "➕ │ Erstelle ein neues Giveaway"))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
        };
    }

    public Giveaway getFromDocument(Document document) {
        TextChannel channel = roonie.getMainGuild().getTextChannelById(document.getLong("channel"));
        assert channel != null;
        return new Giveaway(channel, document.getString("prize"), document.getLong("duration"), document.getList("requirement", String.class), document.getInteger("amount"), document.getString("picture"), channel.getHistory().getMessageById(document.getLong("message")), document.getList("entrys", Long.class));
    }

    public Giveaway findGiveaway(Message message) {
        return getFromDocument(mongoDB.find("giveaway", new Document("message", message.getIdLong())).first());
    }

    public Giveaway getFromMessage(Message message) {
        return getFromDocument(Objects.requireNonNull(mongoDB.find("giveaway", "message", message.getIdLong()).first()));
    }

    public List<Giveaway> getAllGiveaways() {
        List<Giveaway> gwList = new ArrayList<>();
        mongoDB.findAll("giveaway").forEach(document -> gwList.add(getFromDocument(document)));
        return gwList;
    }


    public void insertToMongoDB(Giveaway giveaway) {

        if (giveaway.getEntrys() == null)
            giveaway.setEntrys(new ArrayList<>());
        mongoDB.insert("giveaway", new Document()
                .append("channel", giveaway.getChannel().getIdLong())
                .append("prize", giveaway.getPrize())
                .append("duration", giveaway.getDuration())
                .append("requirement", giveaway.getRequirement())
                .append("amount", giveaway.getAmount())
                .append("picture", giveaway.getPicture())
                .append("message", giveaway.getMessage().getIdLong())
                .append("entrys", giveaway.getEntrys()));
        for (Member m : giveaways.keySet())
            if (giveaways.get(m).equals(this))
                giveaways.remove(m);
    }

    public void removeFromMongoDB(Giveaway giveaway) {
        mongoDB.drop("giveaway", "message", giveaway.getMessage().getIdLong());
        for (Member m : giveaways.keySet())
            if (giveaways.get(m).equals(getFromMessage(giveaway.getMessage())))
                giveaways.remove(m);
    }

    public boolean checkRequirement(Giveaway giveaway, Member member) {
        return switch (giveaway.getRequirement().get(0)) {
            case "role" -> member.getRoles().contains(member.getGuild().getRoleById(giveaway.getRequirement().get(1)));
            case "level" -> levelManager.getLevel(member) >= Integer.parseInt(giveaway.getRequirement().get(1));
            default -> true;
        };
    }

    public boolean isGiveaway(Message message) {
        return mongoDB.exists("giveaway", "message", message.getIdLong());
    }

    @Scheduled(initialDelay = 5, fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
    public void checkGiveaways() {
        for (Giveaway giveaway : getAllGiveaways()) {
            if (LocalDateTime.now().isAfter(LocalDateTime.ofEpochSecond(giveaway.getDuration(), 0, ZoneOffset.UTC))) {
                Guild guild;
                MessageChannel channel;

                guild = roonie.getMainGuild();
                channel = giveaway.getChannel();

                Message m = giveaway.getMessage();

                List<Long> entrys = giveaway.getEntrys();

                List<Member> winners = new ArrayList<>();
                StringBuilder winmessage = new StringBuilder();

                if (entrys.size() >= giveaway.getAmount()) {
                    for (int i = 0; i < giveaway.getAmount(); i++) {
                        Random random = new Random();
                        int winningNumber = random.nextInt(entrys.size());

                        Member winner = guild.getMemberById(entrys.get(winningNumber));

                        winners.add(winner);
                        entrys.remove(winner.getId());
                        winmessage.append(winner.getAsMention());
                    }
                    EmbedBuilder banner = new EmbedBuilder();
                    banner.setColor(m.getEmbeds().get(0).getColor());
                    banner.setImage(m.getEmbeds().get(0).getImage().getUrl());

                    EmbedBuilder main = new EmbedBuilder();
                    main.setColor(m.getEmbeds().get(1).getColor());
                    main.setImage(m.getEmbeds().get(1).getImage().getUrl());
                    main.setTitle(m.getEmbeds().get(1).getTitle());
                    main.setDescription("Dieses Giveaway wurde bereits beendet! Du kannst hier die Details einsehen");
                    main.addField(m.getEmbeds().get(1).getFields().get(0).getName(), m.getEmbeds().get(1).getFields().get(0).getValue(), true);
                    main.addField(m.getEmbeds().get(1).getFields().get(1).getName(), "<:cancel:877158821779345428> BEREITS BEENDET", true);
                    main.addField(":busts_in_silhouette: Gewinner", winmessage.toString(), true);

                    List<Button> buttons = new ArrayList<>();
                    buttons.add(Button.secondary("giveaway.closed", "Giveaway wurde bereits beendet").withEmoji(Emoji.fromCustom("cancel", Long.parseLong("877158821779345428"), false)));

                    m.editMessageEmbeds(banner.build(), main.build()).setComponents(ActionRow.of(buttons)).queue();

                    Message ping = channel.sendMessage(winmessage.toString()).complete();
                    ping.delete().queue();

                    for (Member dmMember: winners) {

                        User user = dmMember.getUser();
                        PrivateChannel dm = user.openPrivateChannel().complete();

                        banner = new EmbedBuilder();
                        banner.setColor(0x28346d);
                        banner.setImage("https://cdn.discordapp.com/attachments/880725442481520660/913825741588803635/banner_giveaway.png");

                        EmbedBuilder dmMessage = new EmbedBuilder();
                        dmMessage.setColor(0x28346d);
                        dmMessage.setTitle(":tada: HERZLICHEN GLÜCKWUNSCH");
                        dmMessage.setDescription("Hey! Es scheint als hast du auf SPLΛYFUNITY an einem Giveaway gewonnen");
                        dmMessage.addField("<a:wettbewerb:898566916958978078> Gewonnener Preis", giveaway.getPrize(), false);
                        dmMessage.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        dm.sendMessageEmbeds(banner.build(), dmMessage.build()).queue();
                        dm.sendMessage(Objects.requireNonNull(guild.getVanityUrl())).queue();
                    }
                    removeFromMongoDB(giveaway);
                }
            }
        }
    }
}