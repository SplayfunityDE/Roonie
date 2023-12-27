package de.splayfer.roonie.utils.enums;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Embeds {

    public static MessageEmbed BANNER_COMMANDS = new EmbedBuilder() {{
        setColor(0x28346d);
        setImage("https://cdn.discordapp.com/attachments/880725442481520660/909071872723935292/banner_commands.png");
    }}.build();

    public static MessageEmbed BANNER_WAITING = new EmbedBuilder() {{
        setColor(0x28346d);
        setImage("https://cdn.discordapp.com/attachments/906251556637249547/937416756547452938/banner_warten.png");}}.build();

    public static MessageEmbed BANNER_STATS = new EmbedBuilder() {{
        setColor(0x28346d);
        setImage("https://cdn.discordapp.com/attachments/906251556637249547/938702207644217424/banner_stats.png");
    }}.build();

    public static MessageEmbed BANNER_MINIGAME = new EmbedBuilder() {{
        setColor(0x28346d);
        setImage("https://cdn.discordapp.com/attachments/906251556637249547/937036676239347812/banner_minigames.png");
    }}.build();

    public static MessageEmbed BANNER_MINIGAME_ENDING = new EmbedBuilder() {{
        setColor(0xffcf55);
        setImage("https://cdn.discordapp.com/attachments/906251556637249547/938483513970262136/banner_winner.png");
    }}.build();

    public static MessageEmbed BANNER_UMFRAGE = new EmbedBuilder() {{
        setColor(0x28346d);
        setImage("https://cdn.discordapp.com/attachments/880725442481520660/910194455494144021/banner_umfrage.png");}}.build();

    public static MessageEmbed BANNER_GIVEAWAY = new EmbedBuilder() {{
        setColor(0x28346d);
        setImage("https://cdn.discordapp.com/attachments/880725442481520660/913825741588803635/banner_giveaway.png");}}.build();

    public static MessageEmbed BANNER_TICKET = new EmbedBuilder() {{
        setColor(0x28346d);
        setImage("https://cdn.discordapp.com/attachments/985551183479463998/986624618569801818/banner_support.png");}}.build();

    public static MessageEmbed BANNER_TICKET_BUG = new EmbedBuilder() {{
        setColor(0xFFD764);
        setImage("https://cdn.discordapp.com/attachments/880725442481520660/921435121410793542/banner_ticketmeldung.png");
    }}.build();

    public static MessageEmbed BANNER_TICKET_REPORT = new EmbedBuilder() {{
        setColor(0xDD2E44);
        setImage("https://cdn.discordapp.com/attachments/880725442481520660/921435121201057802/banner_ticketmeldung2.png");
    }}.build();

    public static MessageEmbed BANNER_ECONOMY = new EmbedBuilder() {{
        setColor(0x398f3c);
        setImage("https://cdn.discordapp.com/attachments/985551183479463998/1012071780896219227/banner_economy2.png");}}.build();

    public static MessageEmbed BANNER_ECONOMY_RANKING = new EmbedBuilder() {{
        setColor(0xffcc4d);
        setImage("https://cdn.discordapp.com/attachments/985551183479463998/1003009248562782289/banner_rangliste.png");}}.build();

    public static MessageEmbed BANNER_ECONOMY_ACCOUNT = new EmbedBuilder() {{
        setColor(0xffcc4d);
        setImage("https://cdn.discordapp.com/attachments/985551183479463998/1002579047609548860/banner_konto.png");}}.build();

    public static MessageEmbed BANNER_CATEGORY_GAMING = new EmbedBuilder() {{
        setColor(0x8b8a91);
        setImage("https://cdn.discordapp.com/attachments/906251556637249547/925053738220150794/banner_vorlagen_gaming.png");}}.build();
}
