package de.splayfer.roonie.modules.music;

import de.splayfer.roonie.Roonie;

import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {

    public ConcurrentHashMap<Long, MusicController> controller;

    public PlayerManager() {
        this.controller = new ConcurrentHashMap<Long, MusicController>();
    }

    public MusicController getController(long guildid) {
        MusicController mc = null;

        if(this.controller.containsKey(guildid)) {
            mc = this.controller.get(guildid);
        }
        else {
            mc = new MusicController(Roonie.shardMan.getGuildById(guildid));

            this.controller.put(guildid, mc);
        }

        return mc;
    }

    public long getGuildByPlayerHash(int hash) {
        for(MusicController controller : this.controller.values()) {
            if(controller.getPlayer().hashCode() == hash) {
                return controller.getGuild().getIdLong();
            }
        }

        return -1;
    }
}
