package de.splayfer.roonie.partner;

import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PartnerUnlockListener extends ListenerAdapter {

    protected Role partnerRole;

    public void onButtonInteraction (ButtonInteractionEvent event) {

        if (event.getButton().getId().equals("unlockpartner")) {

            partnerRole = event.getGuild().getRoleById("894140008137621505");

            if (!event.getMember().getRoles().contains(partnerRole)) {

                event.getGuild().getManager().getGuild().addRoleToMember(UserSnowflake.fromId(event.getMember().getId()), partnerRole).queue();

                event.deferEdit().queue();

            }

        }

    }

}
