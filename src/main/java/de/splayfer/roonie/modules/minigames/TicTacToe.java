package de.splayfer.roonie.modules.minigames;

import de.splayfer.roonie.MongoDBDatabase;
import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class TicTacToe extends ListenerAdapter {

    protected static int crossAbstand = 182;
    protected static int circleAbstand = 179;
    static MongoDBDatabase mongoDB = MongoDBDatabase.getDatabase("minigames");

    public void onStringSelectInteraction (StringSelectInteractionEvent event) {

        if (event.getSelectMenu().getId().equals("minigames.tictatoe")) {
            int number = Integer.parseInt(event.getValues().get(0));
            TicTacToeGame game = TicTacToeGame.getFromMongoDB(event.getChannel().asThreadChannel());
            if (!checkEnd(game)) {
                if (game.getMemberTurn().equals(event.getMember())) {
                    if (game.getFields().containsKey(number)) {
                        for (Message m : event.getChannel().getHistory().retrievePast(100).complete()) {
                            if (m.getAuthor().isBot()) {
                                if (!m.getAttachments().isEmpty()) {
                                    m.delete().queue();
                                }
                            }
                        }
                        setField(event.getMember(), number);
                        event.deferEdit().queue();
                        if (checkForWin(game)) {

                            Member winner = event.getMember();

                            MinigamesManager.addMatchToMember(game.getPlayer1(), "tictactoe");
                            MinigamesManager.addMatchToMember(game.getPlayer2(), "tictactoe");

                            MinigamesManager.addWinToMember(winner, "tictactoe");

                            EmbedBuilder mainEmbed = new EmbedBuilder();
                            mainEmbed.setColor(0xffcf55);
                            mainEmbed.setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/938694173685612594/2004881.png");
                            mainEmbed.setTitle("Das Game wurde beendet!");
                            mainEmbed.setDescription("Das Game wurde beendet, da " + winner.getEffectiveName() + " das Spiel gewonnen hat!");
                            mainEmbed.addField("Details", "Gewinner: " + winner.getAsMention() + "\n" +
                                    "Spielmodus: " + "TicTacToe", false);
                            mainEmbed.addField(":warning: Der Kanal wird in 30 Sekunden automatisch geschlossen", "", false);
                            mainEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/925055440436477982/auto_faqw.png");

                            event.getChannel().sendTyping().queue();
                            event.getChannel().sendMessageEmbeds(Embeds.BANNER_MINIGAME_ENDING, mainEmbed.build()).queue();
                            Timer t = new Timer();
                            t.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    game.removeFromMongoDB();
                                    event.getChannel().delete().queue();
                                }
                            }, 30000);
                        }
                    } else {
                        event.editSelectMenu(event.getSelectMenu()).queue();
                        event.getHook().sendMessage("> :no_entry_sign: Hier wurde bereits ein Symbol gesetzt!").setEphemeral(true).queue();
                    }
                } else {
                    event.editSelectMenu(event.getSelectMenu()).queue();
                    event.getHook().sendMessage("> :no_entry_sign: Du bist gerade nicht an der Reihe!").setEphemeral(true).queue();
                }
            } else {
                event.editSelectMenu(event.getSelectMenu()).queue();
                event.getHook().sendMessage("> :no_entry_sign: Das Game wurde bereits beendet!").setEphemeral(true).queue();
            }
        }
    }

    public static void startGame(Member player1, Member player2, ThreadChannel channel) {
        List<Message> messages = channel.getHistory().getRetrievedHistory();
        channel.purgeMessages(messages);
        File file = new File(System.getProperty("user.dir") + File.separator + "media" + File.separator + "tictactoe" + File.separator + "tictactoe_empty.png");

        channel.sendTyping().queue();
        channel.sendFiles(FileUpload.fromData(file)).setActionRow(StringSelectMenu.create("minigames.tictatoe")
                .setPlaceholder(player1.getEffectiveName() + " ist nun an der Reihe!")
                .addOption("Oben Links", "1" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Oben Mitte", "2" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Oben Rechts", "3" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Mitte Links", "4" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Mitte Mitte", "5" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Mitte Rechts", "6" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Unten Links", "7" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Unten Mitte", "8" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Unten Rechts", "9" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .build()).complete();
    }

    public String getSymbol(Member member) {
        TicTacToeGame game = TicTacToeGame.getFromMember(member);
        if (member.equals(game.getPlayer1())) {
            return "cross";
        } else if (member.equals(game.getPlayer2())) {
            return "circle";
        }
        return null;
    }

    public Message setField(Member member, Integer number) {

        TicTacToeGame game = TicTacToeGame.getFromMember(member);
        game.switchTurn();
        game.getFields().put(number, getSymbol(member));

        BufferedImage backgroundImage = null;
        BufferedImage cross = null;
        BufferedImage circle = null;

        try {
            backgroundImage = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "media" + File.separator + "tictactoe" + File.separator + "tictactoe_empty.png"));
            cross = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "media" + File.separator + "tictactoe" + File.separator + "tictactoe_cross.png"));
            circle = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "media" + File.separator + "tictactoe" + File.separator + "tictactoe_circle.png"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        Graphics g = backgroundImage.getGraphics();

        //stacking images
        for (int i : game.getFields().keySet()) {
            BufferedImage currentImage = null;
            int currenAbstand = 0;
            if (game.getFields().get(i).equals("cross")) {
                currentImage = cross;
                currenAbstand = crossAbstand;
            } else if (game.getFields().get(i).equals("circle")) {
                currentImage = circle;
                currenAbstand = circleAbstand;
            }

            switch (i) {
                case 1:
                    g.drawImage(currentImage, 10, 10, null);
                    break;
                case 2:
                    g.drawImage(currentImage, 10 + currenAbstand, 10, null);
                    break;
                case 3:
                    g.drawImage(currentImage, 10 + (2 * currenAbstand), 10, null);
                    break;
                case 4:
                    g.drawImage(currentImage, 10, 10 + currenAbstand, null);
                    break;
                case 5:
                    g.drawImage(currentImage, 10 + currenAbstand, 10 + currenAbstand, null);
                    break;
                case 6:
                    g.drawImage(currentImage, 10 + (2 * currenAbstand), 10 + currenAbstand, null);
                    break;
                case 7:
                    g.drawImage(currentImage, 10, 10 + (2 * currenAbstand), null);
                    break;
                case 8:
                    g.drawImage(currentImage, 10 + currenAbstand, 10 + (2 * currenAbstand), null);
                    break;
                case 9:
                    g.drawImage(currentImage, 10 + (2 * currenAbstand), 10 + (2 * currenAbstand), null);
                    break;
            }
        }
        g.dispose();

        try {
            ImageIO.write(backgroundImage, "png", new File(System.getProperty("user.dir") + File.separator + "media" + File.separator + "cache" + File.separator + "tictactoe.png"));
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        File tempFile = new File(System.getProperty("user.dir") + File.separator + "media" + File.separator + "cache" + File.separator + "tictactoe.png");
        ThreadChannel channel = game.getChannel();

        Member current = game.getMemberTurn();
        Message message = channel.sendFiles(FileUpload.fromData(tempFile)).setActionRow(StringSelectMenu.create("minigames.tictatoe")
                .setPlaceholder(current.getEffectiveName() + " ist nun an der Reihe!")
                .addOption("Oben Links", "1" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Oben Mitte", "2" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Oben Rechts", "3" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Mitte Links", "4" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Mitte Mitte", "5" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Mitte Rechts", "6" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Unten Links", "7" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Unten Mitte", "8" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .addOption("Unten Rechts", "9" , "", Emoji.fromCustom("tictactoe", 937046989458247692L, false))
                .build()).complete();

        tempFile.delete();

        //save to yml

        String symbol = getSymbol(member);
        game.getFields().put(number, symbol);
        game.insertToMongoDB();
        return message;
    }

    public boolean checkForWin(TicTacToeGame game) {
        boolean checker = false;
        List<String> fields = List.of("", "", "", "", "", "", "", "", "");
        for (int num : game.getFields().keySet())
            fields.set(num, game.getFields().get(num));

        //check combinations
        if (fields.get(0).equals(fields.get(1)) && fields.get(0).equals(fields.get(2)) && !Objects.equals(fields.get(0), "")) {
            checker = true;
        } else if (fields.get(4).equals(fields.get(3)) && fields.get(4).equals(fields.get(5)) && !Objects.equals(fields.get(4), "")) {
            checker = true;
        } else if (fields.get(7).equals(fields.get(6)) && fields.get(7).equals(fields.get(8)) && !Objects.equals(fields.get(7), "")) {
            checker = true;
        } else if (fields.get(3).equals(fields.get(0)) && fields.get(3).equals(fields.get(6)) && !Objects.equals(fields.get(3), "")) {
            checker = true;
        } else if (fields.get(5).equals(fields.get(2)) && fields.get(5).equals(fields.get(8)) && !Objects.equals(fields.get(5), "")) {
            checker = true;
        } else if (fields.get(4).equals(fields.get(1)) && fields.get(4).equals(fields.get(7)) && !Objects.equals(fields.get(4), "")) {
            checker = true;
        } else if (fields.get(4).equals(fields.get(0)) && fields.get(4).equals(fields.get(8)) && !Objects.equals(fields.get(4), "")) {
            checker = true;
        } else if (fields.get(4).equals(fields.get(2)) && fields.get(4).equals(fields.get(6)) && !Objects.equals(fields.get(4), "")) {
            checker = true;
        }

        if (checker) {
            game.setStatus("ending");
            game.insertToMongoDB();
        }
        return checker;
    }

    public boolean checkEnd(TicTacToeGame game) {
        return game.getStatus().equals("ending");
    }
}