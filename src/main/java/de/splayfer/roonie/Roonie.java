package de.splayfer.roonie;

import de.splayfer.roonie.commands.AutoDeleteListener;
import de.splayfer.roonie.commands.CommandInfoListener;
import de.splayfer.roonie.config.SetupCommand;
import de.splayfer.roonie.economy.CoinBomb;
import de.splayfer.roonie.economy.DailyCommand;
import de.splayfer.roonie.economy.MoneyCommand;
import de.splayfer.roonie.general.AutoComplete;
import de.splayfer.roonie.general.AutoRoleListener;
import de.splayfer.roonie.general.WelcomeListener;
import de.splayfer.roonie.giveaway.GiveawayCreateCommand;
import de.splayfer.roonie.giveaway.GiveawayEnterListener;
import de.splayfer.roonie.giveaway.GiveawayManager;
import de.splayfer.roonie.level.*;
import de.splayfer.roonie.library.*;
import de.splayfer.roonie.minigames.DeleteListener;
import de.splayfer.roonie.minigames.GameSelector;
import de.splayfer.roonie.minigames.RequestManager;
import de.splayfer.roonie.minigames.TicTacToe;
import de.splayfer.roonie.nitrogames.NitroGamesListener;
import de.splayfer.roonie.nitrogames.NitrogamesSetupCommand;
import de.splayfer.roonie.partner.PartnerSetupCommand;
import de.splayfer.roonie.partner.PartnerUnlockListener;
import de.splayfer.roonie.poll.PollCreateCommand;
import de.splayfer.roonie.poll.PollEnterListener;
import de.splayfer.roonie.response.ResponseAddCommand;
import de.splayfer.roonie.response.ResponseListener;
import de.splayfer.roonie.response.ResponseRemoveCommand;
import de.splayfer.roonie.schedule.BannerCounter;
import de.splayfer.roonie.schedule.BotCounter;
import de.splayfer.roonie.schedule.MessageCounter;
import de.splayfer.roonie.tempchannel.ChannelListener;
import de.splayfer.roonie.tempchannel.ControlListener;
import de.splayfer.roonie.tempchannel.CreateJoinHubCommand;
import de.splayfer.roonie.tempchannel.RemoveJoinHubCommand;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EnumSet;

public class Roonie {

    public static JDA shardMan;
    public static Guild mainGuild;
    public static Guild emojiServerGuild;
    public static Guild emojiServerGuild2;
    public static Role[] autoRoles;

    public static void main(String[] args) throws IOException {
        FileSystem.loadFileSystem();

        JDABuilder builder = JDABuilder.createDefault("ODg2MjA5NzYzMTc4ODQ0MjEy.G6jBkR.Wr_hOGdDVLscXvI1hfvo1nks9bedkcSDA87guw");
        builder.setActivity(Activity.streaming("auf 🌀SPLΛYFUNITY🌀", "https://twitch.tv/splayfer"));
        builder.setStatus(OnlineStatus.ONLINE);
        builder.setChunkingFilter(ChunkingFilter.ALL);
        builder.setMemberCachePolicy(MemberCachePolicy.ALL);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.DIRECT_MESSAGE_TYPING, GatewayIntent.DIRECT_MESSAGES, GatewayIntent.MESSAGE_CONTENT);
        EnumSet<CacheFlag> enumSet = EnumSet.of(CacheFlag.ONLINE_STATUS, CacheFlag.CLIENT_STATUS, CacheFlag.EMOJI, CacheFlag.VOICE_STATE);
        builder.enableCache(enumSet);
        //register events
        builder.addEventListeners(new ReadyEventClass());
        //tempchannel
        builder.addEventListeners(new CreateJoinHubCommand());
        builder.addEventListeners(new RemoveJoinHubCommand());
        builder.addEventListeners(new ChannelListener());
        builder.addEventListeners(new ControlListener());

        //profil
        builder.addEventListeners(new NitrogamesSetupCommand());

        //general
        builder.addEventListeners(new AutoRoleListener());
        builder.addEventListeners(new WelcomeListener());
        builder.addEventListeners(new AutoComplete());

        //library
        builder.addEventListeners(new LibrarySetupCommand());
        builder.addEventListeners(new BannerListener());
        builder.addEventListeners(new AddBannerCommand());
        builder.addEventListeners(new RemoveBannerCommand());
        builder.addEventListeners(new TemplateListener());
        builder.addEventListeners(new AddTemplateCommand());
        builder.addEventListeners(new RemoveTemplateCommand());

        //partner
        builder.addEventListeners(new PartnerSetupCommand());
        builder.addEventListeners(new PartnerUnlockListener());

        //autoresponse
        builder.addEventListeners(new ResponseAddCommand());
        builder.addEventListeners(new ResponseRemoveCommand());
        builder.addEventListeners(new ResponseListener());

        //levelsystem
        builder.addEventListeners(new LevelListener());
        builder.addEventListeners(new LevelInfoCommand());
        builder.addEventListeners(new RankCommand());
        builder.addEventListeners(new LevelCommand());
        builder.addEventListeners(new XpCommand());

        //umfragen
        builder.addEventListeners(new PollCreateCommand());
        builder.addEventListeners(new PollEnterListener());

        //giveaways
        builder.addEventListeners(new GiveawayCreateCommand());
        builder.addEventListeners(new GiveawayEnterListener());

        //nitrogames
        builder.addEventListeners(new NitrogamesSetupCommand());
        builder.addEventListeners(new NitroGamesListener());

        //economy
        builder.addEventListeners(new CoinBomb());
        builder.addEventListeners(new MoneyCommand());
        builder.addEventListeners(new DailyCommand());

        //minigames
        builder.addEventListeners(new DeleteListener());
        builder.addEventListeners(new GameSelector());
        builder.addEventListeners(new RequestManager());
        builder.addEventListeners(new SetupCommand());
        builder.addEventListeners(new TicTacToe());

        //commands
        builder.addEventListeners(new AutoDeleteListener());
        builder.addEventListeners(new CommandInfoListener());
        builder.addEventListeners(new de.splayfer.roonie.commands.SetupCommand());

        //config
        builder.addEventListeners(new SetupCommand());

        shardMan = builder.build();

        MessageCounter.chatCounterUpdate();
        BotCounter.botCounterUpdate();
        BannerCounter.updateBannerMemberCount();
        AutoRoleListener.fixMemberRoles();
        AutoDeleteListener.checkCommandMessages();
        GiveawayManager.checkGiveaways();
        LevelListener.checkVoiceMembers();

        System.out.println("[Splayfer] Bot changed Status: Online");
    }

    public void shutdown() {
        new Thread(() -> {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                while((line = reader.readLine()) != null) {
                    if(line.equalsIgnoreCase("exit")) {
                        if(shardMan != null) {
                            shardMan.shutdown();
                            System.out.println("Bot offline.");
                        }
                        reader.close();
                        break;
                    }
                    else if(line.equalsIgnoreCase("info"))
                        for(Guild guild : shardMan.getGuilds())
                            System.out.println(guild.getName() + " " + guild.getIdLong());
                    else {
                        System.out.println("Use 'exit' to shutdown.");
                    }
                }
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }).start();
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
                Commands.slash("rank", "\uD83D\uDCCB │ Zeigt dir deinen aktuellen Rank an!")
                        .addOption(OptionType.USER, "nutzer", "Wähle einen bestimmten Nutzer!", false),
                Commands.slash("levels", "✨ │ Schau dir unsere Level-Vorteile an!"),
                Commands.slash("level", "⚙ │ Verwalte die Level eines Nutzers!")
                        .addSubcommands(new SubcommandData("add", "➕ │ Füge dem Nutzer eine bestimmte Anzahl von Leveln hinzu!")
                                .addOption(OptionType.USER, "nutzer", "\uD83D\uDC65 │ Nutzer, dessen Level du verwalten möchtest!", true)
                                .addOption(OptionType.INTEGER, "anzahl", "\uD83D\uDCD1 │ Anzahl der zu verwaltenden Level!", true),
                                new SubcommandData("remove", "➖ │ Entferne dem Nutzer eine bestimmte Anzahl von Leveln!")
                                        .addOption(OptionType.USER, "nutzer", "\uD83D\uDC65 │ Nutzer, dessen Level du verwalten möchtest!", true)
                                        .addOption(OptionType.INTEGER, "anzahl", "\uD83D\uDCD1 │ Anzahl der zu verwaltenden Level!", true),
                                new SubcommandData("set", "\uD83D\uDCC3 │ Setze dem Nutzer die Anzahl seiner Level!")
                                        .addOption(OptionType.USER, "nutzer", "\uD83D\uDC65 │ Nutzer, dessen Level du verwalten möchtest!", true)
                                        .addOption(OptionType.INTEGER, "anzahl", "\uD83D\uDCD1 │ Anzahl der zu verwaltenden Level!", true))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("xp", "⚙ │ Verwalte die Xp eines Nutzers!")
                        .addSubcommands(new SubcommandData("add", "➕ │ Füge dem Nutzer eine bestimmte Anzahl an Xp hinzu!")
                                .addOption(OptionType.USER, "nutzer", "\uD83D\uDC65 │ Nutzer, dessen Xp du verwalten möchtest!", true)
                                .addOption(OptionType.INTEGER, "anzahl", "\uD83D\uDCD1 │ Anzahl der zu verwaltenden Xp!", true),
                                new SubcommandData("remove", "➖ │ Entferne dem Nutzer eine bestimmte Anzahl an Xp!")
                                        .addOption(OptionType.USER, "nutzer", "\uD83D\uDC65 │ Nutzer, dessen Xp du verwalten möchtest!", true)
                                        .addOption(OptionType.INTEGER, "anzahl", "\uD83D\uDCD1 │ Anzahl der zu verwaltenden Xp!", true),
                                new SubcommandData("set", "\uD83D\uDCC3 │ Setze dem Nutzer die Anzahl seiner Xp!")
                                        .addOption(OptionType.USER, "nutzer", "\uD83D\uDC65 │ Nutzer, dessen Xp du verwalten möchtest!", true)
                                        .addOption(OptionType.INTEGER, "anzahl", "\uD83D\uDCD1 │ Anzahl der zu verwaltenden Xp!", true))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("response", "\uD83D\uDCDC │ Verwalte automatisierte Reaktionen des Bots!")
                        .addSubcommands(new SubcommandData("add", "\uD83D\uDCDC │ Füge automatisierte Reaktionen hinzu!")
                                .addOption(OptionType.STRING, "nachricht", "\uD83D\uDCE2 │ Nachricht, auf die der Bot reagieren soll", true),
                                new SubcommandData("remove", "\uD83D\uDCDC │ Entferne automatisierte Reaktionen!")
                                        .addOption(OptionType.STRING, "nachricht", "\uD83D\uDCE2 │ Nachricht, auf die der Bot reagieren soll", true))
                                .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("money", "\uD83D\uDCB3 │ Zeigt dir den aktuellen Kontostand an!")
                        .addOption(OptionType.USER, "nutzer", "\uD83D\uDC65 │ Nutzer, welchen du anzeigen möchtest!", false),
                Commands.slash("leaderboard", "\uD83D\uDCCA │ Zeigt dir die Rangliste mit den aktuell besten Casino Spielern an!"),
                Commands.slash("daily", "\uD83D\uDCC5 │ Hole dir deine tägliche Menge an Coins!"),
                Commands.slash("giveaway", "\uD83C\uDF89 │ Verwalte die gesamten Giveaways des Servers!")
                        .addSubcommands(new SubcommandData("create", "➕ │ Erstelle ein neues Giveaway!"))
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("setup", "\uD83D\uDEE0️ │ Sende verwaltungsrelevante Nachrichten mithilfe dieses Commands")
                        .addOption(OptionType.STRING, "kategorie", "\uD83C\uDFF7️ │ Kategorie, über welche du Nachrichten versenden möchtest!", true, true)
                        .addOption(OptionType.INTEGER, "id", "⚙️ │ Id, der von dir verwendeten Kategorie (muss eine Ganzzahl sein)", false)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)),
                Commands.slash("letsjohannes", "Hmm :eyes:"),
                Commands.slash("umfrage", "\uD83D\uDCCA │ Verwaltet die gesamten Umfragen des Servers!")
                        .addSubcommands(new SubcommandData("create", "➕ │ Erstelle eine neue Umfrage!"))
        ).queue();

    }

}
