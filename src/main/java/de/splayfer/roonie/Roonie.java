package de.splayfer.roonie;

import de.splayfer.roonie.modules.economy.EconomyManager;
import de.splayfer.roonie.modules.giveaway.GiveawayCreateCommand;
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
import de.splayfer.roonie.modules.library.nitrogames.NitrogamesSetupCommand;
import de.splayfer.roonie.modules.poll.PollManager;
import de.splayfer.roonie.modules.response.ResponseAddCommand;
import de.splayfer.roonie.modules.response.ResponseListener;
import de.splayfer.roonie.modules.response.ResponseManager;
import de.splayfer.roonie.modules.response.ResponseRemoveCommand;
import de.splayfer.roonie.general.schedule.BannerCounter;
import de.splayfer.roonie.general.schedule.BotCounter;
import de.splayfer.roonie.general.schedule.MessageCounter;
import de.splayfer.roonie.modules.tempchannel.*;
import de.splayfer.roonie.utils.CommandManager;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
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

    public static void main(String[] args) throws IOException, InterruptedException {
        FileSystem.loadFileSystem();

        builder = JDABuilder.createDefault("ODg2MjA5NzYzMTc4ODQ0MjEy.G6jBkR.Wr_hOGdDVLscXvI1hfvo1nks9bedkcSDA87guw");
        builder.setActivity(Activity.streaming("auf 🌀SPLΛYFUNITY🌀", "https://twitch.tv/splayfer"));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT);
        EnumSet<CacheFlag> enumSet = EnumSet.of(CacheFlag.ONLINE_STATUS, CacheFlag.CLIENT_STATUS, CacheFlag.EMOJI, CacheFlag.VOICE_STATE);
        builder.enableCache(enumSet);

        EconomyManager.init();
        GiveawayManager.init();
        TempchannelManager.init();
        LibraryManager.init();
        PollManager.init();
        MinigamesManager.init();
        LevelManager.init();
        ResponseManager.init();

        //register events
        builder.addEventListeners(new ReadyEventClass());

        //profil
        builder.addEventListeners(new NitrogamesSetupCommand());

        //general
        builder.addEventListeners(new AutoRoleListener());
        builder.addEventListeners(new WelcomeListener());
        builder.addEventListeners(new AutoComplete());

        //commands
        builder.addEventListeners(new AutoDeleteListener());
        builder.addEventListeners(new CommandInfoListener());
        builder.addEventListeners(new de.splayfer.roonie.modules.management.commands.SetupCommand());

        //config
        builder.addEventListeners(new SetupCommand());

        shardMan = builder.build();
        CommandManager.initCommands(shardMan.awaitReady());

        MessageCounter.chatCounterUpdate();
        BotCounter.botCounterUpdate();
        BannerCounter.updateBannerMemberCount();

        AutoRoleListener.fixMemberRoles();
        AutoDeleteListener.checkCommandMessages();

        System.out.println("[Splayfer] Bot changed Status: Online");
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

        Roonie.mainGuild.updateCommands().addCommands(
                Commands.slash("setup", "\uD83D\uDEE0️ │ Sende verwaltungsrelevante Nachrichten mithilfe dieses Commands")
                        .addOption(OptionType.STRING, "kategorie", "\uD83C\uDFF7️ │ Kategorie, über welche du Nachrichten versenden möchtest!", true, true)
                        .addOption(OptionType.INTEGER, "id", "⚙️ │ Id, der von dir verwendeten Kategorie (muss eine Ganzzahl sein)", false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("letsjohannes", "Hmm :eyes:")
        ).queue();
    }

}
