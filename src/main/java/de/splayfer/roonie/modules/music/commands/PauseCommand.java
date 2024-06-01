package de.splayfer.roonie.modules.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.modules.music.MusicController;
import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class PauseCommand extends ListenerAdapter {

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("pause")) {
            MusicController controller = Roonie.playerManager.getController(event.getGuild().getIdLong());
            AudioManager manager = event.getGuild().getAudioManager();
            AudioPlayer player = controller.getPlayer();
            player.setPaused(true);
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Embeds.BANNER_MUSIC_PAUSE.getColor());
            embedBuilder.setTitle("**:pause_button: PAUSIERT**");
            embedBuilder.setDescription("> Der Audioplayer ist nun pausiert");
            embedBuilder.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
            event.replyEmbeds(Embeds.BANNER_MUSIC_PAUSE, embedBuilder.build()).setEphemeral(true).queue();
        }
    }

}
