package de.splayfer.roonie.general;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.general.schedule.BotCounter;
import de.splayfer.roonie.utils.DefaultMessage;
import de.splayfer.roonie.utils.enums.Channels;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.ArrayList;
import java.util.List;

public class WelcomeListener extends ListenerAdapter {

    public void onGuildMemberJoin (GuildMemberJoinEvent event) {
        if (!event.getUser().isBot()) {
            EmbedBuilder bannerEmbed = new EmbedBuilder();
            bannerEmbed.setColor(0x28346d);
            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/938746636019073044/banner_willkommen.png");

            EmbedBuilder mainEmbed = new EmbedBuilder();
            mainEmbed.setColor(0x28346d);
            mainEmbed.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/938746351905288212/879274641070391306.gif");
            mainEmbed.setTitle("Willkommen auf SPLΛYFUNITY");
            mainEmbed.setDescription("> Hey " + event.getUser().getAsMention() + " herzlich <a:hallo:879274641070391306> willkommen");
            mainEmbed.addField("Wir bieten dir", "<:point:940963558693425172> Regelmäßige Events \n" +
                    "<:point:940963558693425172> Viele Giveaways \n" +
                    "<:point:940963558693425172> Coole Belohnungen \n" +
                    "<:point:940963558693425172> Ein kompetentes Team \n", true);
            mainEmbed.addField("Regelmäßig coole Preise", "<:point:940963558693425172> Eigene Discord Bots \n" +
                    "<:point:940963558693425172> Designs / Banner \n" +
                    "<:point:940963558693425172> Discord Nitro \n" +
                    "<:point:940963558693425172> Steam Games", true);
            mainEmbed.addField(":question: Du benötigst Hilfe?", "Teile uns dein Anliegen im `#ticket` Kanal mit!", false);
            mainEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");

            List<Button> buttons = new ArrayList<>();
            buttons.add(Button.primary("welcome.visit", "Schau dich jetzt um!").withUrl("https://discord.gg/V2Vc5hpRkH").withEmoji(Emoji.fromCustom("chat", Long.parseLong("880875728915275786"), true)));
            buttons.add(Button.secondary("welcome.question", "Stelle eine Frage!").withUrl(Channels.TICKETPANEL.getGuildChannel(Roonie.mainGuild).getJumpUrl()).withEmoji(Emoji.fromFormatted("❓")));
            buttons.add(Button.success("welcome.features", "Schau dir unsere Features an!").withEmoji(Emoji.fromCustom("level", 909085962934562896L, false)));
            try {
                event.getUser().openPrivateChannel().complete().sendMessageEmbeds(bannerEmbed.build(), mainEmbed.build()).setActionRow(buttons).queue();
            } catch (Exception e) {
            }
        } else
            BotCounter.botCounterUpdate();
    }

    public void onButtonInteraction (ButtonInteractionEvent event) {
        if (!event.isFromGuild()) {
            switch (event.getButton().getId()) {
                case "welcome.features":
                    event.replyEmbeds(DefaultMessage.error("IN ARBEIT", "Diese Funktion ist zurzeit in Arbeit!")).setEphemeral(true).queue();
                    break;
            }
        }
    }
}