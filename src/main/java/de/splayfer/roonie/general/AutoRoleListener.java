package de.splayfer.roonie.general;

import de.splayfer.roonie.Roonie;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoRoleListener extends ListenerAdapter {

    private final Roonie roonie;

    public void onGuildMemberJoin (GuildMemberJoinEvent event) {
        if (event.getGuild().equals(roonie.getMainGuild()))
            for (Role role : roonie.getAutoRoles())
                roonie.getMainGuild().addRoleToMember(event.getUser(), role).queue();
    }
}
