package de.splayfer.roonie.minigames;

import de.splayfer.roonie.FileSystem;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

public class DeleteListener extends ListenerAdapter {

    YamlConfiguration yml = YamlConfiguration.loadConfiguration(FileSystem.EmbedStats);

    public void onMessageReceived (MessageReceivedEvent event) {

        if (event.getAuthor().getId().equals("886209763178844212")) {

            if (event.getChannel().getId().equals(yml.getString("channel"))) {

                if (event.getMessage().getContentRaw().equals("\uD83C\uDFB2│game")) {

                    event.getMessage().delete().queue();

                }

            }

        }
    }

}
