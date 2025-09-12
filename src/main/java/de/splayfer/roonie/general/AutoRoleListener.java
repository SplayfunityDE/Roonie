package de.splayfer.roonie.general;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
public class AutoRoleListener extends ListenerAdapter {

    public void onGuildMemberJoin (GuildMemberJoinEvent event) {
        if (event.getGuild().equals(Roonie.mainGuild))
            for (Role role : Roonie.autoRoles)
                Roonie.mainGuild.addRoleToMember(event.getUser(), role).queue();
    }
}
