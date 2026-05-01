package de.splayfer.roonie.modules.poll;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.Properties;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PollEnterListener extends ListenerAdapter {

    private final PollManager pollManager;
    private final Properties properties;

    public void onButtonInteraction(ButtonInteractionEvent event) {
        if (event.isFromGuild()) {
            if (event.getGuild().getId().equals(properties.getMainGuild())) {
                if (pollManager.isPoll(event.getChannel(), event.getMessage())) {
                    Poll poll = pollManager.getFromMongoBD(event.getChannel(), event.getMessage());
                    if (poll.hasVoted(event.getMember())) {
                        if (poll.hasClicked(event.getMember(), event.getButton().getCustomId()))
                            poll.updateVote(event.getMember(), event.getButton().getCustomId(), VoteAction.UNVOTE);
                        else {
                            poll.updateVote(event.getMember(), poll.getVote(event.getMember()), VoteAction.UNVOTE);
                            poll.updateVote(event.getMember(), event.getButton().getCustomId(), VoteAction.VOTE);
                        }
                    } else
                        poll.updateVote(event.getMember(), event.getButton().getCustomId(), VoteAction.VOTE);
                    event.deferEdit().queue();
                }
            }
        }
    }
}