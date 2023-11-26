package de.splayfer.roonie.utils.enums;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class Embeds {

    public static MessageEmbed BANNER_UMFRAGE = new EmbedBuilder() {{
        setColor(0x28346d);
        setImage("https://cdn.discordapp.com/attachments/880725442481520660/910194455494144021/banner_umfrage.png");}}.build();

    public static MessageEmbed BANNER_GIVEAWAY = new EmbedBuilder() {{
        setColor(0x28346d);
        setImage("https://cdn.discordapp.com/attachments/880725442481520660/913825741588803635/banner_giveaway.png");}}.build();
}
