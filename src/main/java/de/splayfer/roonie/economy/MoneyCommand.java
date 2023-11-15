package de.splayfer.roonie.economy;

import de.splayfer.roonie.economy.EconomyManager;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoneyCommand extends ListenerAdapter {

    private static Button[] buttons = new Button[] {
            Button.secondary("m_overview", Emoji.fromFormatted("<:list:1002591375960842300>")),
            Button.secondary("empty1", Emoji.fromFormatted("<:icons_splash:859426808461525044>")).asDisabled(),
            Button.secondary("m_leaderboard", Emoji.fromFormatted("<:cup:1002943693822636073>"))
    };

    public static ActionRow getMenuActionrow(String selfMenu) {

        List<Button> buttonTemp = new ArrayList<>();

        for(int i = 0; i < buttons.length; i++) {

            if(buttons[i].getId().equals(selfMenu)) {
                buttonTemp.add(buttons[i].asDisabled().withStyle(ButtonStyle.PRIMARY).withId(buttons[i].getId()));
            }else {
                buttonTemp.add(buttons[i].withId(buttons[i].getId()));
            }

        }
        return ActionRow.of(buttonTemp);
    }

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {

        if (event.getName().equals("money")) {

            Member target;
            boolean other;

            if (event.getOptions().isEmpty()) {
                target = event.getMember();
                other = false;
            } else {
                target = event.getOption("nutzer").getAsMember();
                other = true;
            }

            int money = EconomyManager.getMoney(target);

            //building Embed

            EmbedBuilder banner = new EmbedBuilder();
            banner.setColor(0xffcc4d);
            banner.setImage("https://cdn.discordapp.com/attachments/985551183479463998/1002579047609548860/banner_konto.png");

            EmbedBuilder builder = new EmbedBuilder();
            if (other) {
                builder.setTitle(":coin: **KONTOINFORMATIONEN VON " + target.getEffectiveName() + "**");
                builder.setDescription("> Hier findest du alle wichtigen Econony Statistiken von " + target.getEffectiveName() + "!");
            } else {
                builder.setTitle(":coin: **DEINE KONTOINFORMATIONEN**");
                builder.setDescription("> Hier findest du alle wichtigen Econony Statistiken von dir!");
            }

            builder.setColor(0xffcc4d);
            builder.addField("<:people:1001082477537935501> Nutzer", target.getAsMention(), true);
            builder.addField("<:coin:1002582123154251777> Kontostand", "**" + money + "**", true);
            builder.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
            builder.setFooter("ID: " + target.getId());

            event.replyEmbeds(banner.build(), builder.build()).addActionRows(getMenuActionrow("m_overview")).setEphemeral(true).queue();


        } else if (event.getName().equals("leaderboard")) {

            Member member = event.getMember();

            Map<Integer, String> list = EconomyManager.top(5);

            EmbedBuilder banner = new EmbedBuilder();
            EmbedBuilder builder = new EmbedBuilder();

            banner.setColor(0xffcc4d);
            banner.setImage("https://cdn.discordapp.com/attachments/985551183479463998/1003009248562782289/banner_rangliste.png");

            builder.setColor(0xffcc4d);
            builder.setTitle(":coin: **SPLΛYFUNITY Economy Rangliste**");
            builder.setDescription("> Hier siehst du die besten Nutzer aus dem gesamten Economy System!");

            String name = "";
            String coins = "";
            int i = 1;

            for (Integer num : list.keySet()) {
                name += "\n **" + i + ".** <:people:1001082477537935501> <@" + list.get(num) + ">: ";
                coins += "\n **" + num + "**";
                i++;
            }
            if (!list.containsValue(member.getId())) {
                name += "\n **...** \n \n <:people:1001082477537935501> <@" + member.getId() + ">: ";
                coins += "\n **...** \n \n **" + EconomyManager.getMoney(member) + "**";
            }
            builder.addField("Name", name, true);
            builder.addField("", coins, true);

            builder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");
            builder.setFooter("ID: " + member.getId());

            event.replyEmbeds(banner.build(), builder.build()).addActionRows(getMenuActionrow("m_leaderboard")).setEphemeral(true).queue();

        }

    }

    @Override
    public void onButtonInteraction (ButtonInteractionEvent event) {

        if (event.getButton().getId().startsWith("m_")) {

            String[] args = event.getComponentId().split("_");

            Message message = null;

            Member target = event.getGuild().getMemberById(event.getMessage().getEmbeds().get(1).getFooter().getText().substring(4));
            boolean other;

            if (target.equals(event.getMember())) {
                other = false;
            } else {
                other = true;
            }

            switch(args[1]) {

                case "overview":
                    message = getOverviewEmbed(target, other); break;
                case "leaderboard":
                    message = getLeaderboardEmbed(target); break;
            }

            event.deferEdit().queue();
            event.getHook().editOriginal(message).queue();

            return;

        }

    }

    public static Message getOverviewEmbed(Member m, boolean other) {

        int money = EconomyManager.getMoney(m);

        MessageBuilder mb = new MessageBuilder();

        //building Embed

        EmbedBuilder bannerEmbed = new EmbedBuilder();
        bannerEmbed.setColor(0xffcc4d);
        bannerEmbed.setImage("https://cdn.discordapp.com/attachments/985551183479463998/1002579047609548860/banner_konto.png");

        EmbedBuilder embedBuilder = new EmbedBuilder();
        if (other) {
            embedBuilder.setTitle(":coin: **KONTOINFORMATIONEN VON " + m.getEffectiveName() + "**");
            embedBuilder.setDescription("> Hier findest du alle wichtigen Econony Statistiken von " + m.getEffectiveName() + "!");
        } else {
            embedBuilder.setTitle(":coin: **DEINE KONTOINFORMATIONEN**");
            embedBuilder.setDescription("> Hier findest du alle wichtigen Econony Statistiken von dir!");
        }

        embedBuilder.setColor(0xffcc4d);
        embedBuilder.addField("<:people:1001082477537935501> Nutzer", m.getAsMention(), true);
        embedBuilder.addField("<:coin:1002582123154251777> Kontostand", "**" + money + "**", true);
        embedBuilder.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
        embedBuilder.setFooter("ID: " + m.getId());

        mb.setEmbeds(bannerEmbed.build(), embedBuilder.build());
        mb.setActionRows(getMenuActionrow("m_overview"));

        return mb.build();

    }

    public static Message getLeaderboardEmbed(Member m) {

        Map<Integer, String> list = EconomyManager.top(5);

        MessageBuilder mb = new MessageBuilder();
        EmbedBuilder banner = new EmbedBuilder();
        EmbedBuilder builder = new EmbedBuilder();

        banner.setColor(0xffcc4d);
        banner.setImage("https://cdn.discordapp.com/attachments/985551183479463998/1003009248562782289/banner_rangliste.png");

        builder.setColor(0xffcc4d);
        builder.setTitle(":coin: **SPLΛYFUNITY Economy Rangliste**");
        builder.setDescription("> Hier siehst du die besten Nutzer aus dem gesamten Economy System!");

        String name = "";
        String coins = "";
        int i = 1;

        for (Integer num : list.keySet()) {
            name += "\n **" + i + ".** <:people:1001082477537935501> <@" + list.get(num) + ">: ";
            coins += "\n **" + num + "**";
            i++;
        }

        if (!list.containsValue(m.getId())) {
            name += "\n **...** \n \n <:people:1001082477537935501> <@" + m.getId() + ">: ";
            coins += "\n **...** \n \n **" + EconomyManager.getMoney(m) + "**";
        }
        builder.addField("Name", name, true);
        builder.addField("", coins, true);

        builder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");
        builder.setFooter("ID: " + m.getId());

        mb.setEmbeds(banner.build(), builder.build());
        mb.setActionRows(getMenuActionrow("m_leaderboard"));

        return mb.build();

    }

}
