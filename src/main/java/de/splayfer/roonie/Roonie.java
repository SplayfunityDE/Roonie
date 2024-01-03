package de.splayfer.roonie;

import de.splayfer.roonie.config.ConfigManager;
import de.splayfer.roonie.modules.economy.EconomyManager;
import de.splayfer.roonie.modules.management.commands.AutoDeleteListener;
import de.splayfer.roonie.modules.management.commands.CommandInfoListener;
import de.splayfer.roonie.config.SetupCommand;
import de.splayfer.roonie.general.AutoComplete;
import de.splayfer.roonie.general.AutoRoleListener;
import de.splayfer.roonie.general.WelcomeListener;
import de.splayfer.roonie.modules.giveaway.GiveawayManager;
import de.splayfer.roonie.modules.level.*;
import de.splayfer.roonie.modules.library.*;
import de.splayfer.roonie.modules.minigames.*;
import de.splayfer.roonie.modules.library.NitrogamesSetupCommand;
import de.splayfer.roonie.modules.poll.PollManager;
import de.splayfer.roonie.modules.response.ResponseManager;
import de.splayfer.roonie.general.schedule.BannerCounter;
import de.splayfer.roonie.general.schedule.BotCounter;
import de.splayfer.roonie.general.schedule.MessageCounter;
import de.splayfer.roonie.modules.tempchannel.*;
import de.splayfer.roonie.modules.ticket.TicketManager;
import de.splayfer.roonie.modules.ticket.TicketRestoreListener;
import de.splayfer.roonie.utils.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.io.IOException;
import java.util.EnumSet;

public class Roonie {

    public static Guild mainGuild;
    public static Guild emojiServerGuild;
    public static Guild emojiServerGuild2;
    public static Role[] autoRoles;
    public static JDABuilder builder;
    public static JDA shardMan;

    public static final long BOTID = 886209763178844212L;

    public static void main(String[] args) throws IOException, InterruptedException {

        builder = JDABuilder.createDefault("ODg2MjA5NzYzMTc4ODQ0MjEy.G6Z85Z.s24gB6nFpVOPQkyvJaAxiVtTDRWVzoGtBjucgE")
                .setActivity(Activity.streaming("auf 🌀SPLΛYFUNITY🌀", "https://twitch.tv/splayfer"))
                .setStatus(OnlineStatus.ONLINE)
                .setChunkingFilter(ChunkingFilter.ALL)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .enableCache(EnumSet.of(CacheFlag.ONLINE_STATUS, CacheFlag.CLIENT_STATUS, CacheFlag.EMOJI, CacheFlag.VOICE_STATE));

        EconomyManager.init();
        GiveawayManager.init();
        TempchannelManager.init();
        LibraryManager.init();
        PollManager.init();
        MinigamesManager.init();
        LevelManager.init();
        ResponseManager.init();
        TicketManager.init();
        ConfigManager.init();

        //register events
        builder.addEventListeners(new ReadyEventClass());

        //general
        builder.addEventListeners(new AutoRoleListener());
        builder.addEventListeners(new WelcomeListener());
        builder.addEventListeners(new AutoComplete());

        //commands
        builder.addEventListeners(new AutoDeleteListener());
        builder.addEventListeners(new CommandInfoListener());
        builder.addEventListeners(new de.splayfer.roonie.modules.management.commands.SetupCommand());

        shardMan = builder.build();
        CommandManager.initCommands(shardMan.awaitReady());
        MessageCounter.chatCounterUpdate();
        BotCounter.botCounterUpdate();
        BannerCounter.updateBannerMemberCount();
        AutoRoleListener.fixMemberRoles();
        AutoDeleteListener.checkCommandMessages();
        System.out.println("[Splayfer] Bot changed Status: Online");
        System.out.println("test");
    }
}

class ReadyEventClass extends ListenerAdapter {

    @Override
    public void onReady(ReadyEvent event) {
        Roonie.mainGuild = event.getJDA().getGuildById("873506353551925308");
        Roonie.emojiServerGuild = event.getJDA().getGuildById("877158057988202496");
        Roonie.emojiServerGuild2 = event.getJDA().getGuildById("879786460667052083");
        Roonie.autoRoles = new Role[]{
                Roonie.mainGuild.getRoleById("891292965241233448"), //member-role
        };
        Roonie.mainGuild.updateCommands().addCommands(Commands.slash("letsjohannes", "Hmm :eyes:")).queue();
        TicketRestoreListener.restoreTickets();
    }
}