package de.splayfer.roonie.modules.level;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class XpCommand extends ListenerAdapter {

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {

        if (event.getName().equals("xp")) {
            Member member = event.getOption("nutzer").getAsMember();
            int amount = event.getOption("anzahl").getAsInt();
            EmbedBuilder bb = new EmbedBuilder();
            EmbedBuilder mb = new EmbedBuilder();
            int oldxp = LevelManager.getXp(member);
            switch (event.getSubcommandName()) {
                case "add":
                    LevelManager.addXpToUser(member, amount);
                    mb.setTitle(":white_check_mark: **XP ERFOLGREICH HINZUGEFÜGT!**");
                    mb.setDescription("> Es wurden erfolgreich **`" + amount + "`** Xp hinzugefügt!");
                    mb.addField("<:verify:1003352830758899742> Hinzugefügt", "**" + amount + "**", true);
                    mb.addField("<:sparkles:1003352451455402005> Insgesamt", "**" + LevelManager.getXp(member) + "**", true);
                    break;
                case "remove":
                    LevelManager.removeXpFromUser(member, amount);
                    mb.setTitle(":white_check_mark: **xp ERFOLGREICH ENTFERNT!**");
                    mb.setDescription("> Es wurden erfolgreich **`" + amount + "`** Xp entfernt!");
                    mb.addField("<:cross:1003357371449487440> Entfernt", "**" + amount + "**", true);
                    mb.addField("<:sparkles:1003352451455402005> Insgesamt", "**" + LevelManager.getXp(member) + "**", true);
                    break;
                case "set":
                    LevelManager.setXp(member, amount);
                    mb.setTitle(":white_check_mark: **XP ERFOLGREICH GESETZT!**");
                    mb.setDescription("> Die Xp wurden erfolgreich auf **`" + amount + "`** gesetzt!");
                    mb.addField("<:clock:1003358167251562608> Alte Xp", "**" + oldxp + "**", true);
                    mb.addField("<:sparkles:1003352451455402005> Neue Xp", "**" + amount + "**", true);
                    break;
            }
            //sending success message
            bb.setColor(0x43b480);
            bb.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png");
            mb.setColor(0x43b480);
            mb.addField("<:people:1001082477537935501> Nutzer", member.getAsMention(), true);
            mb.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
            String old = "";
            if (event.getSubcommandName().equals("set")) {
                old = "_" + oldxp;
            }
            List<Button> buttons = new ArrayList<>();
            buttons.add(Button.secondary("xp.undo_" + event.getSubcommandName() + "_" + member.getId() + "_" + amount + old, "Rückgängig").withEmoji(Emoji.fromCustom("undo", 878590238782550076L, false)));
            event.replyEmbeds(bb.build(), mb.build()).setEphemeral(true).addActionRow(buttons).queue();
        }
    }
}