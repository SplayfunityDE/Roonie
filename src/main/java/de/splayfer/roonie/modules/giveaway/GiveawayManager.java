package de.splayfer.roonie.modules.giveaway;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.CommandManager;
import de.splayfer.roonie.utils.enums.Guilds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class GiveawayManager {

    public static void init() {
        Roonie.builder.addEventListeners(new GiveawayEnterListener(), new GiveawayCreateCommand());

        CommandManager.addCommand(Guilds.MAIN,
                Commands.slash("giveaway", "\uD83C\uDF89 │ Verwalte die gesamten Giveaways des Servers!")
                        .addSubcommands(new SubcommandData("create", "➕ │ Erstelle ein neues Giveaway!"))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)));

        checkGiveaways();
    }

    public static void checkGiveaways() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                for (Giveaway giveaway : Giveaway.getAllGiveaways()) {
                    if (LocalDateTime.now().isAfter(LocalDateTime.ofEpochSecond(giveaway.getDuration(), 0, ZoneOffset.UTC))) {
                        //TODO | roll giveaway
                        Guild guild;
                        MessageChannel channel;

                        guild = Roonie.mainGuild;
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
                            main.setDescription("Dieses Giveaway wurde bereits beendet! Du kannst hier die Details einsehen!");
                            main.addField(m.getEmbeds().get(1).getFields().get(0).getName(), m.getEmbeds().get(1).getFields().get(0).getValue(), true);
                            main.addField(m.getEmbeds().get(1).getFields().get(1).getName(), "<:cancel:877158821779345428> BEREITS BEENDET", true);
                            main.addField(":busts_in_silhouette: Gewinner", winmessage.toString(), true);

                            List<Button> buttons = new ArrayList<>();
                            buttons.add(Button.secondary("giveaway.closed", "Giveaway wurde bereits beendet!").withEmoji(Emoji.fromCustom("cancel", Long.parseLong("877158821779345428"), false)));

                            m.editMessageEmbeds(banner.build(), main.build()).setActionRow(buttons).queue();

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
                                dmMessage.setTitle(":tada: HERZLICHEN GLÜCKWUNSCH!");
                                dmMessage.setDescription("Hey! Es scheint als hast du auf SPLΛYFUNITY an einem Giveaway gewonnen!");
                                dmMessage.addField("<a:wettbewerb:898566916958978078> Gewonnener Preis", giveaway.getPrize(), false);
                                dmMessage.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                                dm.sendMessageEmbeds(banner.build(), dmMessage.build()).queue();
                                dm.sendMessage(Objects.requireNonNull(guild.getVanityUrl())).queue();
                            }
                            giveaway.removeFromMongoDB();
                        }
                    }
                }
            }
        }, 60000, 600000);
    }
}