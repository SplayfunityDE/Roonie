package de.splayfer.roonie.modules.music.commands;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.modules.music.MusicController;
import de.splayfer.roonie.modules.music.PlayerManager;
import de.splayfer.roonie.utils.DefaultMessage;
import de.splayfer.roonie.utils.enums.Embeds;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ShuffelCommand extends ListenerAdapter {

    private final PlayerManager playerManager;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("shuffel")) {
            MusicController controller = playerManager.getController(event.getGuild().getIdLong());
            if (controller.getQueue().shuffel()) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setColor(Embeds.BANNER_MUSIC_SHUFFEL.getColor());
                embedBuilder.setTitle("**:twisted_rightwards_arrows: WARTESCHLANGE GESCHUFFELT**");
                embedBuilder.setDescription("> Die Reihenfolge der Inhalte aus der Warteschlange wurde durchgemischt");
                embedBuilder.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
                event.replyEmbeds(Embeds.BANNER_MUSIC_SHUFFEL, embedBuilder.build()).setEphemeral(true).queue();
            } else {
                event.replyEmbeds(DefaultMessage.error("Keine Warteschlange", "Es gibt keine Inhalte aus der Warteschlange, die gemischt werden können")).setEphemeral(true).queue();
            }
        }
    }
}
