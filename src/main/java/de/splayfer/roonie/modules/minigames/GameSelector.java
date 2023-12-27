package de.splayfer.roonie.modules.minigames;

import de.splayfer.roonie.utils.DefaultMessage;
import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class GameSelector extends ListenerAdapter {

    @Override
    public void onStringSelectInteraction (StringSelectInteractionEvent event) {

        if (event.getSelectMenu().getId().equals("minigames.topic")) {
            EmbedBuilder mainEmbed;
            switch (event.getValues().get(0)) {

                case "search":

                    mainEmbed = new EmbedBuilder();
                    mainEmbed.setColor(0x28346d);
                    mainEmbed.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/936636138255421520/basic1-030_dropdown-512_16234.png");
                    mainEmbed.setTitle("<:cursor:913499103919480903> Wähle deinen Spielmodus");
                    mainEmbed.setDescription("> Wähle im Menü aus, in welchem Spielmodus du deine Spielersuche starten möchtest!");
                    mainEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");

                    event.editSelectMenu(StringSelectMenu.create("minigames.topic")
                            .setRequiredRange(1, 1)
                            .setPlaceholder("Wähle eine Aktion aus!")
                            .addOption("Suche ein Match", "search", "Suche nach einem anderen Mitspieler", Emoji.fromFormatted("\uD83D\uDD0E"))
                            .addOption("Fordere einen Spieler heraus", "challenge", "Fordere einen bestimmten Nutzer heraus", Emoji.fromFormatted("⚔"))
                            .addOption("Statistiken", "stats", "Rufe deine Statistiken ab", Emoji.fromCustom("stats", Long.parseLong("937041708967927818"), false))
                            .build()).queue();

                    event.getHook().sendMessageEmbeds(Embeds.BANNER_MINIGAME, mainEmbed.build()).setEphemeral(true).addActionRow(StringSelectMenu.create("minigames.search")
                            .setRequiredRange(1, 1)
                            .setPlaceholder("Wähle deinen Spielmodus!")
                            .addOption("TicTacToe", "tictactoe", "Wer 3 Symbole in einer Reihe hat gewinnt", Emoji.fromCustom("tictactoe", Long.parseLong("937046989458247692"), false))
                            .build()).queue();

                    break;

                case "challenge":

                    mainEmbed = new EmbedBuilder();
                    mainEmbed.setColor(0x28346d);
                    mainEmbed.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/936636138255421520/basic1-030_dropdown-512_16234.png");
                    mainEmbed.setTitle("<:cursor:913499103919480903> Wähle deinen Spielmodus");
                    mainEmbed.setDescription("> Wähle im Menü aus, in welchem Spielmodus du jemanden herausfodern möchtest!");
                    mainEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");

                    event.editSelectMenu(StringSelectMenu.create("minigames.topic")
                            .setRequiredRange(1, 1)
                            .setPlaceholder("Wähle eine Aktion aus!")
                            .addOption("Suche ein Match", "search", "Suche nach einem anderen Mitspieler", Emoji.fromFormatted("\uD83D\uDD0E"))
                            .addOption("Fordere einen Spieler heraus", "challenge", "Fordere einen bestimmten Nutzer heraus", Emoji.fromFormatted("⚔"))
                            .addOption("Statistiken", "stats", "Rufe deine Statistiken ab", Emoji.fromCustom("stats", Long.parseLong("937041708967927818"), false))
                            .build()).queue();

                    event.getHook().sendMessageEmbeds(Embeds.BANNER_MINIGAME, mainEmbed.build()).setEphemeral(true).addActionRow(StringSelectMenu.create("minigames.challenge")
                            .setRequiredRange(1, 1)
                            .setPlaceholder("Wähle deinen Spielmodus!")
                            .addOption("TicTacToe", "tictactoe", "Wer 3 Symbole in einer Reihe hat gewinnt", Emoji.fromCustom("tictactoe", Long.parseLong("937046989458247692"), false))
                            .build()).queue();

                    break;

                case "stats":

                    mainEmbed = new EmbedBuilder();
                    mainEmbed.setColor(0x28346d);
                    mainEmbed.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/938705910438715432/doc.png");
                    mainEmbed.setTitle("Statistiken von " + event.getMember().getEffectiveName());
                    mainEmbed.setDescription("Hier findest du deine persönlichen Statistiken zu den SPLΛYFUNITY Minigames!");
                    mainEmbed.addField("<:tictactoe:937046989458247692> TicTacToe", "Gespielte Spiele: " + MinigamesManager.getMatches(event.getMember(), "tictactoe") + "\n" +
                            "Gewonnene Spiele: " + MinigamesManager.getWins(event.getMember(), "tictactoe"), false);
                    mainEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");

                    event.editSelectMenu(StringSelectMenu.create("minigames.topic")
                            .setRequiredRange(1, 1)
                            .setPlaceholder("Wähle eine Aktion aus!")
                            .addOption("Suche ein Match", "search", "Suche nach einem anderen Mitspieler", Emoji.fromFormatted("\uD83D\uDD0E"))
                            .addOption("Fordere einen Spieler heraus", "challenge", "Fordere einen bestimmten Nutzer heraus", Emoji.fromFormatted("⚔"))
                            .addOption("Statistiken", "stats", "Rufe deine Statistiken ab", Emoji.fromCustom("stats", Long.parseLong("937041708967927818"), false))
                            .build()).queue();

                    event.getHook().sendMessageEmbeds(Embeds.BANNER_STATS, mainEmbed.build()).setEphemeral(true).queue();

                    break;

            }

        } else if (event.getSelectMenu().getId().equals("minigames.challenge")) {
            EmbedBuilder mainEmbed;
            switch (event.getValues().get(0)) {

                case "tictactoe":

                    ThreadChannel threadChannel = event.getChannel().asTextChannel().createThreadChannel("\uD83C\uDFB2│" + "game", false).complete();

                    event.replyEmbeds(DefaultMessage.success("Minigame erfolgreich geladen!", "Das Minigame wurde erfolgreich geladen und du kannst deinen Spieler nun einladen! Klicke auf den Button unter dieser Nachricht um das Game zu aktivieren")).setEphemeral(true).addActionRow(Button.secondary("link", "Tritt dem Game bei!").withUrl("https://discord.com/channels/" + event.getGuild().getId() + "/" + threadChannel.getId()).withEmoji(Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))).queue();

                    mainEmbed = new EmbedBuilder();
                    mainEmbed.setColor(0x28346d);
                    mainEmbed.setTitle(":busts_in_silhouette: Wähle deinen Mitspieler aus!");
                    mainEmbed.setDescription("> Erwähne den Nutzer oder gib seine ID an!");
                    mainEmbed.addField("Beispiel", "`@Splayfer` \n" +
                            "853618861294485534", false);

                    threadChannel.sendMessageEmbeds(mainEmbed.build()).queue();
                    threadChannel.addThreadMember(event.getMember()).queue();

                    //save to yml
                    TicTacToeGame.create(threadChannel, "request", event.getMember()).insertToMongoDB();
                    /*
                    yml.set(threadChannel.getId() + ".game", event.getValues().get(0));
                    yml.set(threadChannel.getId() + ".status", "waiting");
                    yml.set(threadChannel.getId() + ".type", "request");
                    yml.set(threadChannel.getId() + ".player1", event.getMember().getId());
                     */
                    break;

            }

        } else if (event.getSelectMenu().getId().equals("minigames.search")) {
            EmbedBuilder mainEmbed;
            if (Queue.checkForGame()) {

                //game found

                TicTacToeGame game = Queue.getQueueGame();

                event.replyEmbeds(DefaultMessage.success("Minigame erfolgreich geladen!", "Das Minigame wurde erfolgreich geladen und es wird nun auf einen Mitspieler gewartet! Klicke auf den Button unter dieser Nachricht um das Game zu aktivieren")).setEphemeral(true).addActionRow(Button.secondary("link", "Tritt dem Game bei!").withUrl("https://discord.com/channels/" + event.getGuild().getId() + "/" + game).withEmoji(Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))).queue();

                //save to yml
                game.setPlayer2(event.getMember());
                game.setStatus("playing");
                game.insertToMongoDB();
                /*
                yml.set(game + ".player2", event.getMember().getId());
                yml.set(game + ".status", "playing");

                 */

                Member player1 = game.getPlayer1();
                Member player2 = game.getPlayer2();
                ThreadChannel channel = game.getChannel();
                channel.addThreadMember(player2).queue();

                Timer t = new Timer();
                t.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        TicTacToe.startGame(player1, player2, channel);
                    }
                }, 5000);
            } else {

                //no game found

                switch (event.getValues().get(0)) {
                    case "tictactoe":
                        ThreadChannel threadChannel = event.getChannel().asTextChannel().createThreadChannel("\uD83C\uDFB2│" + "game", false).complete();

                        event.replyEmbeds(DefaultMessage.success("Minigame erfolgreich geladen!", "Das Minigame wurde erfolgreich geladen und es wird nun auf einen Mitspieler gewartet! Klicke auf den Button unter dieser Nachricht um das Game zu aktivieren")).setEphemeral(true).addActionRow(Button.secondary("link", "Tritt dem Game bei!").withUrl("https://discord.com/channels/" + event.getGuild().getId() + "/" + threadChannel.getId()).withEmoji(Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))).queue();

                        mainEmbed = new EmbedBuilder();
                        mainEmbed.setColor(0x28346d);
                        mainEmbed.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/937417320157020261/231b.png");
                        mainEmbed.setTitle("<a:loading:877158828465090570> Warten auf Mitspieler");
                        mainEmbed.setDescription("> Nun wird darauf gewartet, dass der Mitspieler, den du herausgefordert hast die Anfrage annimmt. In dieser Zeit kannst du dich mit den Spielregeln vertraut machen!");
                        mainEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");

                        List<Button> buttons = new ArrayList<>();
                        buttons.add(Button.secondary("minigames.tutorial", "Lies dir die Spielregeln durch!").withEmoji(Emoji.fromCustom("text", Long.parseLong("886623802954498069"), false)));

                        threadChannel.sendMessageEmbeds(Embeds.BANNER_WAITING, mainEmbed.build()).setActionRow(buttons).queue();
                        threadChannel.addThreadMember(event.getMember()).queue();
                        //save to yml

                        TicTacToeGame.create(threadChannel, "queue", event.getMember()).insertToMongoDB();
                        /*
                        yml.set(threadChannel.getId() + ".game", event.getValues().get(0));
                        yml.set(threadChannel.getId() + ".status", "waiting");
                        yml.set(threadChannel.getId() + ".type", "queue");
                        yml.set(threadChannel.getId() + ".player1", event.getMember().getId());

                         */
                        break;
                }
            }
        }
    }

    public void onMessageReceived (MessageReceivedEvent event) {
        if (TicTacToeGame.isGameChannel(event.getChannel())) {
            TicTacToeGame game = TicTacToeGame.getFromMongoDB(event.getChannel().asThreadChannel());
            if (game.getPlayer1() == event.getMember()) {
                if (game.getPlayer2() == null) {
                    String[] args = event.getMessage().getContentStripped().split(" ");
                    if (args.length == 1) {
                        //getting user
                        if (!event.getMessage().getMentions().getMembers().isEmpty()) {
                            if (event.getMessage().getMentions().getMembers().size() == 1) {
                                if (!event.getMessage().getMentions().getMembers().get(0).getUser().isBot()) {
                                    if (!event.getMessage().getMentions().getMembers().get(0).getId().equals(event.getAuthor().getId())) {
                                        if (TicTacToeGame.checkPlayer(event.getMessage().getMentions().getMembers().get(0))) {
                                            game.setPlayer2(event.getMessage().getMentions().getMembers().get(0));
                                            game.insertToMongoDB();
                                            RequestManager.sendGameRequest(event.getMessage().getMentions().getMembers().get(0).getUser(), event.getChannel().getId());
                                            RequestManager.setWaitingStatus(event.getChannel().asThreadChannel());
                                        } else
                                            event.getChannel().sendMessageEmbeds(DefaultMessage.error("Der Nutzer befindet sich bereits in einem Game!")).complete().delete().queueAfter(8, TimeUnit.SECONDS);
                                    } else
                                        event.getChannel().sendMessageEmbeds(DefaultMessage.error("Du kannst dich nicht selbst herausfordern!")).complete().delete().queueAfter(8, TimeUnit.SECONDS);
                                } else
                                    event.getChannel().sendMessageEmbeds(DefaultMessage.error("Du kannst keinen Bot herausfordern!")).complete().delete().queueAfter(8, TimeUnit.SECONDS);
                            } else
                                event.getChannel().sendMessageEmbeds(DefaultMessage.error("Du kannst nicht mehrere Nutzer gleichzeitig herausfordern!")).complete().delete().queueAfter(8, TimeUnit.SECONDS);
                        } else if (!event.getMessage().getMentions().getUsers().isEmpty()) {
                            if (event.getMessage().getMentions().getUsers().size() == 1) {
                                if (!event.getMessage().getMentions().getUsers().get(0).isBot()) {
                                    if (!event.getMessage().getMentions().getUsers().get(0).getId().equals(event.getAuthor().getId())) {
                                        if (TicTacToeGame.checkPlayer(event.getGuild().getMember(event.getMessage().getMentions().getUsers().get(0)))) {
                                            game.setPlayer2(event.getMember());
                                            game.insertToMongoDB();
                                            RequestManager.sendGameRequest(event.getMessage().getMentions().getUsers().get(0), event.getChannel().getId());
                                            RequestManager.setWaitingStatus(event.getChannel().asThreadChannel());
                                        } else
                                            event.getChannel().sendMessageEmbeds(DefaultMessage.error("Der Nutzer befindet sich bereits in einem Game!")).complete().delete().queueAfter(8, TimeUnit.SECONDS);
                                    } else
                                        event.getChannel().sendMessageEmbeds(DefaultMessage.error("Du kannst dich nicht selbst herausfordern!")).complete().delete().queueAfter(8, TimeUnit.SECONDS);
                                } else
                                    event.getChannel().sendMessageEmbeds(DefaultMessage.error("Du kannst keinen Bot herausfordern!")).complete().delete().queueAfter(8, TimeUnit.SECONDS);
                            } else
                                event.getChannel().sendMessageEmbeds(DefaultMessage.error("Du kannst nicht mehrere Nutzer gleichzeitig herausfordern!")).complete().delete().queueAfter(8, TimeUnit.SECONDS);
                        } else {
                            boolean checkID = false;
                            Member member = null;
                            try {
                                Double d = Double.parseDouble(args[0]);
                                member = event.getGuild().getMemberById(args[0]);
                                if (!member.getUser().isBot()) {
                                    if (!member.getId().equals(event.getAuthor().getId()))
                                        checkID = true;
                                } else
                                    event.getChannel().sendMessageEmbeds(DefaultMessage.error("Du kannst keinen Bot herausfordern!")).complete().delete().queueAfter(8, TimeUnit.SECONDS);
                            } catch (Exception exception) {
                                event.getChannel().sendMessageEmbeds(DefaultMessage.error("Bitte erwähne den Nutzer oder gib seine ID an!")).complete().delete().queueAfter(8, TimeUnit.SECONDS);
                            }
                            if (checkID) {
                                if (TicTacToeGame.checkPlayer(member)) {
                                    game.setPlayer2(member);
                                    game.insertToMongoDB();
                                    RequestManager.sendGameRequest(member.getUser(), event.getChannel().getId());
                                    RequestManager.setWaitingStatus(event.getChannel().asThreadChannel());
                                } else
                                    event.getChannel().sendMessageEmbeds(DefaultMessage.error("Der Nutzer befindet sich bereits in einem Game!")).complete().delete().queueAfter(8, TimeUnit.SECONDS);
                            }
                        }
                        event.getMessage().delete().queue();
                    }
                }
            }
        }
    }
}