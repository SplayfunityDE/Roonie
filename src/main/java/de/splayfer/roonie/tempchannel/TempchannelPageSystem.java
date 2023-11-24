package de.splayfer.roonie.tempchannel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TempchannelPageSystem {

    private static Button[] buttons = new Button[] {
            Button.secondary("tc_menu_channel", Emoji.fromFormatted("<:icons_mic:859424401198678017>")),
            Button.secondary("tc_menu_roles", Emoji.fromFormatted("<:icons_invite:859424400750542858>")),
            Button.secondary("tc_menu_moderation", Emoji.fromFormatted("<:icons_stagemoderator:988409363255410688>")),
            Button.secondary("tc_menu_activity", Emoji.fromFormatted("<:icons_activities:949635040021721138>")),
            Button.secondary("empty1", Emoji.fromFormatted("<:icons_splash:859426808461525044>")).asDisabled()
    };

    public static MessageCreateData getMainEmbedCreate(Tempchannel channel) {

        MessageCreateBuilder mb = new MessageCreateBuilder();
        EmbedBuilder banner = new EmbedBuilder();
        EmbedBuilder builder = new EmbedBuilder();

        banner.setColor(0xb9bbbd);
        banner.setImage("https://cdn.discordapp.com/attachments/985551183479463998/1001087852215533639/banner_tempchannel.png");

        builder.setColor(0xb9bbbd);
        builder.setThumbnail("https://cdn.discordapp.com/attachments/985551183479463998/1001086934292103198/tempchannel.png");
        builder.setAuthor(channel.getName(), String.format("https://discord.com/channels/%s/%s", channel.vc.getGuild().getId(), channel.vc.getId()), "https://cdn.discordapp.com/emojis/859388128040976384.png?size=96&quality=lossless");
        builder.setTitle("**Kanaleinstellungen**");
        builder.setDescription("> In diesem Menü kannst du Einstellungen an deinem eigenen temporären Sprachkanal vornehmen");
        builder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

        if(channel.limit == 0) {
            builder.addField(String.format("<:people:1001082477537935501> Mitglieder (%s)", channel.getVoiceChannel().getMembers().size()), getArray(channel.getVoiceChannel().getMembers()), false);
        }else {
            builder.addField(String.format("<:people:1001082477537935501> Mitglieder (%s/%s)", channel.getVoiceChannel().getMembers().size(), channel.limit), getArray(channel.getVoiceChannel().getMembers()), false);
        }
        builder.addField("<:channel:1001082478804615238> Name", channel.name, true);
        builder.addField("Limit", String.valueOf(channel.limit), true);

        //builder.setFooter("Zuletzt aktualisiert: " + new SimpleDateFormat("hh:mm:ss").format(LocalDateTime.now()));
        builder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

        mb.setEmbeds(banner.build(), builder.build());

        mb.setActionRow(Button.primary("tc_menu_configure", "Verwalte deinen Kanal!").withEmoji(Emoji.fromCustom("chat", 879356542791598160L, true)), Button.secondary("tc_controlname", "Name").withEmoji(Emoji.fromFormatted("<:icons_edit:859388129625374720>")), Button.secondary("tc_controllimit", "Limit").withEmoji(Emoji.fromFormatted("<:icons_edit:859388129625374720>")));

        return mb.build();

    }

    public static MessageEditData getMainEmbedEdit(Tempchannel channel) {

        MessageEditBuilder mb = new MessageEditBuilder();
        EmbedBuilder banner = new EmbedBuilder();
        EmbedBuilder builder = new EmbedBuilder();

        banner.setColor(0xb9bbbd);
        banner.setImage("https://cdn.discordapp.com/attachments/985551183479463998/1001087852215533639/banner_tempchannel.png");

        builder.setColor(0xb9bbbd);
        builder.setThumbnail("https://cdn.discordapp.com/attachments/985551183479463998/1001086934292103198/tempchannel.png");
        builder.setAuthor(channel.getName(), String.format("https://discord.com/channels/%s/%s", channel.vc.getGuild().getId(), channel.vc.getId()), "https://cdn.discordapp.com/emojis/859388128040976384.png?size=96&quality=lossless");
        builder.setTitle("**Kanaleinstellungen**");
        builder.setDescription("> In diesem Menü kannst du Einstellungen an deinem eigenen temporären Sprachkanal vornehmen");
        builder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

        if(channel.limit == 0) {
            builder.addField(String.format("<:people:1001082477537935501> Mitglieder (%s)", channel.getVoiceChannel().getMembers().size()), getArray(channel.getVoiceChannel().getMembers()), false);
        }else {
            builder.addField(String.format("<:people:1001082477537935501> Mitglieder (%s/%s)", channel.getVoiceChannel().getMembers().size(), channel.limit), getArray(channel.getVoiceChannel().getMembers()), false);
        }
        builder.addField("<:channel:1001082478804615238> Name", channel.name, true);
        builder.addField("Limit", String.valueOf(channel.limit), true);

        //builder.setFooter("Zuletzt aktualisiert: " + new SimpleDateFormat("hh:mm:ss").format(LocalDateTime.now()));
        builder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

        mb.setEmbeds(banner.build(), builder.build());
        mb.setActionRow(Button.primary("tc_menu_configure", "Verwalte deinen Kanal!").withEmoji(Emoji.fromCustom("chat", 879356542791598160L, true)), Button.secondary("tc_controlname", "Name").withEmoji(Emoji.fromFormatted("<:icons_edit:859388129625374720>")), Button.secondary("tc_controllimit", "Limit").withEmoji(Emoji.fromFormatted("<:icons_edit:859388129625374720>")));

        return mb.build();

    }

    public static MessageCreateData getChannelEmbedCreate(Tempchannel channel, String id, Member m) {

        MessageCreateBuilder mb = new MessageCreateBuilder();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(0x37393a);
        builder.setAuthor(channel.vc.getName(), String.format("https://discord.com/channels/%s/%s", channel.vc.getGuild().getId(), channel.vc.getId()), "https://cdn.discordapp.com/emojis/859424401198678017.png?size=96&quality=lossless");
        builder.setTitle("**Sprachkanal**");
        builder.setDescription("> Konfiguriere deinen Sprachkanal nach deinen Bedürfnissen und verwende benutzerdefinierte Einstellungen");

        String channelChat = "Deaktiviert";
        if(channel.channelChat) {
            channelChat = "Aktiviert";
        }
        builder.addField("<:icons_forum:964425853138264094> Kanalchat", channelChat, true);
        builder.addField("<:icons_discover:859429432535023666> Region", parseRegion(channel.getVoiceChannel().getRegion()), true);
        String nsfw = "Ausgeschaltet";
        builder.addField("<:outage:1001096157843443712> NSFW", nsfw, true);

        //builder.setFooter("Zuletzt aktualisiert: " + new SimpleDateFormat("hh:mm:ss").format(LocalDateTime.now()));
        builder.setFooter("ID: " + id);
        builder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

        mb.setEmbeds(builder.build());
        mb.setComponents(getMenuActionrow("tc_menu_channel"), ActionRow.of(
                Button.secondary("tc_controlchat", "Kanaltyp").withEmoji(Emoji.fromFormatted("<:icons_edit:859388129625374720>")).asDisabled(),
                Button.secondary("tc_controlregion", "Region").withEmoji(Emoji.fromFormatted("<:icons_edit:859388129625374720>")).asDisabled(),
                Button.secondary("tc_controlnsfw", "NSFW").withEmoji(Emoji.fromFormatted("<:icons_edit:859388129625374720>")).asDisabled()));

        return mb.build();

    }

    public static MessageEditData getChannelEmbedEdit(Tempchannel channel, String id, Member m) {

        MessageEditBuilder mb = new MessageEditBuilder();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(0x37393a);
        builder.setAuthor(channel.vc.getName(), String.format("https://discord.com/channels/%s/%s", channel.vc.getGuild().getId(), channel.vc.getId()), "https://cdn.discordapp.com/emojis/859424401198678017.png?size=96&quality=lossless");
        builder.setTitle("**Sprachkanal**");
        builder.setDescription("> Konfiguriere deinen Sprachkanal nach deinen Bedürfnissen und verwende benutzerdefinierte Einstellungen");

        String channelChat = "Deaktiviert";
        if(channel.channelChat) {
            channelChat = "Aktiviert";
        }
        builder.addField("<:icons_forum:964425853138264094> Kanalchat", channelChat, true);
        builder.addField("<:icons_discover:859429432535023666> Region", parseRegion(channel.getVoiceChannel().getRegion()), true);
        String nsfw = "Ausgeschaltet";
        builder.addField("<:outage:1001096157843443712> NSFW", nsfw, true);

        //builder.setFooter("Zuletzt aktualisiert: " + new SimpleDateFormat("hh:mm:ss").format(LocalDateTime.now()));
        builder.setFooter("ID: " + id);
        builder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

        mb.setEmbeds(builder.build());
        mb.setComponents(getMenuActionrow("tc_menu_channel"), ActionRow.of(
                Button.secondary("tc_controlchat", "Kanaltyp").withEmoji(Emoji.fromFormatted("<:icons_edit:859388129625374720>")).asDisabled(),
                Button.secondary("tc_controlregion", "Region").withEmoji(Emoji.fromFormatted("<:icons_edit:859388129625374720>")).asDisabled(),
                Button.secondary("tc_controlnsfw", "NSFW").withEmoji(Emoji.fromFormatted("<:icons_edit:859388129625374720>")).asDisabled()));
        return mb.build();

    }

   /* public static MessageCreateData getRoleEmbedCreate(Tempchannel channel, String id, Member m) {

        MessageCreateBuilder mb = new MessageCreateBuilder();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(0xb9bbbd);
        builder.setAuthor(channel.vc.getName(), String.format("https://discord.com/channels/%s/%s", channel.vc.getGuild().getId(), channel.vc.getId()), "https://cdn.discordapp.com/emojis/859424400750542858.png?size=96&quality=lossless");
        builder.setTitle("**Rollenverteilung**");
        builder.setDescription("> Hier kannst du deinen Mitgliedern Rechte und Rollen vergeben");

        builder.addField(String.format("<:colorstaff:1001096426220158986> Moderatoren [%s]", channel.modList.size()), getArray(channel.modList), true);

        builder.addField("⠀", "⠀", false);
        builder.addField("<:owner:1001091842747674685> Inhaber", channel.owner.getAsMention(), true);
        builder.addField("<:globe:1001093203472158740> Sichtbarkeit", "Öffentlich", true);

        //builder.setFooter("Zuletzt aktualisiert: " + new SimpleDateFormat("hh:mm:ss").format(LocalDateTime.now()));
        builder.setFooter("ID: " + id);
        builder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

        mb.setEmbeds(builder.build());

        StringSelectMenu.Builder menu = StringSelectMenu.create("tc_controlmod").setMinValues(0);
        menu.setPlaceholder("Wähle Moderatoren aus");
        List<String> selected = new ArrayList<>();

        if(channel.owner.equals(m)) {
            for(Member member : channel.modList) {
                String emoji = member.getRoles().get(0).getIcon().getEmoji();
                if(emoji == null) {
                    emoji = "<:people:1001082477537935501>";
                }
                menu.addOption(member.getEffectiveName(), "tc_selectmod_" + member.getId(), member.getUser().getAsTag(), Emoji.fromFormatted(emoji));
                selected.add("tc_selectmod_" + member.getId());
            }
            for(Member member : channel.getVoiceChannel().getMembers()) {
                String emoji = member.getRoles().get(0).getIcon().getEmoji();
                if(emoji == null) {
                    emoji = "<:people:1001082477537935501>";
                }
                if(!channel.modList.contains(member) && member != channel.owner) {
                    menu.addOption(member.getEffectiveName(), "tc_selectmod_" + member.getId(), member.getUser().getAsTag(), Emoji.fromFormatted(emoji));
                }
            }
            if(!selected.isEmpty()) {
                menu.setDefaultValues(selected);
            }

            if(menu.getOptions().isEmpty()) {
                menu.setDisabled(true);
                menu.setPlaceholder("Keine Mitglieder gefunden");
                menu.addOption("___", "fakeoption");
            }else {
                menu.setMaxValues(menu.getOptions().size());
            }
        }else {
            menu.setDisabled(true);
            menu.setPlaceholder("Keine Berechtigung!");
            menu.addOption("___", "fakeoption");
        }
        mb.setComponents(getMenuActionrow("tc_menu_roles"), ActionRow.of(menu.build(),
                Button.secondary("tc_switchowner", "Kanal übertragen").asDisabled(),
                Button.secondary("tc_setvisibility", "Sichtbarkeit ändern").asDisabled()));
        return mb.build();
    }*/

    public static MessageEditData getRoleEmbedEdit(Tempchannel channel, String id, Member m) {

        MessageEditBuilder mb = new MessageEditBuilder();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(0xb9bbbd);
        builder.setAuthor(channel.vc.getName(), String.format("https://discord.com/channels/%s/%s", channel.vc.getGuild().getId(), channel.vc.getId()), "https://cdn.discordapp.com/emojis/859424400750542858.png?size=96&quality=lossless");
        builder.setTitle("**Rollenverteilung**");
        builder.setDescription("> Hier kannst du deinen Mitgliedern Rechte und Rollen vergeben");

        builder.addField(String.format("<:colorstaff:1001096426220158986> Moderatoren [%s]", channel.modList.size()), getArray(channel.modList), true);

        builder.addField("⠀", "⠀", false);
        builder.addField("<:owner:1001091842747674685> Inhaber", channel.owner.getAsMention(), true);
        builder.addField("<:globe:1001093203472158740> Sichtbarkeit", "Öffentlich", true);

        //builder.setFooter("Zuletzt aktualisiert: " + new SimpleDateFormat("hh:mm:ss").format(LocalDateTime.now()));
        builder.setFooter("ID: " + id);
        builder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

        mb.setEmbeds(builder.build());

        StringSelectMenu.Builder menu = StringSelectMenu.create("tc_controlmod").setMinValues(0);
        menu.setPlaceholder("Wähle Moderatoren aus");
        List<String> selected = new ArrayList<>();

        if(channel.owner.equals(m)) {
            for(Member member : channel.modList) {
                String emoji = member.getRoles().get(0).getIcon().getEmoji();
                if(emoji == null) {
                    emoji = "<:people:1001082477537935501>";
                }
                menu.addOption(member.getEffectiveName(), "tc_selectmod_" + member.getId(), member.getUser().getAsTag(), Emoji.fromFormatted(emoji));
                selected.add("tc_selectmod_" + member.getId());
            }
            for(Member member : channel.getVoiceChannel().getMembers()) {
                String emoji = member.getRoles().get(0).getIcon().getEmoji();
                if(emoji == null) {
                    emoji = "<:people:1001082477537935501>";
                }
                if(!channel.modList.contains(member) && member != channel.owner) {
                    menu.addOption(member.getEffectiveName(), "tc_selectmod_" + member.getId(), member.getUser().getAsTag(), Emoji.fromFormatted(emoji));
                }
            }
            if(!selected.isEmpty()) {
                menu.setDefaultValues(selected);
            }

            if(menu.getOptions().isEmpty()) {
                menu.setDisabled(true);
                menu.setPlaceholder("Keine Mitglieder gefunden");
                menu.addOption("___", "fakeoption");
            }else {
                menu.setMaxValues(menu.getOptions().size());
            }
        }else {
            menu.setDisabled(true);
            menu.setPlaceholder("Keine Berechtigung!");
            menu.addOption("___", "fakeoption");
        }
        mb.setComponents(getMenuActionrow("tc_menu_roles"), ActionRow.of(menu.build()),
                ActionRow.of(Button.secondary("tc_switchowner", "Kanal übertragen").asDisabled(),
                Button.secondary("tc_setvisibility", "Sichtbarkeit ändern").asDisabled()
        ));
        return mb.build();
    }

    /*public static MessageCreateData getModerationEmbedCreate(Tempchannel channel, String id, Member m) {

        MessageCreateBuilder mb = new MessageCreateBuilder();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(0xb9bbbd);
        builder.setAuthor(channel.vc.getName(), String.format("https://discord.com/channels/%s/%s", channel.vc.getGuild().getId(), channel.vc.getId()), "https://cdn.discordapp.com/emojis/988409363255410688.png?size=96&quality=lossless");
        builder.setTitle("**Moderation**");
        builder.setDescription("> Hier kannst du Mitglieder stummschalten oder sperren, um deinen Kanal besser zu organisieren");

        builder.addField(String.format("<:ban:1001093790901211247> Gebannt [%s]", channel.banned.size()), getArray(channel.banned.keySet()), true);
        builder.addField(String.format("<:mute:1001093792151109753> Stummgeschaltet [%s]", channel.muted.size()), getArray(channel.muted.keySet()), true);

        //builder.setFooter("Zuletzt aktualisiert: " + new SimpleDateFormat("hh:mm:ss").format(LocalDateTime.now()));
        builder.setFooter("ID: " + id);
        builder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

        StringSelectMenu.Builder menu = StringSelectMenu.create("tc_controlmembers").setRequiredRange(1, 1);
        menu.setPlaceholder("Sperren/Stummschaltungen verwalten");

        for(Member member : channel.banned.keySet()) {
            String sub = "Gebannt";
            if(channel.muted.containsKey(member)) {
                sub = sub + ", Stummgeschaltet";
            }
            menu.addOption(member.getEffectiveName(), "tc_selectmember_" + member.getId(), sub, Emoji.fromFormatted("<:ban:1001093790901211247>"));
        }
        for(Member member : channel.muted.keySet()) {
            if(!channel.banned.containsKey(member)) {
                String sub = "Stummgeschaltet";
                menu.addOption(member.getEffectiveName(), "tc_selectmember_" + member.getId(), sub, Emoji.fromFormatted("<:mute:1001093792151109753>"));
            }
        }
        for(Member member : channel.getVoiceChannel().getMembers()) {
            if(!channel.modList.contains(member) && !channel.muted.containsKey(member)) {
                if(!member.isOwner()) {
                        menu.addOption(member.getEffectiveName(), "tc_selectmember_" + member.getId(), "Keine Bestrafungen", Emoji.fromFormatted("<:people:1001082477537935501>"));
                }
            }
        }
        if (menu.getOptions().size() >= 25)
            menu.getOptions().remove(menu.getOptions().size() - 1);
        menu.addOption("Mit ID auswählen", "tc_selectmember_id", Emoji.fromFormatted("<:icons_edit:859388129625374720>"));

        mb.setEmbeds(builder.build());
        mb.setComponents(getMenuActionrow("tc_menu_moderation"), ActionRow.of(menu.build()));
        return mb.build();
    }*/

    public static MessageEditData getModerationEmbedEdit(Tempchannel channel, String id, Member m) {
        MessageEditBuilder mb = new MessageEditBuilder();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(0xb9bbbd);
        builder.setAuthor(channel.vc.getName(), String.format("https://discord.com/channels/%s/%s", channel.vc.getGuild().getId(), channel.vc.getId()), "https://cdn.discordapp.com/emojis/988409363255410688.png?size=96&quality=lossless");
        builder.setTitle("**Moderation**");
        builder.setDescription("> Hier kannst du Mitglieder stummschalten oder sperren, um deinen Kanal besser zu organisieren");

        builder.addField(String.format("<:ban:1001093790901211247> Gebannt [%s]", channel.banned.size()), getArray(channel.banned.keySet()), true);
        builder.addField(String.format("<:mute:1001093792151109753> Stummgeschaltet [%s]", channel.muted.size()), getArray(channel.muted.keySet()), true);

        //builder.setFooter("Zuletzt aktualisiert: " + new SimpleDateFormat("hh:mm:ss").format(LocalDateTime.now()));
        builder.setFooter("ID: " + id);
        builder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

        StringSelectMenu.Builder menu = StringSelectMenu.create("tc_controlmembers").setRequiredRange(1, 1);
        menu.setPlaceholder("Sperren/Stummschaltungen verwalten");

        for(Member member : channel.banned.keySet()) {
            String sub = "Gebannt";
            if(channel.muted.containsKey(member)) {
                sub = sub + ", Stummgeschaltet";
            }
            menu.addOption(member.getEffectiveName(), "tc_selectmember_" + member.getId(), sub, Emoji.fromFormatted("<:ban:1001093790901211247>"));
        }
        for(Member member : channel.muted.keySet()) {
            if(!channel.banned.containsKey(member)) {
                String sub = "Stummgeschaltet";
                menu.addOption(member.getEffectiveName(), "tc_selectmember_" + member.getId(), sub, Emoji.fromFormatted("<:mute:1001093792151109753>"));
            }
        }
        for(Member member : channel.getVoiceChannel().getMembers()) {
            if(!channel.modList.contains(member) && !channel.muted.containsKey(member)) {
                if(!member.isOwner()) {
                    menu.addOption(member.getEffectiveName(), "tc_selectmember_" + member.getId(), "Keine Bestrafungen", Emoji.fromFormatted("<:people:1001082477537935501>"));
                }
            }
        }

        if (menu.getOptions().size() >= 25)
            menu.getOptions().remove(menu.getOptions().size() - 1);
        menu.addOption("Mit ID auswählen", "tc_selectmember_id", Emoji.fromFormatted("<:icons_edit:859388129625374720>"));

        mb.setEmbeds(builder.build());
        mb.setComponents(getMenuActionrow("tc_menu_moderation"), ActionRow.of(menu.build()));

        return mb.build();
    }

    /*public static MessageCreateData getActivityEmbedCreate(Tempchannel channel, String id, Member m) {

        MessageCreateBuilder mb = new MessageCreateBuilder();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(0xb9bbbd);
        builder.setAuthor(channel.vc.getName(), String.format("https://discord.com/channels/%s/%s", channel.vc.getGuild().getId(), channel.vc.getId()), "https://cdn.discordapp.com/emojis/949635040021721138.png?size=96&quality=lossless");
        builder.setTitle("**Aktivitäten**");
        builder.setDescription("> Hier kannst du Aktivitäten im Kanal starten und Minispiele oder Videos mit deinen Mitgliedern genießen");

        builder.addField("1) Aktivität auswählen", "Wähle eine Aktivität aus der Liste aus", true);
        builder.addField("2) Link teilen", "Mitglieder können nun über den Kanal oder den Link, der automatisch geteilt wird, beitreten", true);
        builder.addField("3) Loslegen", "Nun könnt ihr mit der Aktivität beginnen", true);

        //builder.setFooter("Zuletzt aktualisiert: " + new SimpleDateFormat("hh:mm:ss").format(LocalDateTime.now()));
        builder.setFooter("ID: " + id);
        builder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

        StringSelectMenu menu = StringSelectMenu.create("tc_selectActivity")
                .addOption("YouTube Together", "880218394199220334", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("youtube", Long.parseLong("885442516869074946"), false))
                .addOption("Poker", "755827207812677713", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("poker", Long.parseLong("923649817467584512"), false))
                .addOption("Betrayal", "773336526917861400", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("betrayal", Long.parseLong("923649817765347378"), false))
                .addOption("Fishing", "814288819477020702", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("fishingtonio", Long.parseLong("923649817249456179"), false))
                .addOption("Schach", "832012774040141894", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("chess", Long.parseLong("923649817794723880"), false))
                .addOption("Wordsnack", "879863976006127627", "KKlicke, um die Aktivität zu starten!", Emoji.fromCustom("wordsnacks", Long.parseLong("923649817375297596"), false))
                .addOption("DoodleCrew", "878067389634314250", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("doodlecrew", Long.parseLong("923649817249460264"), false))
                .addOption("AwkWord", "879863881349087252", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("awkword", Long.parseLong("923649816892940402"), false))
                .addOption("SpellCast", "852509694341283871", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("spellcast", Long.parseLong("923649817224314930"), false))
                .addOption("Checkers", "832013003968348200", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("checkers", Long.parseLong("923654715072466975"), false))
                .addOption("Puttparty", "763133495793942528", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("chess", Long.parseLong("923649817794723880"), false))
                .build();

        mb.setEmbeds(builder.build());
        mb.setComponents(getMenuActionrow("tc_menu_activity"), ActionRow.of(menu));

        return mb.build();

    }*/

    public static MessageEditData getActivityEmbedEdit(Tempchannel channel, String id, Member m) {
        MessageEditBuilder mb = new MessageEditBuilder();
        EmbedBuilder builder = new EmbedBuilder();

        builder.setColor(0xb9bbbd);
        builder.setAuthor(channel.vc.getName(), String.format("https://discord.com/channels/%s/%s", channel.vc.getGuild().getId(), channel.vc.getId()), "https://cdn.discordapp.com/emojis/949635040021721138.png?size=96&quality=lossless");
        builder.setTitle("**Aktivitäten**");
        builder.setDescription("> Hier kannst du Aktivitäten im Kanal starten und Minispiele oder Videos mit deinen Mitgliedern genießen");

        builder.addField("1) Aktivität auswählen", "Wähle eine Aktivität aus der Liste aus", true);
        builder.addField("2) Link teilen", "Mitglieder können nun über den Kanal oder den Link, der automatisch geteilt wird, beitreten", true);
        builder.addField("3) Loslegen", "Nun könnt ihr mit der Aktivität beginnen", true);

        //builder.setFooter("Zuletzt aktualisiert: " + new SimpleDateFormat("hh:mm:ss").format(LocalDateTime.now()));
        builder.setFooter("ID: " + id);
        builder.setImage("https://cdn.discordapp.com/attachments/985551183479463998/986627378417655858/auto_faqw.png");

        StringSelectMenu menu = StringSelectMenu.create("tc_selectActivity")
                .addOption("YouTube Together", "880218394199220334", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("youtube", Long.parseLong("885442516869074946"), false))
                .addOption("Poker", "755827207812677713", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("poker", Long.parseLong("923649817467584512"), false))
                .addOption("Betrayal", "773336526917861400", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("betrayal", Long.parseLong("923649817765347378"), false))
                .addOption("Fishing", "814288819477020702", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("fishingtonio", Long.parseLong("923649817249456179"), false))
                .addOption("Schach", "832012774040141894", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("chess", Long.parseLong("923649817794723880"), false))
                .addOption("Wordsnack", "879863976006127627", "KKlicke, um die Aktivität zu starten!", Emoji.fromCustom("wordsnacks", Long.parseLong("923649817375297596"), false))
                .addOption("DoodleCrew", "878067389634314250", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("doodlecrew", Long.parseLong("923649817249460264"), false))
                .addOption("AwkWord", "879863881349087252", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("awkword", Long.parseLong("923649816892940402"), false))
                .addOption("SpellCast", "852509694341283871", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("spellcast", Long.parseLong("923649817224314930"), false))
                .addOption("Checkers", "832013003968348200", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("checkers", Long.parseLong("923654715072466975"), false))
                .addOption("Puttparty", "763133495793942528", "Klicke, um die Aktivität zu starten!", Emoji.fromCustom("chess", Long.parseLong("923649817794723880"), false))
                .build();

        mb.setEmbeds(builder.build());
        mb.setComponents(getMenuActionrow("tc_menu_activity"), ActionRow.of(menu));
        return mb.build();
    }

    public static ActionRow getMenuActionrow(String selfMenu) {
        List<Button> buttonTemp = new ArrayList<>();

        for (Button button : buttons) {
            if (button.getId().equals(selfMenu)) {
                buttonTemp.add(button.asDisabled().withStyle(ButtonStyle.PRIMARY).withId(button.getId()));
            } else {
                buttonTemp.add(button.withId(button.getId()));
            }
        }
        return ActionRow.of(buttonTemp);
    }

    public static String parseRegion(Region region) {
        return switch (region.getName()) {
            case "automatic" -> "Automatisch";
            case "rotterdam" -> "Europa";
            case "brazil" -> "Brasilien";
            case "hongkong" -> "Hong Kong";
            case "india" -> "Indien";
            case "japan" -> "Japan";
            case "russia" -> "Russland";
            case "singapore" -> "Singapur";
            case "southafrica" -> "Südafrika";
            case "sydney" -> "Sydney";
            case "usa-central" -> "USA";
            default -> "Automatisch";
        };
    }

    public static String getArray(Collection<Member> members) {
        StringBuilder array = new StringBuilder();

        for(Member member : members) {
            array.append(" ").append(member.getAsMention());
        }
        if(array.toString().isEmpty()) {
            array = new StringBuilder("Keine");
        }

        return array.toString();
    }

}
