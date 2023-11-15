package de.splayfer.roonie.tempchannel;

import de.splayfer.roonie.general.Roles;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.*;

public class Tempchannel {

    private final String settingsCategory = "887680440318705735";
    private final String voiceCategory = "873506353551925310";

    public Member owner = null;
    public VoiceChannel vc = null;
    public TextChannel settings = null;

    public String name;
    public String emoji = "⏳";
    public boolean nameUpdated = false;
    public String channelType = "";
    public Integer limit = 0;

    public List<Member> modList = new ArrayList<Member>();
    public HashMap<Member, Member> banned = new HashMap<>();
    public HashMap<Member, Member> muted = new HashMap<>();

    public Message embedMessage = null;
    public boolean channelChat;
    public Guild guild;

    public HashMap<InteractionHook, String> ephList = new HashMap<>();

    public Tempchannel(Guild guild, Member member) {
        System.out.println("Debug");
        this.name = member.getUser().getName();
        this.owner = member;
        this.guild = guild;
        this.vc = create();
        this.settings = createSettingsChannel();
        System.out.println("Debug");
        ControlListener.tempChannels.put(vc.getId(), this);

        if(owner.getId().equals("600230293550399488") || owner.getId().equals("853618861294485534")) {
            updateMessage();
        }

    }

    public String getName() {
        return String.format("〣│%s・%s", emoji, name);
    }
    public String getName(String emoji) {
        return String.format("〣│%s・%s", emoji, owner.getUser().getName());
    }

    private VoiceChannel create() {
        System.out.println("Debug");
        VoiceChannel channel = guild.createVoiceChannel(getName(), guild.getCategoryById(voiceCategory)).complete();
        System.out.println("Debug");
        channel.upsertPermissionOverride(Roles.everyone.getRole(guild)).deny(Permission.MESSAGE_SEND).grant(Permission.VOICE_SPEAK).queue();
        channel.upsertPermissionOverride(Roles.SUP.getRole(guild)).grant(Permission.MESSAGE_MANAGE).grant(Permission.MANAGE_CHANNEL).queue();
        channel.upsertPermissionOverride(Roles.MOD.getRole(guild)).grant(Permission.MESSAGE_MANAGE).grant(Permission.MANAGE_CHANNEL).queue();
        channel.upsertPermissionOverride(Roles.ADMIN.getRole(guild)).grant(Permission.ADMINISTRATOR).queue();

        guild.getManager().getGuild().moveVoiceMember(owner, channel).queue();
        owner.mute(false).queue();

        return channel;

    }

    private TextChannel createSettingsChannel() {

        TextChannel tempSettings = guild.getCategoryById(settingsCategory).createTextChannel("〣│🔨・einstellungen").setTopic("Einstellungskanal von <#" + vc.getId() + "> (ID: " + vc.getId() + ")")
                .addPermissionOverride(Roles.everyone.getRole(guild), List.of(Permission.EMPTY_PERMISSIONS), List.of(Permission.VIEW_CHANNEL))
                .complete();

        tempSettings.upsertPermissionOverride(owner).grant(Permission.VIEW_CHANNEL).grant(Permission.MESSAGE_SEND).grant(Permission.VOICE_SPEAK).queue();
        tempSettings.upsertPermissionOverride(Roles.SUP.getRole(guild)).grant(Permission.VIEW_CHANNEL).grant(Permission.MANAGE_CHANNEL).queue();
        tempSettings.upsertPermissionOverride(Roles.MOD.getRole(guild)).grant(Permission.VIEW_CHANNEL).grant(Permission.MANAGE_CHANNEL).queue();
        tempSettings.upsertPermissionOverride(Roles.ADMIN.getRole(guild)).grant(Permission.VIEW_CHANNEL).grant(Permission.MANAGE_CHANNEL).queue();

        tempSettings.sendMessage(owner.getAsMention()).complete().delete().queue();
        tempSettings.sendTyping().queue();

        return tempSettings;

    }

    public void updateMessage() {

        if(embedMessage == null) {
            embedMessage = settings.sendMessage(TempchannelPageSystem.getMainEmbed(this)).complete();
        }else {
            embedMessage.editMessage(TempchannelPageSystem.getMainEmbed(this)).queue();
        }

    }

    public InteractionHook getHookByRandomId(String id) {

        for(Map.Entry<InteractionHook, String> entry : ephList.entrySet()) {
            String[] args = entry.getValue().split("_");
            if(args.length >= 3) {
                if (entry.getValue().split("_")[2].equals(id)) {
                    return entry.getKey();
                }
            }
        }

        return null;

    }

    public void updateMessages(String id) {

        for(Map.Entry<InteractionHook, String> entry : ephList.entrySet()) {
            if(entry.getValue().startsWith(id)) {

                InteractionHook hook = entry.getKey();
                Tempchannel channel = ControlListener.getTempchannel((TextChannel) hook.getInteraction().getChannel());

                System.out.println(id + " / "+ hook.getInteraction().getId());

                if(id.startsWith("editmember")) {
                    hook.editOriginal(getMemberView(guild.getMemberById(id.split("_")[1]), hook.getInteraction().getMember())).queue(); return;
                }

                if(id.startsWith("menu_")) {
                    String getId = entry.getValue().split("_")[2];
                    Member m = hook.getInteraction().getMember();
                    String[] args = id.split("_");
                    switch(args[1]) {
                        case "moderation":
                            hook.editOriginal(TempchannelPageSystem.getModerationEmbed(channel, getId, m)).queue(); break;
                        case "activity":
                            hook.editOriginal(TempchannelPageSystem.getActivityEmbed(channel, getId, m)).queue(); break;
                        case "channel":
                            hook.editOriginal(TempchannelPageSystem.getChannelEmbed(channel, getId, m)).queue(); break;
                        case "roles":
                            hook.editOriginal(TempchannelPageSystem.getRoleEmbed(channel, getId, m)).queue(); break;
                    }
                }

            }
        }

    }

    public Message getMemberView(Member member, Member viewing) {

        MessageBuilder mb = new MessageBuilder();
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Nutzerübersicht - " + member.getUser().getAsTag());
        eb.setDescription("Hier kannst du " + member.getAsMention() + " in deinem Kanal moderieren");
        eb.setThumbnail(member.getUser().getEffectiveAvatarUrl());

        Button ban = null;
        Button mute = null;

        if(banned.containsKey(member)) {
            ban = Button.secondary("tc_unban_" + member.getId(), "Entbannen");
            eb.addField("Gebannt von", banned.get(member).getAsMention(), true);
        }else {
            ban = Button.danger("tc_ban_" + member.getId(), "Bannen");
        }

        if(muted.containsKey(member)) {
            mute = Button.secondary("tc_unmute_" + member.getId(), "Mute aufheben");
            eb.addField("Stummgeschaltet von", muted.get(member).getAsMention(), true);
        }else {
            mute = Button.danger("tc_mute_" + member.getId(), "Muten");
        }

        mb.setEmbeds(eb.build());
        if(member.equals(viewing)) {
            mb.setActionRows(ActionRow.of(ban.asDisabled(), mute.asDisabled()));
        }else {
            mb.setActionRows(ActionRow.of(ban, mute));
        }

        return mb.build();

    }

    public Member changeOwner() {

        if(!vc.getMembers().contains(owner)) {

            vc.upsertPermissionOverride(owner).reset().queue();

            if(!modList.isEmpty()) {
                owner = modList.get(0);
            }else {
                owner = vc.getMembers().get(0);
            }

            settings.upsertPermissionOverride(owner).grant(Permission.VIEW_CHANNEL).grant(Permission.MESSAGE_SEND).queue();

            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(0x3b76c4);
            embedBuilder.setTitle(":crown: Eigentumsrechte auf Tempchannel erhalten!");
            embedBuilder.setDescription("Da der Inhaber den Sprachkanal verlassen hat, wurde die Eigentumsrechte nun lauf dich übertragen! Du besitzt nun alle Berechtigungen und kannst den Kanal über <#" + settings.getId() + "> verwalten oder anderen Moderatoren hinzufügen!");

            owner.getUser().openPrivateChannel().complete().sendTyping().queue();
            owner.getUser().openPrivateChannel().complete().sendMessageEmbeds(embedBuilder.build()).queue();

            if(modList.contains(owner)) {
                modList.remove(owner);
            }
            updateMessages("menu_roles");

            return owner;

        }

        return null;

    }

    public void delete() {

        this.vc.delete().queue();
        this.settings.delete().queue();

        if(ControlListener.tempChannels.containsKey(vc.getId())) {

            ControlListener.tempChannels.remove(vc.getId());

        }

    }

    public VoiceChannel getVoiceChannel() {
        return this.vc;
    }

    public boolean isPermitted(Member member) {

        if(Roles.TEMPCHANNEL_MODERATOR.hasAnyRoles(member.getGuild(), member)) {
            return true;
        }
        if(owner.equals(member)) {
            return true;
        }
        if(modList.contains(member)) {
            return true;
        }

        return false;

    }

    public void updatePermissions() {

        for(PermissionOverride po : settings.getMemberPermissionOverrides()) {

            if(modList.contains(po.getMember()) || owner.equals(po.getMember())) {
                settings.upsertPermissionOverride(po.getMember()).grant(Permission.VIEW_CHANNEL).grant(Permission.MESSAGE_SEND).queue();
            }else {
                po.delete().queue();
            }

        }

        for(Member member : modList) {

            settings.upsertPermissionOverride(member).clear().grant(Permission.VIEW_CHANNEL).grant(Permission.MESSAGE_SEND).queue();

        }

    }

}
