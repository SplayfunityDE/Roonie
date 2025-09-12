
package de.splayfer.roonie.modules.tempchannel;

import de.splayfer.roonie.utils.DefaultMessage;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JoinHubCommand extends ListenerAdapter {

    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if (event.getName().equals("voicehub"))
            if (event.getOption("kanal").getAsChannel().getType().equals(ChannelType.VOICE)) {
                VoiceChannel vc = event.getOption("kanal").getAsChannel().asVoiceChannel();
                switch (event.getSubcommandName()) {
                    case "add":
                        if (!TempchannelManager.existesJoinHub(vc.getIdLong())) {
                            TempchannelManager.createJoinHub(vc, event.getMember());
                            event.replyEmbeds(DefaultMessage.success("VoiceHub hinzugefügt", "Ein VoiceHub mit den folgenden Details wurde erfolgreich hinzugefügt!", new MessageEmbed.Field("<:channel:1001082478804615238> Kanal", vc.getAsMention(), true))).setEphemeral(true).queue();
                        } else
                            event.replyEmbeds(DefaultMessage.error("VoiceHub existiert bereits", "Der von dir angegebene Kanal ist bereits ein Voicehub!", new MessageEmbed.Field("<:channel:1001082478804615238> Kanal", vc.getAsMention(), true))).setEphemeral(true).queue();
                        break;
                    case "remove":
                        if (TempchannelManager.existesJoinHub(vc.getIdLong())) {
                            TempchannelManager.removeJoinHub(vc);
                            event.replyEmbeds(DefaultMessage.success2("VoiceHub entfernt", "Ein VoiceHub mit den folgenden Details wurde erfolgreich entfernt!", new MessageEmbed.Field("<:channel:1001082478804615238> Kanal", vc.getAsMention(), true))).setEphemeral(true).queue();
                        } else
                            event.replyEmbeds(DefaultMessage.error("VoiceHub existiert nicht", "Der von dir angegebene Kanal ist noch kein ein Voicehub!", new MessageEmbed.Field("<:channel:1001082478804615238> Kanal", vc.getAsMention(), true))).setEphemeral(true).queue();
                        break;
                }
            } else
                event.replyEmbeds(DefaultMessage.error("Falscher Kanaltyp", "Du musst einen Sprachkanal angeben, ihn als VoiceHub verwalten zu können!")).setEphemeral(true).queue();
    }
}