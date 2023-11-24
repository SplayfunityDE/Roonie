package de.splayfer.roonie.modules.level;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.enums.Roles;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.*;

public class LevelListener extends ListenerAdapter {

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

            if (event.getMember().getRoles().contains(event.getGuild().getBoostRole())) {
                xpStep = 15;
            } else {
                xpStep = 10;
            }

            Member member = event.getMember();

            int level = LevelManager.getLevel(member);
            int xp = LevelManager.getXp(member);

            if (checkCoolDown(member)) {

                LevelManager.addXpToUser(member, xpStep);

                xp += xpStep;

                int levelstep = LevelManager.getLevelStep(level);

                if (xp >= levelstep + 1) {

                    //wenn neues level erreicht

                    xp = xp - (levelstep + 1);
                    
                    LevelManager.setXp(member, xp);
                    LevelManager.addLevelToUser(member, 1);

                    int i = level + 1;

                    initRoles(event.getGuild());

                    if (levelroles.containsKey(i)) {

                        for (Role r : member.getRoles()) {
                            if (levelroles.containsValue(r)) {
                                member.getGuild().removeRoleFromMember(member.getUser(), r).queue();
                            }
                        }

                        event.getGuild().addRoleToMember(member.getUser(), levelroles.get(i)).queue();

                    }

                    levelUp(member);

                }

            }

        }

    }

    private static boolean checkCoolDown(Member member) {

        boolean check;

        if (!messageCoolDown.contains(member)) {
            messageCoolDown.add(member);
            check = true;

            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    messageCoolDown.remove(member);
                    t.cancel();
                }
            }, 10 * 1000);

        } else {
            check = false;
        }

        return check;

    }

    public static void checkVoiceMembers() {

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {

                    for (Member member: Roonie.mainGuild.getMembers()) {

                        if (member.getVoiceState().inAudioChannel()) {

                            if (!(member.getVoiceState().isMuted() || member.getVoiceState().isDeafened() || member.getVoiceState().isSelfMuted())) {

                                int voiceminutes = LevelManager.getXp(member) + 10;

                                int level = LevelManager.getLevel(member);

                                //wenn neues level erreicht

                                int levelstep = LevelManager.getLevelStep(level);

                                if (voiceminutes >= levelstep + 1) {
                                    voiceminutes = voiceminutes - levelstep;

                                    LevelManager.setXp(member, voiceminutes);
                                    LevelManager.addLevelToUser(member, 1);
                                } else {
                                    LevelManager.addXpToUser(member, 10);
                                }


                            }

                        }

                    }

            }
        }, 180000, 180000);

    }

    public static void levelUp(Member member) {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(0xfead33);
        embedBuilder.setTitle(":star: Neues Level auf SPLΛYFUNITY erreicht!");
        embedBuilder.setDescription("**GG**! Du hast ein neues Level erreicht!\n" +
                "<:name:878587314367000606> Neues Level: " + LevelManager.getLevel(member));

        Guild guild = Roonie.mainGuild;

        switch (LevelManager.getLevel(member)) {

            case 5:

                Roonie.mainGuild.getManager().getGuild().addRoleToMember(member.getUser(), Roles.LVL5.getRole(guild)).queue();
                embedBuilder.addField("Neue Vorteile", Roles.LVL5.getRole(guild).getAsMention() + " = `Level 5`\n" +
                        "<a:checkblue:896351653236711454> Rechte auf Nicknames ändern\n" +
                        "<a:checkblue:896351653236711454> 5.000$ im Casino\n" +
                        "<a:checkblue:896351653236711454> Zugriff auf Teamler Bewerbungen\n" +
                        "<a:checkblue:896351653236711454> Höher gelistet werden", false);

                break;

            case 10:

                Roonie.mainGuild.getManager().getGuild().addRoleToMember(member.getUser(), Roles.LVL10.getRole(guild)).queue();
                embedBuilder.addField("Neue Vorteile", Roles.LVL10.getRole(guild).getAsMention() + " = `Level 10`\n" +
                        "<a:checkgreen:896351654092374086> Alle Vorteile von Stufe 5\n" +
                        "<a:checkgreen:896351654092374086> 7.500$ im Casino\n" +
                        "<a:checkgreen:896351654092374086> Eigener animierter Banner", false);

                break;


            case 20:

                Roonie.mainGuild.getManager().getGuild().addRoleToMember(member.getUser(), Roles.LVL20.getRole(guild)).queue();
                embedBuilder.addField("Neue Vorteile", Roles.LVL20.getRole(guild).getAsMention() + " = `Level 20`\n" +
                        "<a:checkorange:896351648601997353> Komplett eingerichteter Discord Server\n" +
                        "<a:checkorange:896351648601997353> Exklusiver Chat & Talk\n" +
                        "<a:checkorange:896351648601997353> 10.000$ im Casino\n" +
                        "<a:checkorange:896351648601997353> Zugriff auf Werbung in #promotion\n \n", false);

                break;

            case 30:

                Roonie.mainGuild.getManager().getGuild().addRoleToMember(member.getUser(), Roles.LVL30.getRole(guild)).queue();
                embedBuilder.addField("Neue Vorteile", Roles.LVL30.getRole(guild).getAsMention() + " = `Level 30`\n" +
                        "<a:checkyellow:896351651617726484> Eigener Discord Bot + 24/7 Hosting\n" +
                        "<a:checkyellow:896351651617726484> 20.000$ im Casino\n" +
                        "<a:checkyellow:896351651617726484> Zufälliger Steam Key\n" +
                        "<a:checkyellow:896351651617726484> Chat Cosmetic Tier I\n \n", false);

                break;

            case 40:

                Roonie.mainGuild.getManager().getGuild().addRoleToMember(member.getUser(), Roles.LVL40.getRole(guild)).queue();
                embedBuilder.addField("Neue Vorteile", Roles.LVL40.getRole(guild).getAsMention() + " = `Level 40`\n" +
                        "<a:checkpurple:896351653651972116> Giveaways ohne Vorraussetzungen\n" +
                        "<a:checkpurple:896351653651972116> 20.000$ im Casino" +
                        "<a:checkpurple:896351653651972116> Zugriff auf den `/ban` Command", false);

                break;

            case 50:

                Roonie.mainGuild.getManager().getGuild().addRoleToMember(member.getUser(), Roles.LVL50.getRole(guild)).queue();
                embedBuilder.addField("Neue Vorteile", Roles.LVL50.getRole(guild).getAsMention() + " = `Level 50`\n" +
                        "<a:checkgreen:896351654092374086> Alle Booster Vorteile\n" +
                        "<a:checkgreen:896351654092374086> Chat Cosmetic Tier II\n" +
                        "<a:checkgreen:896351654092374086> 30.000$ im Casino", false);

                break;

            case 100:

                Roonie.mainGuild.getManager().getGuild().addRoleToMember(member.getUser(), Roles.LVL100.getRole(guild)).queue();
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

        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.secondary("viewlevels", "Alle Vorteile ansehen!").withEmoji(Emoji.fromFormatted("\uD83C\uDF81")));

        member.getUser().openPrivateChannel().complete().sendTyping().queue();
        member.getUser().openPrivateChannel().complete().sendMessageEmbeds(embedBuilder.build()).setActionRow(buttons).queue();

    }

}
