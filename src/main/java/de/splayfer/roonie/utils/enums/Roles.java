package de.splayfer.roonie.utils.enums;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum Roles {

    SPLAYFER ("splayfer", "883280882088165407"),
    ADMIN ("admin", "873515181114806272"),
    MOD ("mod", "873508502063169556"),
    CONTENT ("content", "902610342700519455"),
    DEV ("dev", "921818389368930364"),
    SUP ("sup", "873515229470928937"),
    AZUBI ("azubi", "880863788763603054"),

    LVL5 ("lvl5", "883286077824172042"),
    LVL10 ("lvl10", "883286102897725482"),
    LVL20 ("lvl20", "883287727930482708"),
    LVL30 ("lvl30", "883286178542022687"),
    LVL40 ("lvl40", "883287260294950972"),
    LVL50 ("lvl50", "883287013141409823"),
    LVL100 ("lvl100", "883286116474716180"),

    LVLROLES(Roles.LVL5, Roles.LVL10, Roles.LVL20, Roles.LVL30, Roles.LVL40, Roles.LVL50, Roles.LVL100),

    //verify

    VERIFY1 ("verify1", "891292396288098355"),
    VERIFY2 ("verify2", "891292508527673374"),
    VERIFY3 ("verify3", "891292529364992011"),
    VERIFYCOMPLETE ("verifycomplete", "891292580036349952"),

    //pings

    GIVEAWAYS ("giveaways", "891720473094795304"),
    EVENTS ("events", "891720541826859038"),
    NEUIGKEITEN ("neuigkeiten", "891721689166139443"),
    UMFRAGEN ("umfragen", "891721706455056395"),
    BIBLIOTHEK ("bibliothek", "891722055244980257"),
    PARTNER ("partner", "891722134521540690"),

    PINGROLES (Roles.GIVEAWAYS, Roles.EVENTS, Roles.NEUIGKEITEN, Roles.UMFRAGEN, Roles.BIBLIOTHEK, Roles.PARTNER),

    //platform

    WINDOWS ("windows", "891675735444582410"),
    APPLE ("apple", "891675753404596265"),
    ANDROID ("android", "891675772123775006"),
    NINTENDO ("nintendo", "891675792193511456"),
    XBOX ("xbox", "891675811076263946"),
    PLAYSTATION ("playstation", "891675828226777099"),

    PLATFORM (WINDOWS, APPLE, ANDROID, NINTENDO, XBOX, PLAYSTATION),

    //hobbys

    SPORT ("sport", "891675869398044732"),
    GAMING ("gaming", "891675890180821032"),
    CODEN ("coden", "891675915707355186"),
    KUNST ("kunst", "891675935932305408"),
    WISSENSCHAFT ("wissenschaft", "891675935638695996"),
    MUSIK ("musik", "891716033004580865"),
    TRAINSPOTTING ("trainspotting", "891676011559796736"),

    TEAM (Roles.ADMIN, Roles.MOD, Roles.SUP, Roles.CONTENT, Roles.DEV),
    PRETEAM (Roles.AZUBI),

    TEAM_EXTENDS (Roles.TEAM, Roles.PRETEAM),

    // Team Permissions

    TEMPCHANNEL_MODERATOR (Roles.MOD, Roles.ADMIN, Roles.SPLAYFER, Roles.DEV),

    everyone ("everyone", "873506353551925308"),
    MEMBER ("member", "891292965241233448");

    private final ArrayList<String> roleIds;
    private String name;

    private Roles(String name, String... roleIds){
        this.roleIds = new ArrayList<>(Arrays.asList(roleIds));
        this.name = name;
    }

    private Roles(Roles... roles) {

        this.roleIds = new ArrayList<>();

        for(Roles role : roles) {
            roleIds.addAll(role.roleIds);
        }
    }

    public ArrayList<Role> getRoles(Guild guild) {

        return (ArrayList<Role>) this.roleIds.stream().map(guild::getRoleById).collect(Collectors.toList());

    }

    public Role getRole(Guild guild) {

        return getRoles(guild).get(0);

    }

    public boolean hasRoles(Guild guild, Member member) {
        return member.getRoles().containsAll(this.getRoles(guild));
    }

    public boolean hasAnyRoles(Guild guild, Member member) {

        return this.getRoles(guild).stream().anyMatch((role) -> member.getRoles().contains(role));

    }

    public static Role byName(String Name, Guild Guild) {

        for(Roles roles : Roles.values()) {
            if(roles.name.equals(Name)) {
                return Guild.getRoleById(roles.roleIds.get(0));
            }
        }

        return null;

    }

}
