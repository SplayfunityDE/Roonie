package de.splayfer.roonie.economy;

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

                while (round < random) {
                    round += 5;
                }

                int money = EconomyManager.getMoney(event.getMember());

                EmbedBuilder banner = new EmbedBuilder();
                banner.setColor(0x398f3c);
                banner.setImage("https://cdn.discordapp.com/attachments/985551183479463998/1012071780896219227/banner_economy2.png");

                EmbedBuilder main = new EmbedBuilder();
                main.setColor(0x398f3c);
                main.setTitle(":dollar:   **" + round + "** GEFUNDEN!");
                main.setDescription("> Dir wurden " + round + " Coins auf dein Konto hinzugefügt!");
                main.addField("Alter Kontostand", "**" + money + "**", true);
                main.addField("Neuer Kontostand", "**" + (money + round) + "**", true);
                main.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

                //add money

                EconomyManager.addMoneyToUser(event.getMember(), round);

                event.replyEmbeds(banner.build(), main.build()).setEphemeral(true).queue();

            } else {

                EmbedBuilder bannerEmbed = new EmbedBuilder();
                bannerEmbed.setColor(0xed4245);
                bannerEmbed.setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380353040384/banner_fehler.png");

                EmbedBuilder reply = new EmbedBuilder();
                reply.setColor(0xed4245);
                reply.setTitle(":no_entry_sign: **NOCH NICHT VERFÜGBAR**");
                reply.setDescription("> Huch. Es scheint als ist dieser Command nur alle 24h verwendbar...");
                reply.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                event.replyEmbeds(bannerEmbed.build(), reply.build()).setEphemeral(true).addActionRow(Button.secondary("link", "Bruh").withEmoji(Emoji.fromCustom("kekw", 925673040728166470L, false)).withUrl("https://www.youtube.com/watch?v=mKue4WuagL8")).queue();

            }

        }
    }

    protected boolean checkCooldown(Member member) {

        boolean check;

        if (!coolDownList.contains(member)) {
            coolDownList.add(member);
            check = true;

            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {

                    coolDownList.remove(member);

                    t.cancel();

                }
            }, 1000 * 60 * 60 * 24);

        } else {
            check = false;
        }

        return check;

    }

}
