package de.splayfer.roonie.modules.level;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class LevelCommand extends ListenerAdapter {

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {

        if (event.getName().equals("level")) {
            Member member = event.getOption("nutzer").getAsMember();
            int amount = event.getOption("anzahl").getAsInt();
            EmbedBuilder bb = new EmbedBuilder();
            EmbedBuilder mb = new EmbedBuilder();
            int oldvalue = LevelManager.getLevel(member);
            switch (event.getSubcommandName()) {

                case "add":
                    LevelManager.addLevelToUser(member, amount);
                    mb.setTitle(":white_check_mark: **LEVEL ERFOLGREICH HINZUGEFÜGT!**");
                    mb.setDescription("> Es wurden erfolgreich **`" + amount + "`** Level hinzugefügt!");
                    mb.addField("<:verify:1003352830758899742> Hinzugefügt", "**" + amount + "**", true);
                    mb.addField("<:sparkles:1003352451455402005> Insgesamt", "**" + LevelManager.getLevel(member) + "**", true);
                    break;
                case "remove":
                    LevelManager.removeLevelFromUser(member, amount);
                    mb.setTitle(":white_check_mark: **LEVEL ERFOLGREICH ENTFERNT!**");
                    mb.setDescription("> Es wurden erfolgreich **`" + amount + "`** Level entfernt!");
                    mb.addField("<:cross:1003357371449487440> Entfernt", "**" + amount + "**", true);
                    mb.addField("<:sparkles:1003352451455402005> Insgesamt", "**" + LevelManager.getLevel(member) + "**", true);
                    break;
                case "set":
                    LevelManager.setLevel(member, amount);
                    mb.setTitle(":white_check_mark: **LEVEL ERFOLGREICH GESETZT!**");
                    mb.setDescription("> Die Level wurden erfolgreich auf **`" + amount + "`** gesetzt!");
                    mb.addField("<:clock:1003358167251562608> Alte Level", "**" + oldvalue + "**", true);
                    mb.addField("<:sparkles:1003352451455402005> Neue Level", "**" + amount + "**", true);
                    break;
            }

            //sending success message
            bb.setColor(0x43b480);
            bb.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");
            mb.setColor(0x43b480);
            mb.addField("<:people:1001082477537935501> Nutzer", member.getAsMention(), true);
            mb.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

            String old = "";
            if (event.getSubcommandName().equals("set"))
                old = "_" + oldvalue;
            List<Button> buttons = new ArrayList<>();
            buttons.add(Button.secondary("level.undo_" + event.getSubcommandName() + "_" + member.getId() + "_" + amount + old, "Rückgängig").withEmoji(Emoji.fromCustom("undo", 878590238782550076L, false)));
            event.replyEmbeds(bb.build(), mb.build()).setEphemeral(true).addActionRow(buttons).queue();

        }

    }

    public void onButtonInteraction (ButtonInteractionEvent event) {
        if (event.getButton().getId().startsWith("level.undo")) {
            String[] args = event.getButton().getId().split("_");
            Member member = event.getGuild().getMemberById(args[2]);
            System.out.println(args[3]);
            int amount = Integer.parseInt(args[3]);

            EmbedBuilder bb = new EmbedBuilder();
            EmbedBuilder mb = new EmbedBuilder();

            switch (args[1]) {
                case "add":
                    LevelManager.removeLevelFromUser(member, amount);
                    mb.setDescription("> Die von dir hinzugefügten Level (" + amount + ") wurde erfolgreich wieder entfernt!");
                    break;
                case "remove":
                    LevelManager.addLevelToUser(member, amount);
                    mb.setDescription("> Die von dir entfernten Level (" + amount + ") wurde erfolgreich wieder hinzugefügt!");
                    break;
                case "set":
                    LevelManager.setLevel(member, Integer.parseInt(args[4]));
                    mb.setDescription("> Die von dir gesetzten Level (" + amount + ") wurde erfolgreich wieder auf **`" + Integer.parseInt(args[4]) + "`** zurückgesetzt!");
                    break;
            }
            bb.setColor(0x43b480);
            bb.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");
            mb.setColor(0x43b480);
            mb.setTitle("<:undo:878590238782550076> **ERFOLGREICH RÜCKGÄNGIG GEMACHT!**");
            mb.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
            event.replyEmbeds(bb.build(), mb.build()).setEphemeral(true).queue();

        }

    }

}
