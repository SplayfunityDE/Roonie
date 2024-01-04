package de.splayfer.roonie.modules.booster;

import de.splayfer.roonie.config.Config;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.*;

public class BoosterSetupCommand {

    public static void setup(SlashCommandInteractionEvent event) {

        EmbedBuilder banner = new EmbedBuilder();
        banner.setColor(0x28346d);
        banner.setImage("https://cdn.discordapp.com/attachments/906251556637249547/984899323470946314/banner_booster.png");

        EmbedBuilder message = new EmbedBuilder();
        message.setColor(0x28346d);
        message.setTitle(":sparkles: Werde Booster auf SPLΛYFUNITY");
        message.setDescription("Als Booster unterstützt du den Server in Bereichen wie dem Planen von Events und der Servergestaltung. Als Dankeschön erhältst exklusive Vorteile!");
        message.addField("Vorteile als Booster", "<:point:940963558693425172> Den exklusiven Booster Rang \n" +
                "<:point:940963558693425172> Erstelle Diskussionen (Threads) \n" +
                "<:point:940963558693425172> Erhalte 2x Level XP \n" +
                "<:point:940963558693425172> Dein eigener Command \n" +
                "<:point:940963558693425172> Gewinnspiele ohne Bedingungen", false);
        message.addField("Ab 2 Boosts",
                "<:x_:906141028657037343> Beantrage deinen eigenen Discord Bot \n" +
                        "", false);
        message.setImage("https://cdn.discordapp.com/attachments/880725442481520660/906158281557434368/boosterbanner.png");

        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.secondary("boostinfo", "Erfahre mehr!").withEmoji(Emoji.fromCustom(event.getJDA().getEmojiById("906135368439570472"))));

        Message m = event.getChannel().sendMessageEmbeds(banner.build(), message.build()).setActionRow(buttons).complete();
        Config.setConfigChannel("booster", event.getChannel(), m);
    }
}