package de.splayfer.roonie.modules.music.commands;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.modules.music.MusicController;
import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class ShuffelCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("shuffle")) {
            MusicController controller = Roonie.playerManager.getController(event.getGuild().getIdLong());
            controller.getQueue().shuffel();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Embeds.BANNER_MUSIC_SHUFFEL.getColor());
            embedBuilder.setTitle("**:twisted_rightwards_arrows: WARTESCHLANGE GESCHUFFELT**");
            embedBuilder.setDescription("> Die Reihenfolge der Inhalte aus der Warteschlange wurde durchgemischt");
            embedBuilder.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
            event.replyEmbeds(Embeds.BANNER_MUSIC_SHUFFEL, embedBuilder.build()).setEphemeral(true).queue();
        }
    }
}
