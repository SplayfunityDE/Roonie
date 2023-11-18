package de.splayfer.roonie.poll;

import de.splayfer.roonie.FileSystem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
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

    public void onButtonInteraction (ButtonInteractionEvent buttonClickEvent) {

        if(!buttonClickEvent.getChannelType().isGuild()) {
            return;
        }

        TextChannel umfrageChannel = buttonClickEvent.getGuild().getTextChannelById("880717025255751721");

        if (buttonClickEvent.getChannel() == umfrageChannel) {

            adminrole = buttonClickEvent.getGuild().getRoleById("873515181114806272");
            modrole = buttonClickEvent.getGuild().getRoleById("873508502063169556");
            suprole = buttonClickEvent.getGuild().getRoleById("873515229470928937");
            contentrole = buttonClickEvent.getGuild().getRoleById("902610342700519455");

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

            if (yml.contains(buttonClickEvent.getMessage().getId())) {

                EmbedBuilder bannerEmbed;
                EmbedBuilder reply;

                switch (buttonClickEvent.getButton().getId()) {

                    case "true":

                        if (buttonyml.contains(buttonClickEvent.getMessage().getId() + ".true")) {
                            trueList = buttonyml.getStringList(buttonClickEvent.getMessage().getId() + ".true");
                        } else {
                            trueList = new ArrayList<>();
                        }

                        if (buttonyml.contains(buttonClickEvent.getMessage().getId() + ".none")) {
                            noneList = buttonyml.getStringList(buttonClickEvent.getMessage().getId() + ".none");
                        } else {
                            noneList = new ArrayList<>();
                        }

                        if (buttonyml.contains(buttonClickEvent.getMessage().getId() + ".none")) {
                            falseList = buttonyml.getStringList(buttonClickEvent.getMessage().getId() + ".false");
                        } else {
                            falseList = new ArrayList<>();
                        }

                        //checking for user

                        if (trueList.contains(buttonClickEvent.getMember().getId())) {

                            //bereits abgestimmt

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0xed4245);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0xed4245);
                            reply.setTitle(":no_entry_sign: Auswahl bereits getroffen!");
                            reply.setDescription("Du bist nicht mehr in der Lage, diese Option auszuwählen, da du dies bereits ausgewählt hast!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else if (noneList.contains(buttonClickEvent.getMember().getId())) {

                            //Stimme ändern

                            noneList.remove(buttonClickEvent.getMember().getId());
                            trueList.add(buttonClickEvent.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich geändert!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else if (falseList.contains(buttonClickEvent.getMember().getId())) {

                            //Stimme ändern

                            falseList.remove(buttonClickEvent.getMember().getId());
                            trueList.add(buttonClickEvent.getMember().getId());

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

                            trueList.add(buttonClickEvent.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich hinzugefügt!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        }

                        if (yml.getBoolean(buttonClickEvent.getMessage().getId() + ".showvoting")) {

                            replaceEmbed = new EmbedBuilder();
                            replaceEmbed.setColor(buttonClickEvent.getMessage().getEmbeds().get(1).getColor());
                            replaceEmbed.setAuthor(buttonClickEvent.getMessage().getEmbeds().get(1).getAuthor().getName(), buttonClickEvent.getMessage().getEmbeds().get(1).getAuthor().getUrl(), buttonClickEvent.getMessage().getEmbeds().get(1).getAuthor().getIconUrl());
                            replaceEmbed.setTitle(buttonClickEvent.getMessage().getEmbeds().get(1).getTitle());
                            replaceEmbed.addField(buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(0).getName(), buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(0).getValue(), false);
                            replaceEmbed.addField(buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(1).getName(), buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(1).getValue(), true);
                            replaceEmbed.addField(buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(2).getName(), buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(2).getValue(), true);
                            replaceEmbed.addField(buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(3).getName(), "", false);
                            replaceEmbed.setImage(buttonClickEvent.getMessage().getEmbeds().get(1).getImage().getUrl());

                            List<Button> buttons = new ArrayList<>();
                            buttons.add(Button.success("true", yml.getString(buttonClickEvent.getMessage().getId() + ".buttontrue") + " (" + trueList.size() + ")").withEmoji(buttonClickEvent.getMessage().getActionRows().get(0).getButtons().get(0).getEmoji()));
                            buttons.add(Button.secondary("none", yml.getString(buttonClickEvent.getMessage().getId() + ".buttonnone") + " (" + noneList.size() + ")").withEmoji(buttonClickEvent.getMessage().getActionRows().get(0).getButtons().get(1).getEmoji()));
                            buttons.add(Button.danger("false", yml.getString(buttonClickEvent.getMessage().getId() + ".buttonfalse") + " (" + falseList.size() + ")").withEmoji(buttonClickEvent.getMessage().getActionRows().get(0).getButtons().get(2).getEmoji()));

                            buttonClickEvent.replyEmbeds(bannerEmbed.build(), reply.build()).setEphemeral(true).queue();
                            buttonClickEvent.getMessage().editMessageEmbeds(mainbannerEmbed.build(), replaceEmbed.build()).setActionRow(buttons).queue();

                        }

                        buttonyml.set(buttonClickEvent.getMessage().getId() + ".true", trueList);
                        buttonyml.set(buttonClickEvent.getMessage().getId() + ".none", noneList);
                        buttonyml.set(buttonClickEvent.getMessage().getId() + ".false", falseList);

                        try {
                            buttonyml.save(umfrageButtonLog);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }

                        break;

                    case "none":

                        if (buttonyml.contains(buttonClickEvent.getMessage().getId() + ".true")) {
                            trueList = buttonyml.getStringList(buttonClickEvent.getMessage().getId() + ".true");
                        } else {
                            trueList = new ArrayList<>();
                        }

                        if (buttonyml.contains(buttonClickEvent.getMessage().getId() + ".none")) {
                            noneList = buttonyml.getStringList(buttonClickEvent.getMessage().getId() + ".none");
                        } else {
                            noneList = new ArrayList<>();
                        }

                        if (buttonyml.contains(buttonClickEvent.getMessage().getId() + ".none")) {
                            falseList = buttonyml.getStringList(buttonClickEvent.getMessage().getId() + ".false");
                        } else {
                            falseList = new ArrayList<>();
                        }

                        //checking for user

                        if (trueList.contains(buttonClickEvent.getMember().getId())) {

                            //Stimme ändern

                            trueList.remove(buttonClickEvent.getMember().getId());
                            noneList.add(buttonClickEvent.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich geändert!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else if (noneList.contains(buttonClickEvent.getMember().getId())) {

                            //bereits abgestimmt

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0xed4245);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0xed4245);
                            reply.setTitle(":no_entry_sign: Auswahl bereits getroffen!");
                            reply.setDescription("Du bist nicht mehr in der Lage, diese Option auszuwählen, da du dies bereits ausgewählt hast!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else if (falseList.contains(buttonClickEvent.getMember().getId())) {

                            //Stimme ändern

                            falseList.remove(buttonClickEvent.getMember().getId());
                            noneList.add(buttonClickEvent.getMember().getId());

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

                            noneList.add(buttonClickEvent.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich hinzugefügt!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        }

                        if (yml.getBoolean(buttonClickEvent.getMessage().getId() + ".showvoting")) {

                            replaceEmbed = new EmbedBuilder();
                            replaceEmbed.setColor(buttonClickEvent.getMessage().getEmbeds().get(1).getColor());
                            replaceEmbed.setAuthor(buttonClickEvent.getMessage().getEmbeds().get(1).getAuthor().getName(), buttonClickEvent.getMessage().getEmbeds().get(1).getAuthor().getUrl(), buttonClickEvent.getMessage().getEmbeds().get(1).getAuthor().getIconUrl());
                            replaceEmbed.setTitle(buttonClickEvent.getMessage().getEmbeds().get(1).getTitle());
                            replaceEmbed.addField(buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(0).getName(), buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(0).getValue(), false);
                            replaceEmbed.addField(buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(1).getName(), buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(1).getValue(), true);
                            replaceEmbed.addField(buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(2).getName(), buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(2).getValue(), true);
                            replaceEmbed.addField(buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(3).getName(), "", false);
                            replaceEmbed.setImage(buttonClickEvent.getMessage().getEmbeds().get(1).getImage().getUrl());

                            List<Button> buttons = new ArrayList<>();
                            buttons.add(Button.success("true", yml.getString(buttonClickEvent.getMessage().getId() + ".buttontrue") + " (" + trueList.size() + ")").withEmoji(buttonClickEvent.getMessage().getActionRows().get(0).getButtons().get(0).getEmoji()));
                            buttons.add(Button.secondary("none", yml.getString(buttonClickEvent.getMessage().getId() + ".buttonnone") + " (" + noneList.size() + ")").withEmoji(buttonClickEvent.getMessage().getActionRows().get(0).getButtons().get(1).getEmoji()));
                            buttons.add(Button.danger("false", yml.getString(buttonClickEvent.getMessage().getId() + ".buttonfalse") + " (" + falseList.size() + ")").withEmoji(buttonClickEvent.getMessage().getActionRows().get(0).getButtons().get(2).getEmoji()));

                            buttonClickEvent.replyEmbeds(bannerEmbed.build(), reply.build()).setEphemeral(true).queue();
                            buttonClickEvent.getMessage().editMessageEmbeds(mainbannerEmbed.build(), replaceEmbed.build()).setActionRow(buttons).queue();

                        }

                        buttonyml.set(buttonClickEvent.getMessage().getId() + ".true", trueList);
                        buttonyml.set(buttonClickEvent.getMessage().getId() + ".none", noneList);
                        buttonyml.set(buttonClickEvent.getMessage().getId() + ".false", falseList);

                        try {
                            buttonyml.save(umfrageButtonLog);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }

                        break;

                    case "false":

                        if (buttonyml.contains(buttonClickEvent.getMessage().getId() + ".true")) {
                            trueList = buttonyml.getStringList(buttonClickEvent.getMessage().getId() + ".true");
                        } else {
                            trueList = new ArrayList<>();
                        }

                        if (buttonyml.contains(buttonClickEvent.getMessage().getId() + ".none")) {
                            noneList = buttonyml.getStringList(buttonClickEvent.getMessage().getId() + ".none");
                        } else {
                            noneList = new ArrayList<>();
                        }

                        if (buttonyml.contains(buttonClickEvent.getMessage().getId() + ".none")) {
                            falseList = buttonyml.getStringList(buttonClickEvent.getMessage().getId() + ".false");
                        } else {
                            falseList = new ArrayList<>();
                        }

                        //checking for user

                        if (trueList.contains(buttonClickEvent.getMember().getId())) {

                            //Stimme ändern

                            trueList.remove(buttonClickEvent.getMember().getId());
                            falseList.add(buttonClickEvent.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich geändert!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else if (noneList.contains(buttonClickEvent.getMember().getId())) {

                            //Stimme ändern

                            noneList.remove(buttonClickEvent.getMember().getId());
                            falseList.add(buttonClickEvent.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich geändert!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        } else if (falseList.contains(buttonClickEvent.getMember().getId())) {

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

                            falseList.add(buttonClickEvent.getMember().getId());

                            bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0x43b480);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");

                            reply = new EmbedBuilder();
                            reply.setColor(0x43b480);
                            reply.setTitle(":bar_chart: Stimme erfolgreich hinzugefügt!");
                            reply.setDescription("Du hast deine Stimme für diese Option erfolgreich abgegeben!");
                            reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                        }

                        if (yml.getBoolean(buttonClickEvent.getMessage().getId() + ".showvoting")) {

                            replaceEmbed = new EmbedBuilder();
                            replaceEmbed.setColor(buttonClickEvent.getMessage().getEmbeds().get(1).getColor());
                            replaceEmbed.setAuthor(buttonClickEvent.getMessage().getEmbeds().get(1).getAuthor().getName(), buttonClickEvent.getMessage().getEmbeds().get(1).getAuthor().getUrl(), buttonClickEvent.getMessage().getEmbeds().get(1).getAuthor().getIconUrl());
                            replaceEmbed.setTitle(buttonClickEvent.getMessage().getEmbeds().get(1).getTitle());
                            replaceEmbed.addField(buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(0).getName(), buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(0).getValue(), false);
                            replaceEmbed.addField(buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(1).getName(), buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(1).getValue(), true);
                            replaceEmbed.addField(buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(2).getName(), buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(2).getValue(), true);
                            replaceEmbed.addField(buttonClickEvent.getMessage().getEmbeds().get(1).getFields().get(3).getName(), "", false);
                            replaceEmbed.setImage(buttonClickEvent.getMessage().getEmbeds().get(1).getImage().getUrl());

                            List<Button> buttons = new ArrayList<>();
                            buttons.add(Button.success("true", yml.getString(buttonClickEvent.getMessage().getId() + ".buttontrue") + " (" + trueList.size() + ")").withEmoji(buttonClickEvent.getMessage().getActionRows().get(0).getButtons().get(0).getEmoji()));
                            buttons.add(Button.secondary("none", yml.getString(buttonClickEvent.getMessage().getId() + ".buttonnone") + " (" + noneList.size() + ")").withEmoji(buttonClickEvent.getMessage().getActionRows().get(0).getButtons().get(1).getEmoji()));
                            buttons.add(Button.danger("false", yml.getString(buttonClickEvent.getMessage().getId() + ".buttonfalse") + " (" + falseList.size() + ")").withEmoji(buttonClickEvent.getMessage().getActionRows().get(0).getButtons().get(2).getEmoji()));

                            buttonClickEvent.replyEmbeds(bannerEmbed.build(), replaceEmbed.build()).setEphemeral(true).queue();
                            buttonClickEvent.getMessage().editMessageEmbeds(mainbannerEmbed.build(), replaceEmbed.build()).setActionRow(buttons).queue();

                        }

                        buttonyml.set(buttonClickEvent.getMessage().getId() + ".true", trueList);
                        buttonyml.set(buttonClickEvent.getMessage().getId() + ".none", noneList);
                        buttonyml.set(buttonClickEvent.getMessage().getId() + ".false", falseList);

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
