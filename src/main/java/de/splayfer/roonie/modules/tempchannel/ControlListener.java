package de.splayfer.roonie.modules.tempchannel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.Region;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.InteractionHook;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectOption;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import net.dv8tion.jda.api.utils.messages.MessageEditData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

public class ControlListener extends ListenerAdapter {

    protected Role adminrole;
    protected Role modrole;
    protected Role suprole;

    public static HashMap<String, Tempchannel> tempChannels = new HashMap<>();

    public static boolean isTempchannel(VoiceChannel voiceChannel) {
       for(Entry<String, Tempchannel> entry : tempChannels.entrySet()) {
           if(entry.getValue().getVoiceChannel().equals(voiceChannel)) {
               return true;
           }
           System.out.println("false");
       }

       return false;

    }

    public static Tempchannel getTempchannel(TextChannel channel) {

        for(Entry<String, Tempchannel> entry : tempChannels.entrySet()) {
            if(entry.getValue().settings.equals(channel)) {
                return entry.getValue();
            }
        }

        return null;

    }

    public static Tempchannel getTempchannel(VoiceChannel channel) {

        for(Entry<String, Tempchannel> entry : tempChannels.entrySet()) {
            if(entry.getValue().vc.equals(channel)) {
                return entry.getValue();
            }
        }

        return null;

    }

    //public ArrayList<Function<SelectMenuInteractionEvent, Boolean>> abc = new ArrayList<>();

    public void onStringSelectInteraction (StringSelectInteractionEvent event) {

        if(event.getComponentId().startsWith("tc_")) {

            String[] args = event.getComponentId().split("_");
            Tempchannel tchannel = getTempchannel(event.getChannel().asTextChannel());

            if(tchannel == null) {
                event.reply("Du kannst diese Funktion nur in Einstellungskanälen nutzen!").setEphemeral(true).queue(); return;
            }

            if(!tchannel.isPermitted(event.getMember())) {
                event.reply("Du hast keine Berechtigung, um Einstellungen an diesem Kanal vorzunehmen!").setEphemeral(true).queue(); return;
            }

            switch (args[1]) {

                case "selectregion":

                    tchannel.vc.getManager().setRegion(Region.valueOf(event.getValues().get(0))).queue();

                    EmbedBuilder reply1 = new EmbedBuilder();
                    reply1.setColor(0x43b480);
                    reply1.setTitle(":map: REGION ERFOLGREICH AKTUALISIERT!");
                    reply1.setDescription("Du hast die Region deines Sprachkanals erfolgreich geändert!");
                    reply1.addField("Neue Region:", event.getInteraction().getSelectedOptions().get(0).getEmoji().getFormatted() +  " " + event.getSelectedOptions().get(0).getLabel(), false);
                    reply1.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                    //event.replyEmbeds(Embeds.SUCCESS_BANNER.getEmbed(), reply1.build()).setEphemeral(true).queue();
                    event.deferEdit().queue();
                    return;

                case "selectActivity":

                    String gameUrl = tchannel.vc.createInvite().setTargetApplication(event.getValues().get(0)).complete().getUrl();
                    Button linked = Button.link(gameUrl, "Starte die Aktivität").withEmoji(Emoji.fromCustom("support", Long.parseLong("880028066733236264"), false));

                    EmbedBuilder reply2 = new EmbedBuilder();
                    reply2.setTitle(":bowling: Aktivität erfolgreich geladen!");
                    reply2.setDescription("Klicke nun auf den Button unter dieser Nachricht, um die Aktivität zu starten!");
                    reply2.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

                    event.replyEmbeds(TcEmbeds.SUCCESS_BANNER.getEmbed(), reply2.build()).setEphemeral(true).addActionRow(linked).queue();

                    EmbedBuilder userreply = new EmbedBuilder();
                    userreply.setDescription("Klicke nun auf den Button unter dieser Nachricht, um die Aktivität zu starten!");

                    tchannel.getVoiceChannel().sendMessageEmbeds(userreply.build()).setActionRow(linked).queue();
                    return;

                case "controlmod":
                    tchannel.modList.clear();
                    for(SelectOption option : event.getComponent().getOptions()) {
                        Member member = event.getGuild().getMemberById(option.getValue().split("_")[2]);
                        if(member != null) {
                            if(event.getSelectedOptions().contains(option)) {
                                tchannel.modList.add(member);
                            }
                        }
                    }

                    tchannel.updatePermissions();
                    tchannel.updateMessages("menu_roles");

                    //event.replyEmbeds(reply3.build()).setEphemeral(true).queue();
                    event.deferEdit().queue();
                    return;

                case "controlmembers":
                    String id = event.getSelectedOptions().get(0).getValue().split("_")[2];
                    if(id.equals("id")) {
                        TextInput.Builder fname = TextInput.create("tc_field_memberid", "ID des Mitglieds", TextInputStyle.SHORT).setPlaceholder("000000000000000000").setRequiredRange(18, 20).setRequired(true);
                        Modal modal = Modal.create("tc_moderatemember", "Mitglied mit ID auswählen").addActionRow(fname.build()).build();

                        event.replyModal(modal).queue();
                        String getId = event.getMessage().getEmbeds().get(0).getFooter().getText().substring(4);
                        tchannel.updateMessages("menu_moderation_" + getId);
                        return;
                    }else {
                        Member member = event.getGuild().getMemberById(event.getSelectedOptions().get(0).getValue().split("_")[2]);
                        if(member == null) {
                            event.reply("Es wurde kein Mitglied gefunden! Bitte überprüfe die angegebene ID.").setEphemeral(true).queue(); return;
                        }

                        tchannel.ephList.put(event.reply(tchannel.getMemberViewCreate(member, event.getMember())).setEphemeral(true).complete(), "editmember_" + member.getId());
                        tchannel.updateMessages("menu_roles");
                        return;
                    }

            }

            System.out.println("ERROR (Value: " + args[1] + ") \n" + Thread.currentThread().getStackTrace().toString());
            event.reply("Ein unerwarteter Fehler ist aufgetreten! Weitere Informationen wurden im Log gespeichert.").setEphemeral(true).queue();

        }

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

        if(event.getChannelType().equals(ChannelType.TEXT) && !event.getAuthor().isBot()) {

            Tempchannel channel = getTempchannel(event.getChannel().asTextChannel());

            if(channel != null) {
                event.getMessage().delete().queueAfter(7, TimeUnit.SECONDS);
            }

        }

    }

    @Override
    public void onModalInteraction(ModalInteractionEvent event) {

        if(event.getModalId().startsWith("tc_")) {

            String[] args = event.getModalId().split("_");
            Tempchannel channel = getTempchannel(event.getChannel().asTextChannel());

            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");

            EmbedBuilder success = new EmbedBuilder();
            success.setColor(0xE03F3F);
            success.setFooter("\uD83D\uDD59 Uhrzeit: " + formatter.format(new Date()) + " Uhr");

            EmbedBuilder error = new EmbedBuilder();
            error.setColor(0xc01c34);

            switch(args[1]) {

                case "setname":

                    channel.name = event.getValue("tc_field_name").getAsString();
                    channel.nameUpdated = true;
                    channel.getVoiceChannel().getManager().setName(channel.getName()).queue();

                    success.setTitle("Name erfolgreich geändert!");
                    success.setDescription("<:text:886623802954498069> Der Name des Sprachkanals wurde erfolgreich geändert!");
                    success.addField("Details zu dieser Aktion", "<:name:877158821397663784> Name: `" + event.getValue("tc_field_name").getAsString() + "`", false);

                    //event.replyEmbeds(success.build()).setEphemeral(true).queue();
                    event.deferEdit().queue();

                    channel.updateMessage();
                    break;

                case "setlimit":
                    Integer limit = getNumber(event.getValue("tc_field_limit").getAsString());
                    if(limit != null) {
                        if(limit >= 0) {
                            channel.vc.getManager().setUserLimit(limit).queue();
                            channel.limit = limit;

                            success.setTitle("Limitänderung erfolgreich!");
                            success.setDescription("<:name:877158821397663784> Das Limit des Kanals wurde erfolgreich geändert!");
                            success.addField("Details zu dieser Aktion", "<:name:877158821397663784> Limit: `" + limit + "`", false);

                            //event.replyEmbeds(success.build()).setEphemeral(true).queue();
                            event.deferEdit().queue();
                        } else {
                            error.setTitle(":exclamation: **Bitte wähle eine Zahl zwischen 0 und 99!**");
                            event.replyEmbeds(error.build()).setEphemeral(true).queue();
                        }
                    }else {
                        error.setTitle(":exclamation: **Du musst eine Zahl angeben!**");
                        event.replyEmbeds(error.build()).setEphemeral(true).queue();
                    }

                    channel.updateMessage();
                    break;

                case "moderatemember":
                    Member member = event.getGuild().getMemberById(event.getValue("tc_field_memberid").getAsString());

                    if(member == null) {
                        event.reply("Es wurde kein Mitglied gefunden! Bitte überprüfe die angegebene ID.").setEphemeral(true).queue(); return;
                    }

                    channel.ephList.put(event.reply(channel.getMemberViewCreate(member, event.getMember())).setEphemeral(true).complete(), "editmember_" + member.getId());
                    channel.updateMessages("menu_roles");

            }
        }
    }

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {

        if(event.getComponentId().startsWith("tc_menu_")) {

            String[] args = event.getComponentId().split("_");
            Tempchannel channel = getTempchannel(event.getChannel().asTextChannel());

            if(args[2].equals("configure")) {
                String id = generateId();
                InteractionHook hook = event.reply(TempchannelPageSystem.getChannelEmbedCreate(channel, id, event.getMember())).setEphemeral(true).complete();
                channel.ephList.put(hook, "menu_channel_" + id);
                return;
            }

            MessageEditData message = null;
            String id = event.getMessage().getEmbeds().get(0).getFooter().getText().substring(4);

            switch(args[2]) {

                case "moderation":
                    message = TempchannelPageSystem.getModerationEmbedEdit(channel, id, event.getMember()); break;
                case "activity":
                    message = TempchannelPageSystem.getActivityEmbedEdit(channel, id, event.getMember()); break;
                case "channel":
                    message = TempchannelPageSystem.getChannelEmbedEdit(channel, id, event.getMember()); break;
                case "roles":
                    message = TempchannelPageSystem.getRoleEmbedEdit(channel, id, event.getMember()); break;
            }

            event.deferEdit().queue();

            InteractionHook hook = channel.getHookByRandomId(id);
            hook.editOriginal(message).queue();
            channel.ephList.put(hook, "menu_" + args[2] + "_" + id);

            return;

        }

        if(event.getComponentId().startsWith("tc_")) {

            Tempchannel channel = getTempchannel(event.getChannel().asTextChannel());
            String[] args = event.getComponentId().split("_");

            if(channel.isPermitted(event.getMember())) {

                SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");

                EmbedBuilder response = new EmbedBuilder();
                response.setColor(0x3b76c4);

                response.setFooter("\uD83D\uDD59 Uhrzeit: " + formatter.format(new Date()) + " Uhr");

                switch(args[1]) {

                    case "controlname":
                        TextInput.Builder fname1 = TextInput.create("tc_field_name", "Neuer Name", TextInputStyle.SHORT).setPlaceholder(channel.getVoiceChannel().getName()).setRequiredRange(0, 50);
                        if(channel.nameUpdated) {
                            fname1.setPlaceholder(channel.getName("⏳")).setValue(channel.name);
                        }
                        Modal modal1 = Modal.create("tc_setname", "Kanalnamen setzen").addActionRow(fname1.build()).build();

                        event.replyModal(modal1).queue();
                        break;

                    case "controllimit":
                        TextInput fname2 = TextInput.create("tc_field_limit", "Neues Limit", TextInputStyle.SHORT).setPlaceholder("0-99").setRequiredRange(1, 2).setValue(String.valueOf(channel.getVoiceChannel().getUserLimit())).build();
                        Modal modal2 = Modal.create("tc_setlimit", "Kanallimit setzen").addActionRow(fname2).build();

                        event.replyModal(modal2).queue();
                        break;

                    case "controlmod":
                        if(!channel.owner.equals(event.getMember())) {
                            event.reply("Nur der Kanalinhaber kann die **Kanalmoderatoren** festlegen!").setEphemeral(true).queue();
                            return;
                        }
                        response.setTitle("Wähle Moderatoren aus");
                        response.setDescription("Du kannst aus der angegebenen Liste Kanalmitglieder auswählen, die als Moderator tätig sein sollen.");

                        StringSelectMenu.Builder list = StringSelectMenu.create("tc_controlmod");
                        list.setPlaceholder("Wähle Moderatoren aus");
                        List<String> selected = new ArrayList<>();

                        for(Member member : channel.modList) {
                            list.addOption(member.getEffectiveName(), "tc_selectmod_" + member.getId(), member.getUser().getAsTag() + " (" + member.getId() + ")", Emoji.fromUnicode(member.getRoles().get(0).getIcon().getEmoji()));
                            selected.add("tc_selectmod_" + member.getId());
                        }
                        for(Member member : channel.getVoiceChannel().getMembers()) {
                            if(!channel.modList.contains(member) && member != channel.owner) {
                                list.addOption(member.getEffectiveName(), "tc_selectmod_" + member.getId(), member.getUser().getAsTag() + " (" + member.getId() + ")", Emoji.fromUnicode(member.getRoles().get(0).getIcon().getEmoji()));
                            }
                        }
                        if(!selected.isEmpty()) {
                            list.setDefaultValues(selected);
                        }

                        event.replyEmbeds(response.build()).addActionRow(list.build()).setEphemeral(true).queue(); break;

                    case "ban":
                        Member member = event.getGuild().getMemberById(event.getComponentId().split("_")[2]);

                        channel.getVoiceChannel().upsertPermissionOverride(member).deny(Permission.ALL_VOICE_PERMISSIONS).grant(Permission.VIEW_CHANNEL).queue();
                        if(member.getVoiceState().inAudioChannel() && member.getVoiceState().getChannel().getId().equals(channel.getVoiceChannel().getId())) {
                            member.getGuild().kickVoiceMember(member).queue();
                        }

                        channel.banned.put(member, event.getMember());
                        channel.updateMessages("editmember_" + member.getId());
                        channel.updateMessages("menu_moderation");
                        channel.getVoiceChannel().upsertPermissionOverride(member).deny(Permission.MESSAGE_SEND).queue();
                        event.deferEdit().queue();
                        break;

                    case "unban":
                        Member member2 = event.getGuild().getMemberById(event.getComponentId().split("_")[2]);

                        channel.getVoiceChannel().upsertPermissionOverride(member2).clear().queue();

                        channel.banned.remove(member2);
                        channel.updateMessages("editmember_" + member2.getId());
                        channel.updateMessages("menu_moderation");
                        channel.getVoiceChannel().upsertPermissionOverride(member2).grant(Permission.MESSAGE_SEND).queue();
                        event.deferEdit().queue();
                        break;

                    case "mute":
                        Member member3 = event.getGuild().getMemberById(event.getComponentId().split("_")[2]);

                        channel.getVoiceChannel().upsertPermissionOverride(member3).deny(Permission.VOICE_SPEAK).queue();
                        if(member3.getVoiceState().getChannel().getId().equals(channel.getVoiceChannel().getId())) {
                            member3.mute(true).queue();
                        }

                        channel.muted.put(member3, event.getMember());
                        channel.updateMessages("editmember_" + member3.getId());
                        channel.updateMessages("menu_moderation");
                        channel.getVoiceChannel().upsertPermissionOverride(member3).deny(Permission.MESSAGE_SEND).queue();
                        event.deferEdit().queue();
                        break;

                    case "unmute":
                        Member member4 = event.getGuild().getMemberById(event.getComponentId().split("_")[2]);

                        channel.getVoiceChannel().upsertPermissionOverride(member4).grant(Permission.VOICE_SPEAK).queue();
                        if(member4.getVoiceState().inAudioChannel() && member4.getVoiceState().getChannel().getId().equals(channel.getVoiceChannel().getId())) {
                            member4.mute(false).queue();
                        }

                        channel.muted.remove(member4);
                        channel.updateMessages("editmember_" + member4.getId());
                        channel.updateMessages("menu_moderation");
                        channel.getVoiceChannel().upsertPermissionOverride(member4).grant(Permission.MESSAGE_SEND).queue();
                        event.deferEdit().queue();
                        break;

                }

            }else {
               event.reply("Du hast keine Berechtigung, um Einstellungen an diesem Kanal vorzunehmen!").setEphemeral(true).queue();
            }

        }

    }

    public String generateId() {

        String charSet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder random = new StringBuilder();

        for(int i = 0; i < 15; i++) {

            random.append(charSet.charAt((int) (Math.random() * (charSet.length() - 1))));

        }

        return random.toString();

    }

    public Integer getNumber(String Number) {

        try {
            return Integer.valueOf(Number);
        }catch (NumberFormatException e) {
            return null;
        }

    }

}
