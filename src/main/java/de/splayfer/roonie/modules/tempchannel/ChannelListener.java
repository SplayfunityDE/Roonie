package de.splayfer.roonie.modules.tempchannel;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.PermissionOverride;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ChannelListener extends ListenerAdapter {

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        if (event.getVoiceState().inAudioChannel() && !event.getVoiceState().getChannel().getType().equals(ChannelType.STAGE)) {

            if (TempchannelManager.existesJoinHub(event.getChannelJoined().getIdLong())) {
                createNewChannel(event.getGuild(), event.getMember());
                return;
            }
            if(!delete((VoiceChannel) event.getChannelLeft())) {
                ControlListener.tempChannels.get(event.getChannelLeft().getId()).changeOwner();
            }
            VoiceChannel vc = event.getGuild().getVoiceChannelById(event.getChannelJoined().getId());
            if (vc != null) {
                canSpeak(event.getMember(), vc);
                Tempchannel channel = ControlListener.getTempchannel(vc);

                //Tempchannel == null wenn channel kein Tempchannel
                if (channel != null) {
                    channel.getVoiceChannel().upsertPermissionOverride(event.getMember()).grant(Permission.MESSAGE_SEND).queue();
                    channel.updateMessage();
                    channel.updateMessages("menu_moderation");
                    channel.updateMessages("menu_roles");
                }

            }
        } else {

            if (!delete((VoiceChannel) event.getChannelLeft())) {
                ControlListener.tempChannels.get(event.getChannelLeft().getId()).changeOwner();
            }

            VoiceChannel vc = event.getGuild().getVoiceChannelById(event.getChannelLeft().getId());
            if (vc != null) {
                Tempchannel channel = ControlListener.getTempchannel(vc);
                if (channel != null) {
                    channel.getVoiceChannel().upsertPermissionOverride(event.getMember()).deny(Permission.MESSAGE_SEND).queue();
                }
            }

        }

    }

    private void createNewChannel(Guild guild, Member member) {
        new Tempchannel(guild, member);
    }

    public boolean delete(VoiceChannel voiceChannel) {
        if(ControlListener.isTempchannel(voiceChannel)) {
            if(voiceChannel.getMembers().isEmpty()) {
                ControlListener.tempChannels.get(voiceChannel.getId()).delete();
                return true;
            }
        } else {
            return true;
        }
        return false;

    }

    public void canSpeak(Member member, VoiceChannel channel) {

        boolean canSpeak = true;

        for(int i = (channel.getRolePermissionOverrides().size() - 1); i >= 0; i--) {

            PermissionOverride po = channel.getRolePermissionOverrides().get(i);

            if(member.getRoles().contains(po.getRole()) || po.getRole().getId().equals("873506353551925308")) {
                if(po.getDenied().contains(Permission.VOICE_SPEAK)) {
                    canSpeak = false;
                }else if(po.getAllowed().contains(Permission.VOICE_SPEAK)) {
                    canSpeak = true;
                }
            }

        }

        for(int i = (channel.getMemberPermissionOverrides().size() - 1); i >= 0; i--) {

            PermissionOverride po = channel.getMemberPermissionOverrides().get(i);

            if(po.getMember().equals(member)) {
                if(po.getDenied().contains(Permission.VOICE_SPEAK)) {
                    canSpeak = false;
                }else if(po.getAllowed().contains(Permission.VOICE_SPEAK)) {
                    canSpeak = true;
                }
            }

        }
        if(canSpeak) {
            member.mute(false).queue();
        }else {
            member.mute(true).queue();
        }
    }
}