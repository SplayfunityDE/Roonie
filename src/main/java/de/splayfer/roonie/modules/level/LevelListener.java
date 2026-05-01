package de.splayfer.roonie.modules.level;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.enums.Roles;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.components.actionrow.ActionRow;
import net.dv8tion.jda.api.components.buttons.Button;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class LevelListener extends ListenerAdapter {

    private final Roonie roonie;
    private final LevelManager levelManager;

    public LevelListener(@Lazy  Roonie roonie, LevelManager levelManager) {
        this.roonie = roonie;
        this.levelManager = levelManager;
    }

    private static List<Member> messageCoolDown = new ArrayList<>();

    public static HashMap<Integer, Role> levelroles = null;

    public static void initRoles(Guild guild) {

        levelroles = new HashMap<>() {{
            put(5, Roles.LVL5.getRole(guild));
            put(10, Roles.LVL10.getRole(guild));
            put(20, Roles.LVL20.getRole(guild));
            put(30, Roles.LVL30.getRole(guild));
            put(40, Roles.LVL40.getRole(guild));
            put(50, Roles.LVL50.getRole(guild));
            put(100, Roles.LVL100.getRole(guild));
        }};
    }

    public void onMessageReceived (MessageReceivedEvent event) {
        if (!event.getAuthor().isBot() && event.getChannelType().isGuild()) {
            int xpStep;
            if (checkCoolDown(event.getMember())) {
                if (event.getMember().getRoles().contains(event.getGuild().getBoostRole()))
                    xpStep = 15;
                else
                    xpStep = 10;

                Member member = event.getMember();

                int level = levelManager.getLevel(member);
                int xp = levelManager.getXp(member);
                levelManager.addXpToUser(member, xpStep);
                xp += xpStep;
                int levelstep = levelManager.getLevelStep(level);
                if (xp >= levelstep + 1) {

                    //wenn neues level erreicht
                    xp = xp - (levelstep + 1);

                    levelManager.setXp(member, xp);
                    levelManager.addLevelToUser(member, 1);

                    int i = level + 1;
                    if (i >= 5) {
                        int itemp = i;
                        initRoles(event.getGuild());
                        while (!levelroles.containsKey(itemp)) {
                            itemp--;
                        }
                        for (Role r : member.getRoles())
                            if (levelroles.containsValue(r))
                                member.getGuild().removeRoleFromMember(member.getUser(), r).queue();
                        event.getGuild().addRoleToMember(member.getUser(), levelroles.get(itemp)).queue();
                    }
                    event.getMessage().reply(levelUp(member)).queue();
                }
            }
        }
    }

    private static boolean checkCoolDown(Member member) {
        boolean check = false;
        if (!messageCoolDown.contains(member)) {
            check = true;
            messageCoolDown.add(member);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    messageCoolDown.remove(member);
                }
            }, 10 * 1000);
        }
        return check;
    }

    @Scheduled(initialDelay = 5, fixedDelay = 30, timeUnit = TimeUnit.MINUTES)
    public void checkVoiceMembers() {
        for (Member member: roonie.getMainGuild().getMembers()) {
            if (member.getVoiceState().inAudioChannel() && member.getVoiceState().getChannel().getType().equals(ChannelType.VOICE)) {
                if (!(member.getVoiceState().isMuted() || member.getVoiceState().isDeafened() || member.getVoiceState().isSelfMuted())) {
                    int voiceminutes = levelManager.getXp(member) + 10;
                    int level = levelManager.getLevel(member);

                    //wenn neues level erreicht

                    int levelstep = levelManager.getLevelStep(level);
                    if (voiceminutes >= levelstep + 1) {
                        voiceminutes = voiceminutes - levelstep;
                        levelManager.setXp(member, voiceminutes);
                        levelManager.addLevelToUser(member, 1);
                        member.getVoiceState().getChannel().asVoiceChannel().sendMessage(member.getAsMention()).queue();
                        member.getVoiceState().getChannel().asVoiceChannel().sendMessage(levelUp(member)).queue();
                    } else
                        levelManager.addXpToUser(member, 10);
                }
            }
        }
    }

    public MessageCreateData levelUp(Member member) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0xfead33);
        embedBuilder.setTitle(":star: Neues Level auf SPLΛYFUNITY erreicht!");
        embedBuilder.setDescription("**GG**! Du hast ein neues Level erreicht!\n" +
                "<:name:878587314367000606> Neues Level: " + levelManager.getLevel(member));
        Guild guild = roonie.getMainGuild();
        switch (levelManager.getLevel(member)) {
            case 5:
                guild.getManager().getGuild().addRoleToMember(member.getUser(), Roles.LVL5.getRole(guild)).queue();
                embedBuilder.addField("Neue Vorteile", Roles.LVL5.getRole(guild).getAsMention() + " = `Level 5`\n" +
                        "<a:checkblue:896351653236711454> Rechte auf Nicknames ändern\n" +
                        "<a:checkblue:896351653236711454> 5.000$ im Casino\n" +
                        "<a:checkblue:896351653236711454> Zugriff auf Teamler Bewerbungen\n" +
                        "<a:checkblue:896351653236711454> Höher gelistet werden", false);
                break;
            case 10:
                guild.getManager().getGuild().addRoleToMember(member.getUser(), Roles.LVL10.getRole(guild)).queue();
                embedBuilder.addField("Neue Vorteile", Roles.LVL10.getRole(guild).getAsMention() + " = `Level 10`\n" +
                        "<a:checkgreen:896351654092374086> Alle Vorteile von Stufe 5\n" +
                        "<a:checkgreen:896351654092374086> 7.500$ im Casino\n" +
                        "<a:checkgreen:896351654092374086> Eigener animierter Banner", false);
                break;
            case 20:
                guild.getManager().getGuild().addRoleToMember(member.getUser(), Roles.LVL20.getRole(guild)).queue();
                embedBuilder.addField("Neue Vorteile", Roles.LVL20.getRole(guild).getAsMention() + " = `Level 20`\n" +
                        "<a:checkorange:896351648601997353> Komplett eingerichteter Discord Server\n" +
                        "<a:checkorange:896351648601997353> Exklusiver Chat & Talk\n" +
                        "<a:checkorange:896351648601997353> 10.000$ im Casino\n" +
                        "<a:checkorange:896351648601997353> Zugriff auf Werbung in #promotion\n \n", false);
                break;
            case 30:
                guild.getManager().getGuild().addRoleToMember(member.getUser(), Roles.LVL30.getRole(guild)).queue();
                embedBuilder.addField("Neue Vorteile", Roles.LVL30.getRole(guild).getAsMention() + " = `Level 30`\n" +
                        "<a:checkyellow:896351651617726484> Eigener Discord Bot + 24/7 Hosting\n" +
                        "<a:checkyellow:896351651617726484> 20.000$ im Casino\n" +
                        "<a:checkyellow:896351651617726484> Zufälliger Steam Key\n" +
                        "<a:checkyellow:896351651617726484> Chat Cosmetic Tier I\n \n", false);
                break;
            case 40:
                guild.getManager().getGuild().addRoleToMember(member.getUser(), Roles.LVL40.getRole(guild)).queue();
                embedBuilder.addField("Neue Vorteile", Roles.LVL40.getRole(guild).getAsMention() + " = `Level 40`\n" +
                        "<a:checkpurple:896351653651972116> Giveaways ohne Vorraussetzungen\n" +
                        "<a:checkpurple:896351653651972116> 20.000$ im Casino" +
                        "<a:checkpurple:896351653651972116> Zugriff auf den `/ban` Command", false);
                break;
            case 50:
                guild.getManager().getGuild().addRoleToMember(member.getUser(), Roles.LVL50.getRole(guild)).queue();
                embedBuilder.addField("Neue Vorteile", Roles.LVL50.getRole(guild).getAsMention() + " = `Level 50`\n" +
                        "<a:checkgreen:896351654092374086> Alle Booster Vorteile\n" +
                        "<a:checkgreen:896351654092374086> Chat Cosmetic Tier II\n" +
                        "<a:checkgreen:896351654092374086> 30.000$ im Casino", false);
                break;
            case 100:
                guild.getManager().getGuild().addRoleToMember(member.getUser(), Roles.LVL100.getRole(guild)).queue();
                embedBuilder.addField("Neue Vorteile", Roles.LVL100.getRole(guild).getAsMention() + " = `Level 100`\n" +
                        "<a:checkpink:896351654092374086> Eigene Rolle\n" +
                        "<a:checkpink:896351654092374086> Zugriff auf den `$heck` command\n" +
                        "<a:checkpink:896351654092374086> 50.000$ im Casino\n" +
                        "<a:checkpink:896351654092374086> Chat Cosmetic Tier III", false);
                break;
            default:
                embedBuilder.addField("Neue Vorteile", "`KEINE`", false);
                break;
        }
        return new MessageCreateBuilder().setEmbeds(embedBuilder.build()).setComponents(ActionRow.of(Button.secondary("viewlevels", "Alle Vorteile ansehen!").withEmoji(Emoji.fromFormatted("\uD83C\uDF81")))).build();
    }
}