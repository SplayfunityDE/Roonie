package de.splayfer.roonie.utils.enums;

public enum Guilds {

    MAIN(873506353551925308L),
    UNBAN(879322210706276382L),
    GLOBAL(-1L);

    private long guildId;
    Guilds(long id) {
        this.guildId = id;
    }

    public long getId() {
        return this.guildId;
    }

}
