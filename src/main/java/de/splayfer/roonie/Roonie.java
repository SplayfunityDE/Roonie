package de.splayfer.roonie;

import club.minnced.discord.jdave.interop.JDaveSessionFactory;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import de.splayfer.roonie.utils.Properties;
import de.splayfer.roonie.utils.SlashCommandManager;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.audio.AudioModuleConfig;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Roonie {

    @Setter
    @Getter
    private final List<SlashCommandManager> slashCommandManagers;
    private final List<ListenerAdapter> listeners;
    private final Properties properties;
    @Getter
    private AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
    @Setter
    @Getter
    private Guild mainGuild;
    @Setter
    @Getter
    private Guild emojiServerGuild;
    @Setter
    @Getter
    private Guild emojiServerGuild2;
    @Setter
    @Getter
    private Role[] autoRoles;
    private JDABuilder builder;
    @Setter
    @Getter
    private JDA shardMan;

    @Autowired
    public Roonie(List<ListenerAdapter> listeners, Properties properties, List<SlashCommandManager> slashCommandManagers) {
        this.listeners = listeners;
        this.properties = properties;
        this.slashCommandManagers = slashCommandManagers;
    }

    @PostConstruct
    public void startJDA() throws IOException, InterruptedException {

        builder = JDABuilder.createDefault(properties.getToken())
                .setActivity(Activity.streaming("🌀SPLΛYFUNITY🌀", "https://twitch.tv/splayfer"))
                .setStatus(OnlineStatus.ONLINE)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .enableCache(EnumSet.of(CacheFlag.ONLINE_STATUS, CacheFlag.CLIENT_STATUS, CacheFlag.EMOJI, CacheFlag.VOICE_STATE))
                .setAudioModuleConfig(new AudioModuleConfig().withDaveSessionFactory(new JDaveSessionFactory()));

        audioPlayerManager.registerSourceManager(new YoutubeAudioSourceManager());
        AudioSourceManagers.registerRemoteSources(audioPlayerManager, YoutubeAudioSourceManager.class);

        //register events
        for (ListenerAdapter listener : listeners) {
            builder.addEventListeners(listener);
        }

        shardMan = builder.build();

        //init slash commands
        shardMan.awaitReady().updateCommands().addCommands(
                slashCommandManagers.stream()
                        .flatMap(manager -> Arrays.stream(manager.slashCommands()))
                        .collect(Collectors.toList())
        ).queue();

        System.out.println("[Splayfer] Bot changed Status: Online");
    }
}

@Component
@RequiredArgsConstructor
class ReadyEventClass extends ListenerAdapter implements SlashCommandManager {

    private final Roonie roonie;
    private final Properties properties;

    @Override
    public void onReady(ReadyEvent event) {

        roonie.setMainGuild(event.getJDA().getGuildById(properties.getMainGuild()));
        roonie.setEmojiServerGuild(event.getJDA().getGuildById(properties.getEmojiServerGuild()));
        roonie.setEmojiServerGuild2(event.getJDA().getGuildById(properties.getEmojiServerGuild2()));

        roonie.setAutoRoles(
                Arrays.stream(properties.getAutoroles()).map(role -> roonie.getMainGuild().getRoleById(role)).toArray(Role[]::new) //member-role
        );
    }

    @Override
    public SlashCommandData[] slashCommands() {
        return new SlashCommandData[]{
                Commands.slash("letsjohannes", "Hmm :eyes:")
        };
    }
}