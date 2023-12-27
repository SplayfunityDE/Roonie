package de.splayfer.roonie.utils.enums;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;

public enum Channels {

    TICKETFORUM(1186235258027393075L),
    TICKETPANEL(908795138623537232L);

    private final long id;
    private Channels(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }
}