package de.splayfer.roonie.modules.poll;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class PollEnterListener extends ListenerAdapter {

    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.getGuild().equals(Roonie.mainGuild)) {
            if (Poll.isPoll(event.getChannel(), event.getMessage())) {
                Poll poll = Poll.getFromMongoBD(event.getChannel(), event.getMessage());
                if (poll.hasVoted(event.getMember())) {
                    if (poll.hasClicked(event.getMember(), event.getButton().getId()))
                        poll.updateVote(event.getMember(), event.getButton().getId(), VoteAction.UNVOTE);
                    else {
                        poll.updateVote(event.getMember(), poll.getVote(event.getMember()), VoteAction.UNVOTE);
                        poll.updateVote(event.getMember(), event.getButton().getId(), VoteAction.VOTE);
                    }
                } else
                    poll.updateVote(event.getMember(), event.getButton().getId(), VoteAction.VOTE);
                event.deferEdit().queue();
            }
        }
    }
}