package de.splayfer.roonie.general;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;
public class AutoRoleListener extends ListenerAdapter {

    YamlConfiguration yml;

    public void onGuildMemberJoin (GuildMemberJoinEvent event) {

        Role memberRole = event.getGuild().getRoleById("891292965241233448");
        Role verify1 = event.getGuild().getRoleById("891292396288098355");

        //adding default member role

        event.getGuild().getManager().getGuild().addRoleToMember(event.getUser(), memberRole).queue();
        event.getGuild().getManager().getGuild().addRoleToMember(event.getUser(), verify1).queue();

    }

    public static void fixMemberRoles() {

            Guild guild = Roonie.shardMan.getGuildById(Roonie.mainServerId);

            for (Member m : guild.getMembers()) {

                if (!m.getUser().isBot()) {

                    if (!m.getRoles().contains(guild.getRoleById("891292965241233448"))) {

                        guild.getManager().getGuild().addRoleToMember(m, guild.getRoleById("891292965241233448")).queue();

                    }

                }

            }
        }



}
