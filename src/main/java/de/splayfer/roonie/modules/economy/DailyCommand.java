package de.splayfer.roonie.modules.economy;

import de.splayfer.roonie.utils.DefaultMessage;
import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.*;

public class DailyCommand extends ListenerAdapter {

    protected static List<Member> coolDownList = new ArrayList<>();

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {

        if (event.getName().equals("daily")) {
            if (checkCooldown(event.getMember())) {

                int random = new Random().nextInt(500) + 1;
                int round = 0;
                while (round < random)
                    round += 5;
                int money = EconomyManager.getMoney(event.getMember());

                EmbedBuilder main = new EmbedBuilder();
                main.setColor(0x398f3c);
                main.setTitle(":dollar:   **" + round + "** GEFUNDEN!");
                main.setDescription("> Dir wurden " + round + " Coins auf dein Konto hinzugefügt!");
                main.addField("Alter Kontostand", "**" + money + "**", true);
                main.addField("Neuer Kontostand", "**" + (money + round) + "**", true);
                main.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

                //add money
                EconomyManager.addMoneyToUser(event.getMember(), round);
                event.replyEmbeds(Embeds.BANNER_ECONOMY, main.build()).setEphemeral(true).queue();
            } else
                event.replyEmbeds(DefaultMessage.error("Noch nicht verfügbar", "Huch. Es scheint als ist dieser Command nur alle 24h verwendbar...")).setEphemeral(true).addActionRow(Button.secondary("link", "Bruh").withEmoji(Emoji.fromCustom("kekw", 925673040728166470L, false)).withUrl("https://www.youtube.com/watch?v=mKue4WuagL8")).queue();
        }
    }

    protected boolean checkCooldown(Member member) {
        boolean check;
        if (!coolDownList.contains(member)) {
            coolDownList.add(member);
            check = true;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    coolDownList.remove(member);
                }
            }, 1000 * 60 * 60 * 24);
        } else
            check = false;
        return check;
    }
}