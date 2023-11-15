package de.splayfer.roonie.level;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.general.Roles;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class LevelInfoCommand extends ListenerAdapter {

    public void onMessageReceived (MessageReceivedEvent event) {

        if (event.isFromGuild()) {

            Guild guild = event.getGuild();

            if (event.getMessage().getContentStripped().startsWith(Roonie.prefix + "levels") || event.getMessage().getContentStripped().startsWith(Roonie.prefix + "level info")) {

                String[] args = event.getMessage().getContentStripped().split(" ");

                if (event.getMessage().getContentStripped().startsWith(Roonie.prefix + "levels") && args.length == 1) {

                    EmbedBuilder bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0xfcc21b);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905401314857611334/banner_level.png");

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(0xfcc21b);
                    embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/883278317753626655/896346916290445312/splayfunity_level.png");
                    embedBuilder.setTitle(":sparkles: Level Übersicht von SPLΛYFUNITY");
                    embedBuilder.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                    embedBuilder.addField(":question: Wie kann ich im Level aufsteigen?", "Du kannst im Level aufsteigen, indem du XP sammelst! Ab einer bestimmten Anzahl von Xp steigst du in ein neues Level auf und kannst neue Vorteile freischalten!\n" +
                            "<a:chat:879356542791598160> `Nachricht senden` = **10 XP**\n" +
                            "<:voice:877158818189033502> `Pro Minute im Voice` = **10XP**\n" +
                            "<a:umfrage:896819438714757151> `Teilnahme an Umfrage` = **70XP**\n" +
                            ":link: `1 Einladung` = 50XP", false);

                    embedBuilder.setDescription("Hier findest du alle wichtigen Tipps & Infos zu unserem Levelsystem!");
                    embedBuilder.addField("", Roles.LVL5.getRole(guild).getAsMention() + " = `Level 5`\n" +
                            "<a:checkblue:896351653236711454> Rechte auf Nicknames ändern\n" +
                            "<a:checkblue:896351653236711454> 5.000$ im Casino\n" +
                            "<a:checkblue:896351653236711454> Zugriff auf Teamler Bewerbungen\n" +
                            "<a:checkblue:896351653236711454> Höher gelistet werden", false);

                    embedBuilder.addField("", Roles.LVL10.getRole(guild).getAsMention() + " = `Level 10`\n" +
                            "<a:checkgreen:896351654092374086> Alle Vorteile von Stufe 5\n" +
                            "<a:checkgreen:896351654092374086> 7.500$ im Casion\n" +
                            "<a:checkgreen:896351654092374086> Eigener animierter Banner \n \n", false);

                    embedBuilder.addField("", Roles.LVL20.getRole(guild).getAsMention() + " = `Level 20`\n" +
                            "<a:checkorange:896351648601997353> Komplett eingerichteter Discord Server\n" +
                            "<a:checkorange:896351648601997353> Exklusiver Chat & Talk\n" +
                            "<a:checkorange:896351648601997353> 10.000$ im Casino\n" +
                            "<a:checkorange:896351648601997353> Zugriff auf Werbung in #promotion\n \n", false);

                    embedBuilder.addField("", Roles.LVL30.getRole(guild).getAsMention() + " = `Level 30`\n" +
                            "<a:checkyellow:896351651617726484> Eigener Discord Bot + 24/7 Hosting\n" +
                            "<a:checkyellow:896351651617726484> 20.000$ im Casino\n" +
                            "<a:checkyellow:896351651617726484> Zufälliger Steam Key\n" +
                            "<a:checkyellow:896351651617726484> Chat Cosmetic Tier I\n \n", false);

                    embedBuilder.addField("", Roles.LVL40.getRole(guild).getAsMention() + " = `Level 40`\n" +
                            "<a:checkpurple:896351653651972116> Giveaways ohne Vorraussetzungen\n" +
                            "<a:checkpurple:896351653651972116> 20.000$ im Casino" +
                            "<a:checkpurple:896351653651972116> Zugriff auf den `/ban` Command", false);

                    embedBuilder.addField("", Roles.LVL50.getRole(guild).getAsMention() + " = `Level 50`\n" +
                            "<a:checkgreen:896351654092374086> Alle Booster Vorteile\n" +
                            "<a:checkgreen:896351654092374086> Chat Cosmetic Tier II\n" +
                            "<a:checkgreen:896351654092374086> 30.000$ im Casino", false);

                    embedBuilder.addField("", Roles.LVL100.getRole(guild).getAsMention() + " = `Level 100`\n" +
                            "<a:checkpink:896351651626106910> Eigene Rolle\n" +
                            "<a:checkpink:896351651626106910> Zugriff auf den `$heck` Command\n" +
                            "<a:checkpink:896351651626106910> 50.000 $ im Casino\n" +
                            "<a:checkpink:896351651626106910> Chat Cosmetic Tier III", false);

                    embedBuilder.setFooter("Alle Vorteile + Rollen sind geistiges Eigentum von SPLΛYFUNITY!");

                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessageEmbeds(bannerEmbed.build()).queue();
                    event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();

                } else if (event.getMessage().getContentStripped().startsWith(Roonie.prefix + "level info") && args.length == 2) {

                    EmbedBuilder bannerEmbed = new EmbedBuilder();
                    bannerEmbed.setColor(0xfcc21b);
                    bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905401314857611334/banner_level.png");

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setColor(0xfcc21b);
                    embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/883278317753626655/896346916290445312/splayfunity_level.png");
                    embedBuilder.setTitle(":sparkles: Level Übersicht von SPLΛYFUNITY");
                    embedBuilder.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                    embedBuilder.addField(":question: Wie kann ich im Level aufsteigen?", "Du kannst im Level aufsteigen, indem du XP sammelst! Ab einer bestimmten Anzahl von Xp steigst du in ein neues Level auf und kannst neue Vorteile freischalten!\n" +
                            "<a:chat:879356542791598160> `Nachricht senden` = **10 XP**\n" +
                            "<:voice:877158818189033502> `Pro Minute im Voice` = **10XP**\n" +
                            "<a:umfrage:896819438714757151> `Teilnahme an Umfrage` = **70XP**\n" +
                            ":link: `1 Einladung` = 50XP", false);

                    embedBuilder.setDescription("Hier findest du alle wichtigen Tipps & Infos zu unserem Levelsystem!");
                    embedBuilder.addField("", Roles.LVL5.getRole(guild).getAsMention() + " = `Level 5`\n" +
                            "<a:checkblue:896351653236711454> Rechte auf Nicknames ändern\n" +
                            "<a:checkblue:896351653236711454> 5.000$ im Casino\n" +
                            "<a:checkblue:896351653236711454> Zugriff auf Teamler Bewerbungen\n" +
                            "<a:checkblue:896351653236711454> Höher gelistet werden", false);

                    embedBuilder.addField("", Roles.LVL10.getRole(guild).getAsMention() + " = `Level 10`\n" +
                            "<a:checkgreen:896351654092374086> Alle Vorteile von Stufe 5\n" +
                            "<a:checkgreen:896351654092374086> 7.500$ im Casion\n" +
                            "<a:checkgreen:896351654092374086> Eigener animierter Banner \n \n", false);

                    embedBuilder.addField("", Roles.LVL20.getRole(guild).getAsMention() + " = `Level 20`\n" +
                            "<a:checkorange:896351648601997353> Komplett eingerichteter Discord Server\n" +
                            "<a:checkorange:896351648601997353> Exklusiver Chat & Talk\n" +
                            "<a:checkorange:896351648601997353> 10.000$ im Casino\n" +
                            "<a:checkorange:896351648601997353> Zugriff auf Werbung in #promotion\n \n", false);

                    embedBuilder.addField("", Roles.LVL30.getRole(guild).getAsMention() + " = `Level 30`\n" +
                            "<a:checkyellow:896351651617726484> Eigener Discord Bot + 24/7 Hosting\n" +
                            "<a:checkyellow:896351651617726484> 20.000$ im Casino\n" +
                            "<a:checkyellow:896351651617726484> Zufälliger Steam Key\n" +
                            "<a:checkyellow:896351651617726484> Chat Cosmetic Tier I\n \n", false);

                    embedBuilder.addField("", Roles.LVL40.getRole(guild).getAsMention() + " = `Level 40`\n" +
                            "<a:checkpurple:896351653651972116> Giveaways ohne Vorraussetzungen\n" +
                            "<a:checkpurple:896351653651972116> 20.000$ im Casino" +
                            "<a:checkpurple:896351653651972116> Zugriff auf den `/ban` Command", false);

                    embedBuilder.addField("", Roles.LVL50.getRole(guild).getAsMention() + " = `Level 50`\n" +
                            "<a:checkgreen:896351654092374086> Alle Booster Vorteile\n" +
                            "<a:checkgreen:896351654092374086> Chat Cosmetic Tier II\n" +
                            "<a:checkgreen:896351654092374086> 30.000$ im Casino", false);

                    embedBuilder.addField("", Roles.LVL100.getRole(guild).getAsMention() + " = `Level 100`\n" +
                            "<a:checkpink:896351651626106910> Eigene Rolle\n" +
                            "<a:checkpink:896351651626106910> Zugriff auf den `$heck` Command\n" +
                            "<a:checkpink:896351651626106910> 50.000 $ im Casino\n" +
                            "<a:checkpink:896351651626106910> Chat Cosmetic Tier III", false);

                    embedBuilder.setFooter("Alle Vorteile + Rollen sind geistiges Eigentum von SPLΛYFUNITY!");

                    event.getChannel().sendTyping().queue();
                    event.getChannel().sendMessageEmbeds(bannerEmbed.build()).queue();
                    event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();

                } else {

                    //building Embed

                    EmbedBuilder embedBuilder = new EmbedBuilder();
                    embedBuilder.setTitle(":exclamation: **Bitte nutze `/levels` oder `/level info`!**");
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

            }

        }

    }

    public void onButtonInteraction (ButtonInteractionEvent event) {

        if (event.getButton().getId().equals("viewlevels")) {

            if (event.isFromGuild()) {

                Guild guild = event.getGuild();

                //per Server

                EmbedBuilder bannerEmbed = new EmbedBuilder();
                bannerEmbed.setColor(0xfcc21b);
                bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905401314857611334/banner_level.png");

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(0xfcc21b);
                embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/883278317753626655/896346916290445312/splayfunity_level.png");
                embedBuilder.setTitle(":sparkles: Level Übersicht von SPLΛYFUNITY");
                embedBuilder.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                embedBuilder.addField(":question: Wie kann ich im Level aufsteigen?", "Du kannst im Level aufsteigen, indem du XP sammelst! Ab einer bestimmten Anzahl von Xp steigst du in ein neues Level auf und kannst neue Vorteile freischalten!\n" +
                        "<a:chat:879356542791598160> `Nachricht senden` = **10 XP**\n" +
                        "<:voice:877158818189033502> `Pro Minute im Voice` = **10XP**\n" +
                        "<a:umfrage:896819438714757151> `Teilnahme an Umfrage` = **70XP**\n" +
                        ":link: `1 Einladung` = 50XP", false);

                embedBuilder.setDescription("Hier findest du alle wichtigen Tipps & Infos zu unserem Levelsystem!");
                embedBuilder.addField("",  Roles.LVL5.getRole(guild).getAsMention() + " = `Level 5`\n" +
                        "<a:checkblue:896351653236711454> Rechte auf Nicknames ändern\n" +
                        "<a:checkblue:896351653236711454> 5.000$ im Casino\n" +
                        "<a:checkblue:896351653236711454> Zugriff auf Teamler Bewerbungen\n" +
                        "<a:checkblue:896351653236711454> Höher gelistet werden", false);

                embedBuilder.addField("", Roles.LVL10.getRole(guild).getAsMention() + " = `Level 10`\n" +
                        "<a:checkgreen:896351654092374086> Alle Vorteile von Stufe 5\n" +
                        "<a:checkgreen:896351654092374086> 7.500$ im Casion\n" +
                        "<a:checkgreen:896351654092374086> Eigener animierter Banner \n \n", false);

                embedBuilder.addField("", Roles.LVL20.getRole(guild).getAsMention() + " = `Level 20`\n" +
                        "<a:checkorange:896351648601997353> Komplett eingerichteter Discord Server\n" +
                        "<a:checkorange:896351648601997353> Exklusiver Chat & Talk\n" +
                        "<a:checkorange:896351648601997353> 10.000$ im Casino\n" +
                        "<a:checkorange:896351648601997353> Zugriff auf Werbung in #promotion\n \n", false);

                embedBuilder.addField("", Roles.LVL30.getRole(guild).getAsMention() + " = `Level 30`\n" +
                        "<a:checkyellow:896351651617726484> Eigener Discord Bot + 24/7 Hosting\n" +
                        "<a:checkyellow:896351651617726484> 20.000$ im Casino\n" +
                        "<a:checkyellow:896351651617726484> Zufälliger Steam Key\n" +
                        "<a:checkyellow:896351651617726484> Chat Cosmetic Tier I\n \n", false);

                embedBuilder.addField("", Roles.LVL40.getRole(guild).getAsMention() + " = `Level 40`\n" +
                        "<a:checkpurple:896351653651972116> Giveaways ohne Vorraussetzungen\n" +
                        "<a:checkpurple:896351653651972116> 20.000$ im Casino" +
                        "<a:checkpurple:896351653651972116> Zugriff auf den `/ban` Command", false);

                embedBuilder.addField("", Roles.LVL50.getRole(guild).getAsMention() + " = `Level 50`\n" +
                        "<a:checkgreen:896351654092374086> Alle Booster Vorteile\n" +
                        "<a:checkgreen:896351654092374086> Chat Cosmetic Tier II\n" +
                        "<a:checkgreen:896351654092374086> 30.000$ im Casino", false);

                embedBuilder.addField("", Roles.LVL100.getRole(guild).getAsMention() + " = `Level 100`\n" +
                        "<a:checkpink:896351651626106910> Eigene Rolle\n" +
                        "<a:checkpink:896351651626106910> Zugriff auf den `$heck` Command\n" +
                        "<a:checkpink:896351651626106910> 50.000 $ im Casino\n" +
                        "<a:checkpink:896351651626106910> Chat Cosmetic Tier III", false);

                embedBuilder.setFooter("Alle Vorteile + Rollen sind geistiges Eigentum von SPLΛYFUNITY!");

                event.getChannel().sendTyping().queue();
                event.getChannel().sendMessageEmbeds(bannerEmbed.build()).queue();
                event.getChannel().sendMessageEmbeds(embedBuilder.build()).queue();

            } else {

                //per DM

                Guild guild = Roonie.mainGuild;

                User target = event.getUser();
                PrivateChannel dm = target.openPrivateChannel().complete();

                EmbedBuilder bannerEmbed = new EmbedBuilder();
                bannerEmbed.setColor(0xfcc21b);
                bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905401314857611334/banner_level.png");

                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(0xfcc21b);
                embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/883278317753626655/896346916290445312/splayfunity_level.png");
                embedBuilder.setTitle(":sparkles: Level Übersicht von SPLΛYFUNITY");
                embedBuilder.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                embedBuilder.addField(":question: Wie kann ich im Level aufsteigen?", "Du kannst im Level aufsteigen, indem du XP sammelst! Ab einer bestimmten Anzahl von Xp steigst du in ein neues Level auf und kannst neue Vorteile freischalten!\n" +
                        "<a:chat:879356542791598160> `Nachricht senden` = **10 XP**\n" +
                        "<:voice:877158818189033502> `Pro Minute im Voice` = **10XP**\n" +
                        "<a:umfrage:896819438714757151> `Teilnahme an Umfrage` = **70XP**\n" +
                        ":link: `1 Einladung` = 50XP", false);

                embedBuilder.setDescription("Hier findest du alle wichtigen Tipps & Infos zu unserem Levelsystem!");
                embedBuilder.addField("",  Roles.LVL5.getRole(guild).getName() + " = `Level 5`\n" +
                        "<a:checkblue:896351653236711454> Rechte auf Nicknames ändern\n" +
                        "<a:checkblue:896351653236711454> 5.000$ im Casino\n" +
                        "<a:checkblue:896351653236711454> Zugriff auf Teamler Bewerbungen\n" +
                        "<a:checkblue:896351653236711454> Höher gelistet werden", false);

                embedBuilder.addField("", Roles.LVL10.getRole(guild).getName() + " = `Level 10`\n" +
                        "<a:checkgreen:896351654092374086> Alle Vorteile von Stufe 5\n" +
                        "<a:checkgreen:896351654092374086> 7.500$ im Casino\n" +
                        "<a:checkgreen:896351654092374086> Eigener animierter Banner \n \n", false);

                embedBuilder.addField("", Roles.LVL20.getRole(guild).getName() + " = `Level 20`\n" +
                        "<a:checkorange:896351648601997353> Komplett eingerichteter Discord Server\n" +
                        "<a:checkorange:896351648601997353> Exklusiver Chat & Talk\n" +
                        "<a:checkorange:896351648601997353> 10.000$ im Casino\n" +
                        "<a:checkorange:896351648601997353> Zugriff auf Werbung in #promotion\n \n", false);

                embedBuilder.addField("", Roles.LVL30.getRole(guild).getName() + " = `Level 30`\n" +
                        "<a:checkyellow:896351651617726484> Erstellen von Diskussionen (Threads)\n" +
                        "<a:checkyellow:896351651617726484> Eigener Discord Bot + 24/7 Hosting\n" +
                        "<a:checkyellow:896351651617726484> 20.000$ im Casino\n" +
                        "<a:checkyellow:896351651617726484> Zufälliger Steam Key\n" +
                        "<a:checkyellow:896351651617726484> Chat Cosmetic Tier I\n \n", false);

                embedBuilder.addField("", Roles.LVL40.getRole(guild).getName() + " = `Level 40`\n" +
                        "<a:checkpurple:896351653651972116> Giveaways ohne Vorraussetzungen\n" +
                        "<a:checkpurple:896351653651972116> 20.000$ im Casino\n" +
                        "<a:checkpurple:896351653651972116> Zugriff auf den `/ban` Command", false);

                embedBuilder.addField("", Roles.LVL50.getRole(guild).getName() + " = `Level 50`\n" +
                        "<a:checkgreen:896351654092374086> Alle Booster Vorteile\n" +
                        "<a:checkgreen:896351654092374086> Chat Cosmetic Tier II\n" +
                        "<a:checkgreen:896351654092374086> 30.000$ im Casino", false);

                embedBuilder.addField("", Roles.LVL100.getRole(guild).getName() + " = `Level 100`\n" +
                        "<a:checkpink:896351651626106910> Eigene Rolle\n" +
                        "<a:checkpink:896351651626106910> Zugriff auf den `$heck` Command\n" +
                        "<a:checkpink:896351651626106910> 50.000 $ im Casino\n" +
                        "<a:checkpink:896351651626106910> Chat Cosmetic Tier III", false);

                embedBuilder.setFooter("Alle Vorteile + Rollen sind geistiges Eigentum von SPLΛYFUNITY!");

                dm.sendTyping().queue();
                dm.sendMessageEmbeds(bannerEmbed.build()).queue();
                dm.sendMessageEmbeds(embedBuilder.build()).queue();

            }

        }
    }

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {

        if (event.getName().equals("levels")) {

            Guild guild = event.getGuild();

            //per Server

            EmbedBuilder bannerEmbed = new EmbedBuilder();
            bannerEmbed.setColor(0xfcc21b);
            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905401314857611334/banner_level.png");

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0xfcc21b);
            embedBuilder.setThumbnail("https://cdn.discordapp.com/attachments/883278317753626655/896346916290445312/splayfunity_level.png");
            embedBuilder.setTitle(":sparkles: Level Übersicht von SPLΛYFUNITY");
            embedBuilder.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

            embedBuilder.addField(":question: Wie kann ich im Level aufsteigen?", "Du kannst im Level aufsteigen, indem du XP sammelst! Ab einer bestimmten Anzahl von Xp steigst du in ein neues Level auf und kannst neue Vorteile freischalten!\n" +
                    "<a:chat:879356542791598160> `Nachricht senden` = **10 XP**\n" +
                    "<:voice:877158818189033502> `Pro Minute im Voice` = **10XP**\n" +
                    "<a:umfrage:896819438714757151> `Teilnahme an Umfrage` = **70XP**\n" +
                    ":link: `1 Einladung` = 50XP", false);

            embedBuilder.setDescription("Hier findest du alle wichtigen Tipps & Infos zu unserem Levelsystem!");
            embedBuilder.addField("",  Roles.LVL5.getRole(guild).getAsMention() + " = `Level 5`\n" +
                    "<a:checkblue:896351653236711454> Rechte auf Nicknames ändern\n" +
                    "<a:checkblue:896351653236711454> 5.000$ im Casino\n" +
                    "<a:checkblue:896351653236711454> Zugriff auf Teamler Bewerbungen\n" +
                    "<a:checkblue:896351653236711454> Höher gelistet werden", false);

            embedBuilder.addField("", Roles.LVL10.getRole(guild).getAsMention() + " = `Level 10`\n" +
                    "<a:checkgreen:896351654092374086> Alle Vorteile von Stufe 5\n" +
                    "<a:checkgreen:896351654092374086> 7.500$ im Casion\n" +
                    "<a:checkgreen:896351654092374086> Eigener animierter Banner \n \n", false);

            embedBuilder.addField("", Roles.LVL20.getRole(guild).getAsMention() + " = `Level 20`\n" +
                    "<a:checkorange:896351648601997353> Komplett eingerichteter Discord Server\n" +
                    "<a:checkorange:896351648601997353> Exklusiver Chat & Talk\n" +
                    "<a:checkorange:896351648601997353> 10.000$ im Casino\n" +
                    "<a:checkorange:896351648601997353> Zugriff auf Werbung in #promotion\n \n", false);

            embedBuilder.addField("", Roles.LVL30.getRole(guild).getAsMention() + " = `Level 30`\n" +
                    "<a:checkyellow:896351651617726484> Eigener Discord Bot + 24/7 Hosting\n" +
                    "<a:checkyellow:896351651617726484> 20.000$ im Casino\n" +
                    "<a:checkyellow:896351651617726484> Zufälliger Steam Key\n" +
                    "<a:checkyellow:896351651617726484> Chat Cosmetic Tier I\n \n", false);

            embedBuilder.addField("", Roles.LVL40.getRole(guild).getAsMention() + " = `Level 40`\n" +
                    "<a:checkpurple:896351653651972116> Giveaways ohne Vorraussetzungen\n" +
                    "<a:checkpurple:896351653651972116> 20.000$ im Casino" +
                    "<a:checkpurple:896351653651972116> Zugriff auf den `/ban` Command", false);

            embedBuilder.addField("", Roles.LVL50.getRole(guild).getAsMention() + " = `Level 50`\n" +
                    "<a:checkgreen:896351654092374086> Alle Booster Vorteile\n" +
                    "<a:checkgreen:896351654092374086> Chat Cosmetic Tier II\n" +
                    "<a:checkgreen:896351654092374086> 30.000$ im Casino", false);

            embedBuilder.addField("", Roles.LVL100.getRole(guild).getAsMention() + " = `Level 100`\n" +
                    "<a:checkpink:896351651626106910> Eigene Rolle\n" +
                    "<a:checkpink:896351651626106910> Zugriff auf den `$heck` Command\n" +
                    "<a:checkpink:896351651626106910> 50.000 $ im Casino\n" +
                    "<a:checkpink:896351651626106910> Chat Cosmetic Tier III", false);

            embedBuilder.setFooter("Alle Vorteile + Rollen sind geistiges Eigentum von SPLΛYFUNITY!");

            event.replyEmbeds(bannerEmbed.build(), embedBuilder.build()).setEphemeral(true).queue();

        }

    }

}