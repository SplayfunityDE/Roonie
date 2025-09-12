package de.splayfer.roonie.utils.enums;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;

public enum Channels {

    MEDIACHANNEL(985551183479463998L),
    TICKETFORUM(1186235258027393075L),
    TICKETPANEL(908795138623537232L),
    COMMANDCHAT(1175859916565643427L),
    MAINCHAT(883278317753626655L);

    private final long id;
    private Channels(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public MessageChannel getMessageChannel(Guild guild) {
        return guild.getChannelById(MessageChannel.class, id);
    }

    public GuildChannel getGuildChannel(Guild guild) {
        return guild.getGuildChannelById(id);
    }

    public TextChannel getTextChannel(Guild guild) {
        return guild.getTextChannelById(id);
    }
}