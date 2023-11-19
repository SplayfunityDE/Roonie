package de.splayfer.roonie.poll;

import de.splayfer.roonie.FileSystem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PollEnterListener extends ListenerAdapter {

    protected Role adminrole;
    protected Role modrole;
    protected Role suprole;
    protected Role contentrole;

    List<String> trueList;
    List<String> noneList;
    List<String> falseList;

    EmbedBuilder replaceEmbed;

    protected File umfrageLog = new File(FileSystem.UmfrageLog.getAbsolutePath());
    protected YamlConfiguration yml;

    protected File umfrageButtonLog = new File(FileSystem.UmfrageButtonLog.getAbsolutePath());
    protected YamlConfiguration buttonyml;

    protected EmbedBuilder mainbannerEmbed;

    public void onButtonInteraction (ButtonInteractionEvent event) {

        if(!event.getChannelType().isGuild()) {
            return;
        }

        TextChannel umfrageChannel = event.getGuild().getTextChannelById("880717025255751721");

        if (event.getChannel() == umfrageChannel) {

            adminrole = event.getGuild().getRoleById("873515181114806272");
            modrole = event.getGuild().getRoleById("873508502063169556");
            suprole = event.getGuild().getRoleById("873515229470928937");
            contentrole = event.getGuild().getRoleById("902610342700519455");

            yml = YamlConfiguration.loadConfiguration(umfrageLog);
            buttonyml = YamlConfiguration.loadConfiguration(umfrageButtonLog);

            mainbannerEmbed = new EmbedBuilder();
            mainbannerEmbed.setColor(0x28346d);
            mainbannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/910194455494144021/banner_umfrage.png");

            if (!umfrageLog.exists()) {

                try {
                    umfrageLog.createNewFile();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            }

            if (!umfrageButtonLog.exists()) {

                try {
                    umfrageButtonLog.createNewFile();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            }

            if (yml.contains(event.getMessage().getId())) {

                EmbedBuilder bannerEmbed;
                EmbedBuilder reply;

                switch (event.getButton().getId()) {

                    case "true":

                        if (buttonyml.contains(event.getMessage().getId() + ".true")) {
                            trueList = buttonyml.getStringList(event.getMessage().getId() + ".true");
                        } else {
                            trueList = new ArrayList<>();
                        }

                        if (buttonyml.contains(event.getMessage().getId() + ".none")) {
                            noneList = buttonyml.getStringList(event.getMessage().getId() + ".none");
                        } else {
                            noneList = new ArrayList<>();
                        }

                        if (buttonyml.contains(event.getMessage().getId() + ".none")) {
                            falseList = buttonyml.getStringList(event.getMessage().getId() + ".false");
                        } else {
                            falseList = new ArrayList<>();
                        }

                        //checking for user

                        if (trueList.contains(event.getMember().getId())) {

                            //bereits abgestimmt

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0xed4245);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0xed4245);
                            reply.setTitle(":no_entry_sign: Auswahl bereits getroffen!");
                            reply.setDescription("Du bist nicht mehr in der Lage, diese Option auszuwählen, da du dies bereits ausgewählt hast!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else if (noneList.contains(event.getMember().getId())) {

                            //Stimme ändern

                            noneList.remove(event.getMember().getId());
                            trueList.add(event.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich geändert!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else if (falseList.contains(event.getMember().getId())) {

                            //Stimme ändern

                            falseList.remove(event.getMember().getId());
                            trueList.add(event.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich geändert!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else {

                            //Stimme hinzufügen

                            trueList.add(event.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich hinzugefügt!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        }

                        if (yml.getBoolean(event.getMessage().getId() + ".showvoting")) {

                            replaceEmbed = new EmbedBuilder();
                            replaceEmbed.setColor(event.getMessage().getEmbeds().get(1).getColor());
                            replaceEmbed.setAuthor(event.getMessage().getEmbeds().get(1).getAuthor().getName(), event.getMessage().getEmbeds().get(1).getAuthor().getUrl(), event.getMessage().getEmbeds().get(1).getAuthor().getIconUrl());
                            replaceEmbed.setTitle(event.getMessage().getEmbeds().get(1).getTitle());
                            replaceEmbed.addField(event.getMessage().getEmbeds().get(1).getFields().get(0).getName(), event.getMessage().getEmbeds().get(1).getFields().get(0).getValue(), false);
                            replaceEmbed.addField(event.getMessage().getEmbeds().get(1).getFields().get(1).getName(), event.getMessage().getEmbeds().get(1).getFields().get(1).getValue(), true);
                            replaceEmbed.addField(event.getMessage().getEmbeds().get(1).getFields().get(2).getName(), event.getMessage().getEmbeds().get(1).getFields().get(2).getValue(), true);
                            replaceEmbed.addField(event.getMessage().getEmbeds().get(1).getFields().get(3).getName(), "", false);
                            replaceEmbed.setImage(event.getMessage().getEmbeds().get(1).getImage().getUrl());

                            List<Button> buttons = new ArrayList<>();
                            buttons.add(Button.success("true", yml.getString(event.getMessage().getId() + ".buttontrue") + " (" + trueList.size() + ")").withEmoji(event.getMessage().getActionRows().get(0).getButtons().get(0).getEmoji()));
                            buttons.add(Button.secondary("none", yml.getString(event.getMessage().getId() + ".buttonnone") + " (" + noneList.size() + ")").withEmoji(event.getMessage().getActionRows().get(0).getButtons().get(1).getEmoji()));
                            buttons.add(Button.danger("false", yml.getString(event.getMessage().getId() + ".buttonfalse") + " (" + falseList.size() + ")").withEmoji(event.getMessage().getActionRows().get(0).getButtons().get(2).getEmoji()));

                            event.replyEmbeds(bannerEmbed.build(), reply.build()).setEphemeral(true).queue();
                            event.getMessage().editMessageEmbeds(mainbannerEmbed.build(), replaceEmbed.build()).setActionRow(buttons).queue();

                        }

                        buttonyml.set(event.getMessage().getId() + ".true", trueList);
                        buttonyml.set(event.getMessage().getId() + ".none", noneList);
                        buttonyml.set(event.getMessage().getId() + ".false", falseList);

                        try {
                            buttonyml.save(umfrageButtonLog);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }

                        break;

                    case "none":

                        if (buttonyml.contains(event.getMessage().getId() + ".true")) {
                            trueList = buttonyml.getStringList(event.getMessage().getId() + ".true");
                        } else {
                            trueList = new ArrayList<>();
                        }

                        if (buttonyml.contains(event.getMessage().getId() + ".none")) {
                            noneList = buttonyml.getStringList(event.getMessage().getId() + ".none");
                        } else {
                            noneList = new ArrayList<>();
                        }

                        if (buttonyml.contains(event.getMessage().getId() + ".none")) {
                            falseList = buttonyml.getStringList(event.getMessage().getId() + ".false");
                        } else {
                            falseList = new ArrayList<>();
                        }

                        //checking for user

                        if (trueList.contains(event.getMember().getId())) {

                            //Stimme ändern

                            trueList.remove(event.getMember().getId());
                            noneList.add(event.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich geändert!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else if (noneList.contains(event.getMember().getId())) {

                            //bereits abgestimmt

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0xed4245);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0xed4245);
                            reply.setTitle(":no_entry_sign: Auswahl bereits getroffen!");
                            reply.setDescription("Du bist nicht mehr in der Lage, diese Option auszuwählen, da du dies bereits ausgewählt hast!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else if (falseList.contains(event.getMember().getId())) {

                            //Stimme ändern

                            falseList.remove(event.getMember().getId());
                            noneList.add(event.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich geändert!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else {

                            //Stimme hinzufügen

                            noneList.add(event.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich hinzugefügt!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        }

                        if (yml.getBoolean(event.getMessage().getId() + ".showvoting")) {

                            replaceEmbed = new EmbedBuilder();
                            replaceEmbed.setColor(event.getMessage().getEmbeds().get(1).getColor());
                            replaceEmbed.setAuthor(event.getMessage().getEmbeds().get(1).getAuthor().getName(), event.getMessage().getEmbeds().get(1).getAuthor().getUrl(), event.getMessage().getEmbeds().get(1).getAuthor().getIconUrl());
                            replaceEmbed.setTitle(event.getMessage().getEmbeds().get(1).getTitle());
                            replaceEmbed.addField(event.getMessage().getEmbeds().get(1).getFields().get(0).getName(), event.getMessage().getEmbeds().get(1).getFields().get(0).getValue(), false);
                            replaceEmbed.addField(event.getMessage().getEmbeds().get(1).getFields().get(1).getName(), event.getMessage().getEmbeds().get(1).getFields().get(1).getValue(), true);
                            replaceEmbed.addField(event.getMessage().getEmbeds().get(1).getFields().get(2).getName(), event.getMessage().getEmbeds().get(1).getFields().get(2).getValue(), true);
                            replaceEmbed.addField(event.getMessage().getEmbeds().get(1).getFields().get(3).getName(), "", false);
                            replaceEmbed.setImage(event.getMessage().getEmbeds().get(1).getImage().getUrl());

                            List<Button> buttons = new ArrayList<>();
                            buttons.add(Button.success("true", yml.getString(event.getMessage().getId() + ".buttontrue") + " (" + trueList.size() + ")").withEmoji(event.getMessage().getActionRows().get(0).getButtons().get(0).getEmoji()));
                            buttons.add(Button.secondary("none", yml.getString(event.getMessage().getId() + ".buttonnone") + " (" + noneList.size() + ")").withEmoji(event.getMessage().getActionRows().get(0).getButtons().get(1).getEmoji()));
                            buttons.add(Button.danger("false", yml.getString(event.getMessage().getId() + ".buttonfalse") + " (" + falseList.size() + ")").withEmoji(event.getMessage().getActionRows().get(0).getButtons().get(2).getEmoji()));

                            event.replyEmbeds(bannerEmbed.build(), reply.build()).setEphemeral(true).queue();
                            event.getMessage().editMessageEmbeds(mainbannerEmbed.build(), replaceEmbed.build()).setActionRow(buttons).queue();

                        }

                        buttonyml.set(event.getMessage().getId() + ".true", trueList);
                        buttonyml.set(event.getMessage().getId() + ".none", noneList);
                        buttonyml.set(event.getMessage().getId() + ".false", falseList);

                        try {
                            buttonyml.save(umfrageButtonLog);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }

                        break;

                    case "false":

                        if (buttonyml.contains(event.getMessage().getId() + ".true")) {
                            trueList = buttonyml.getStringList(event.getMessage().getId() + ".true");
                        } else {
                            trueList = new ArrayList<>();
                        }

                        if (buttonyml.contains(event.getMessage().getId() + ".none")) {
                            noneList = buttonyml.getStringList(event.getMessage().getId() + ".none");
                        } else {
                            noneList = new ArrayList<>();
                        }

                        if (buttonyml.contains(event.getMessage().getId() + ".none")) {
                            falseList = buttonyml.getStringList(event.getMessage().getId() + ".false");
                        } else {
                            falseList = new ArrayList<>();
                        }

                        //checking for user

                        if (trueList.contains(event.getMember().getId())) {

                            //Stimme ändern

                            trueList.remove(event.getMember().getId());
                            falseList.add(event.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich geändert!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else if (noneList.contains(event.getMember().getId())) {

                            //Stimme ändern

                            noneList.remove(event.getMember().getId());
                            falseList.add(event.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich geändert!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else if (falseList.contains(event.getMember().getId())) {

                            //bereits abgestimmt

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0xed4245);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0xed4245);
                            reply.setTitle(":no_entry_sign: Auswahl bereits getroffen!");
                            reply.setDescription("Du bist nicht mehr in der Lage, diese Option auszuwählen, da du dies bereits ausgewählt hast!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else {

                            //Stimme hinzufügen

                            falseList.add(event.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich hinzugefügt!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        }

                        if (yml.getBoolean(event.getMessage().getId() + ".showvoting")) {

                            replaceEmbed = new EmbedBuilder();
                            replaceEmbed.setColor(event.getMessage().getEmbeds().get(1).getColor());
                            replaceEmbed.setAuthor(event.getMessage().getEmbeds().get(1).getAuthor().getName(), event.getMessage().getEmbeds().get(1).getAuthor().getUrl(), event.getMessage().getEmbeds().get(1).getAuthor().getIconUrl());
                            replaceEmbed.setTitle(event.getMessage().getEmbeds().get(1).getTitle());
                            replaceEmbed.addField(event.getMessage().getEmbeds().get(1).getFields().get(0).getName(), event.getMessage().getEmbeds().get(1).getFields().get(0).getValue(), false);
                            replaceEmbed.addField(event.getMessage().getEmbeds().get(1).getFields().get(1).getName(), event.getMessage().getEmbeds().get(1).getFields().get(1).getValue(), true);
                            replaceEmbed.addField(event.getMessage().getEmbeds().get(1).getFields().get(2).getName(), event.getMessage().getEmbeds().get(1).getFields().get(2).getValue(), true);
                            replaceEmbed.addField(event.getMessage().getEmbeds().get(1).getFields().get(3).getName(), "", false);
                            replaceEmbed.setImage(event.getMessage().getEmbeds().get(1).getImage().getUrl());

                            List<Button> buttons = new ArrayList<>();
                            buttons.add(Button.success("true", yml.getString(event.getMessage().getId() + ".buttontrue") + " (" + trueList.size() + ")").withEmoji(event.getMessage().getActionRows().get(0).getButtons().get(0).getEmoji()));
                            buttons.add(Button.secondary("none", yml.getString(event.getMessage().getId() + ".buttonnone") + " (" + noneList.size() + ")").withEmoji(event.getMessage().getActionRows().get(0).getButtons().get(1).getEmoji()));
                            buttons.add(Button.danger("false", yml.getString(event.getMessage().getId() + ".buttonfalse") + " (" + falseList.size() + ")").withEmoji(event.getMessage().getActionRows().get(0).getButtons().get(2).getEmoji()));

                            event.replyEmbeds(bannerEmbed.build(), replaceEmbed.build()).setEphemeral(true).queue();
                            event.getMessage().editMessageEmbeds(mainbannerEmbed.build(), replaceEmbed.build()).setActionRow(buttons).queue();

                        }

                        buttonyml.set(event.getMessage().getId() + ".true", trueList);
                        buttonyml.set(event.getMessage().getId() + ".none", noneList);
                        buttonyml.set(event.getMessage().getId() + ".false", falseList);

                        try {
                            buttonyml.save(umfrageButtonLog);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }

                        break;

                }

            }

        }

    }

}
