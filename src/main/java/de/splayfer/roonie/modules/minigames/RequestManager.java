package de.splayfer.roonie.modules.minigames;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.utils.DefaultMessage;
import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.PrivateChannel;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequestManager extends ListenerAdapter {

    MongoDBDatabase mongoDB = new MongoDBDatabase("minigames");

    public static void sendGameRequest(User user, String id) {

        PrivateChannel dm = user.openPrivateChannel().complete();
        TicTacToeGame game = TicTacToeGame.getFromMongoDB(Roonie.mainGuild.getThreadChannelById(id));

        EmbedBuilder mainEmbed = new EmbedBuilder();
        mainEmbed.setColor(0x28346d);
        mainEmbed.setTitle(":crossed_swords: Du wurdest herausgefordert!");
        mainEmbed.setDescription("> Auf SPLΛYFUNITY wurdest du zu einem Minigame herausgefordert!");
        mainEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");


        mainEmbed.addField("Details", ":busts_in_silhouette:│Nutzer: " + game.getPlayer1().getAsMention() + "\n" +
                ":game_die:│Game: " + "TicTacToe", false);


        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.primary("join." + id, "Nimm die Herausforderung an!").withEmoji(Emoji.fromFormatted("⚔")));

        dm.sendTyping().queue();
        dm.sendMessageEmbeds(Embeds.BANNER_MINIGAME, mainEmbed.build()).setActionRow(buttons).queue();

    }

    public static void setWaitingStatus(ThreadChannel channel) {
        List<Message> messages = channel.getHistory().retrievePast(100).complete();
        channel.deleteMessages(messages).queue();

        EmbedBuilder mainEmbed = new EmbedBuilder();
        mainEmbed.setColor(0x28346d);
        mainEmbed.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/937417320157020261/231b.png");
        mainEmbed.setTitle("<a:loading:877158828465090570> Warten auf Mitspieler");
        mainEmbed.setDescription("> Nun wird darauf gewartet, dass der Mitspieler, den du herausgefordert hast die Anfrage annimmt. In dieser Zeit kannst du dich mit den Spielregeln vertraut machen!");
        mainEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");

        List<Button> buttons = new ArrayList<>();
        buttons.add(Button.secondary("minigames.tutorial", "Lies dir die Spielregeln durch!").withEmoji(Emoji.fromCustom("text", Long.parseLong("886623802954498069"), false)));
        buttons.add(Button.danger("minigames.cancel", "Spiel abbrechen").withEmoji(Emoji.fromCustom("cross", 880711722288169032L, true)));

        channel.sendMessageEmbeds(Embeds.BANNER_WAITING, mainEmbed.build()).setActionRow(buttons).queue();

    }

    public void onButtonInteraction (ButtonInteractionEvent event) {
        if (event.getButton().getId().startsWith("minigames.")) {
            TicTacToeGame game = TicTacToeGame.getFromMongoDB(event.getChannel().asThreadChannel());
            switch (event.getButton().getId().split("\\.")[1]) {
                case "tutorial":
                    break;
                case "cancel":
                    if (Objects.equals(game.getStatus(), "waiting")) {
                        event.getChannel().delete().queue();
                        game.removeFromMongoDB();
                    } else
                        event.replyEmbeds(DefaultMessage.error("Game bereits gestartet", "Es scheint als ist das Game bereits gestartet! Du kannst nur Runden im Wartemodus vorzeitig beenden!")).setEphemeral(true).queue();
                    break;
            }
        } else if (event.getButton().getId().startsWith("join.")) {
            TicTacToeGame game = TicTacToeGame.getFromMongoDB(event.getChannel().asThreadChannel());
            long id = Long.parseLong(event.getButton().getId().substring(5));
            boolean check = false;
            for (Document document : mongoDB.findAll("tictactoe"))
                if (document.getLong("channel").equals(id))
                    check = true;
            if (check) {
                if (game.getPlayer2().equals(event.getMember())) {
                    if (game.getStatus().equals("waiting")) {
                        //get guild
                        Guild guild = null;
                        for (Guild g : Roonie.shardMan.getGuilds()) {
                            for (ThreadChannel threadChannel : g.getThreadChannels()) {
                                if (threadChannel.getId().equals(id)) {
                                    guild = g;
                                }
                            }
                        }
                        game.getChannel().addThreadMember(game.getPlayer2()).queue();
                            TicTacToe.startGame(game.getPlayer1(), game.getPlayer2(), game.getChannel());

                        game.setStatus("playing");
                        game.insertToMongoDB();
                        event.replyEmbeds(DefaultMessage.success("Einladung erfolgreich angenommen!", "Du hast die Einladung erfolgreich angenommen und kannst nun dem Game beitreten. Klicke dazu einfach auf den Button unter dieser Nachricht!")).setEphemeral(true).addActionRow(Button.secondary("link", "Trete dem Game jetzt bei!").withUrl("https://discord.com/channels/" + guild.getId() + "/" + id).withEmoji(Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))).queue();
                    } else {
                        //get guild
                        Guild guild = null;
                        for (Guild g : Roonie.shardMan.getGuilds())
                            for (ThreadChannel threadChannel : g.getThreadChannels())
                                if (threadChannel.getId().equals(id))
                                    guild = g;
                        event.replyEmbeds(DefaultMessage.error("Herausforderung bereits angenommen!", "Du hast die folgende Herausforderung bereits angenommen. Über den Button unter dieser Nachricht kannst du dem Game beitreten!")).setEphemeral(true).addActionRow(Button.secondary("link", "Trete dem Game jetzt bei!").withUrl("https://discord.com/channels/" + guild.getId() + "/" + id).withEmoji(Emoji.fromCustom("text", Long.parseLong("877158818088386580"), false))).queue();
                    }
                } else
                    event.replyEmbeds(DefaultMessage.error("Anfrage zurückgezogen", "Der Nutzer hat die Herausforderung zurückgezogen.")).setEphemeral(true).queue();

            } else
                event.replyEmbeds(DefaultMessage.error("Game bereits gestartet", "Das Game zu dem du eingeladen wurdest, ist inzwischen leider schon beendet!")).setEphemeral(true).queue();
        }
    }
}