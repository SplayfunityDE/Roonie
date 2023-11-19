package de.splayfer.roonie.minigames;

import de.splayfer.roonie.FileSystem;
import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.ThreadChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.FileUpload;
import org.bspfsystems.yamlconfiguration.file.YamlConfiguration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TicTacToe extends ListenerAdapter {

    protected static HashMap<String, Long> timeoutList = new HashMap<>();

    protected static int crossAbstand = 182;
    protected static int circleAbstand = 179;

    protected static YamlConfiguration yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

    public void onStringSelectInteraction (StringSelectInteractionEvent event) {

        if (event.getSelectMenu().getId().equals("minigames.tictatoe")) {

            int number = Integer.parseInt(event.getValues().get(0));

            if (!checkEnd(event.getChannel().getId())) {

                if (checkTurn(event.getMember())) {

                    yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

                    if (!yml.contains(event.getChannel().getId() + ".fields" + "." + number)) {

                        for (Message m : event.getChannel().getHistory().retrievePast(100).complete()) {

                            if (m.getAuthor().isBot()) {

                                if (!m.getAttachments().isEmpty()) {

                                    m.delete().queue();

                                }

                            }

                        }

                        setField(event.getMember(), number);

                        event.deferEdit().queue();

                        if (checkForWin(event.getChannel().getId())) {

                            Member winner = event.getMember();

                            Member player1 = event.getGuild().getMemberById(yml.getString(event.getChannel().getId() + ".player1"));
                            Member player2 = event.getGuild().getMemberById(yml.getString(event.getChannel().getId() + ".player2"));

                            MinigamesManager.addMatchToMember(player1, "tictactoe");
                            MinigamesManager.addMatchToMember(player2, "tictactoe");

                            MinigamesManager.addWinToMember(winner, "tictactoe");

                            EmbedBuilder bannerEmbed = new EmbedBuilder();
                            bannerEmbed.setColor(0xffcf55);
                            bannerEmbed.setImage("https://cdn.discordapp.com/attachments/906251556637249547/938483513970262136/banner_winner.png");

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
                            event.getChannel().sendMessageEmbeds(bannerEmbed.build(), mainEmbed.build()).queue();

                            Timer t = new Timer();
                            t.schedule(new TimerTask() {
                                @Override
                                public void run() {

                                    yml.set(event.getChannel().getId(), null);

                                    try {
                                        yml.save(FileSystem.GameLog);
                                    } catch (IOException exception) {
                                        exception.printStackTrace();
                                    }

                                    event.getChannel().delete().queue();

                                    t.cancel();
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

        checkTimeout(channel.getId(), player1);

    }

    public String getSymbol(Member member) {

        yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

        String symbol = "";
        String gameID = getGameID(member);

        if (yml.get(gameID + ".player1").equals(member.getId())) {

            symbol = "cross";

        } else if (yml.get(gameID + ".player2").equals(member.getId())) {

            symbol = "circle";

        }

        return symbol;

    }

    public Member getPlayerBySymbol(String gameID, String symbol) {

        yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

        Member member;
        Guild guild = Roonie.shardMan.getThreadChannelById(gameID).getGuild();

        if (symbol.equals("cross")) {

            member =guild.getMemberById(yml.getString(gameID + ".player1"));

        } else {

            member = guild.getMemberById(yml.getString(gameID + ".player2"));

        }

        return member;

    }

    public Message setField(Member member, Integer number) {

        yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

        String gameID = getGameID(member);

        switchTurn(gameID);

        HashMap<Integer, String> fields = new HashMap<>();

        if (yml.contains(gameID + ".fields")) {

            for (String s : yml.getConfigurationSection(gameID + ".fields").getKeys(false)) {

                int value = Integer.parseInt(s);

                fields.put(value, yml.getString(gameID + ".fields" + "." + s));

            }

        }

        fields.put(number, getSymbol(member));

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

        for (int i : fields.keySet()) {

            BufferedImage currentImage = null;
            int currenAbstand = 0;

            if (fields.get(i).equals("cross")) {

                currentImage = cross;
                currenAbstand = crossAbstand;

            } else if (fields.get(i).equals("circle")) {

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
        ThreadChannel channel = Roonie.shardMan.getThreadChannelById(gameID);

        Member current = channel.getGuild().getMemberById(getTurn(gameID));

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

        yml.set(gameID + ".fields." + number, symbol);

        try {
            yml.save(FileSystem.GameLog);
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return message;

    }

    public void setTurn(Member member) throws IOException {

        yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

        //get id

        String id = getGameID(member);

        yml.set(id + ".turn", member.getId());

        yml.save(FileSystem.GameLog);

    }

    public String getTurn(String gameID) {

        yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

        String playerID;

        if (yml.contains(gameID + ".turn")) {

            playerID = yml.getString(gameID + ".turn");

        } else {

            playerID = yml.getString(gameID + ".player1");

        }

        return playerID;

    }

    public void switchTurn(String gameID) {

        yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

        String player1 = yml.getString(gameID + ".player1");
        String player2 = yml.getString(gameID + ".player2");

        if (yml.contains(gameID + ".turn")) {

            if (yml.getString(gameID + ".turn").equals(player1)) {

                yml.set(gameID + ".turn", player2);

                try {
                    yml.save(FileSystem.GameLog);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            } else if (yml.getString(gameID + ".turn").equals(player2)) {

                yml.set(gameID + ".turn", player1);

                try {
                    yml.save(FileSystem.GameLog);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            }

        } else {

            yml.set(gameID + ".turn", player1);

            try {
                yml.save(FileSystem.GameLog);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

        }

        checkTimeout(gameID, Roonie.mainGuild.getMemberById(getTurn(gameID)));

    }

    public boolean checkTurn(Member member) {

        yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

        String gameID = getGameID(member);
        boolean check;

        if (yml.contains(gameID + ".turn")) {

            check = yml.getString(gameID + ".turn").equals(member.getId());

        } else {

            yml.set(gameID + ".turn", yml.getString(gameID + ".player1"));

            try {
                yml.save(FileSystem.GameLog);
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            check = member.getId().equals(yml.getString(gameID + ".player1"));

        }

        return check;

    }

    public String getGameID(Member member) {

        yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

        String playerID = member.getId();

        String id = "";

        for (String s : yml.getKeys(false)) {

            if (yml.get(s + ".player1").equals(playerID) || yml.get(s + ".player2").equals(playerID)) {

                id = s;

            }

        }

        return id;

    }

    public boolean checkForWin(String gameID) {

        yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

        boolean checker = false;

        String field1 = "";
        String field2 = "";
        String field3 = "";
        String field4 = "";
        String field5 = "";
        String field6 = "";
        String field7 = "";
        String field8 = "";
        String field9 = "";

        if (yml.contains(gameID + ".fields.1")) {
            field1 = yml.getString(gameID + ".fields.1");
        }
        if (yml.contains(gameID + ".fields.2")) {
            field2 = yml.getString(gameID + ".fields.2");
        }
        if (yml.contains(gameID + ".fields.3")) {
            field3 = yml.getString(gameID + ".fields.3");
        }
        if (yml.contains(gameID + ".fields.4")) {
            field4 = yml.getString(gameID + ".fields.4");
        }
        if (yml.contains(gameID + ".fields.5")) {
            field5 = yml.getString(gameID + ".fields.5");
        }
        if (yml.contains(gameID + ".fields.6")) {
            field6 = yml.getString(gameID + ".fields.6");
        }
        if (yml.contains(gameID + ".fields.7")) {
            field7 = yml.getString(gameID + ".fields.7");
        }
        if (yml.contains(gameID + ".fields.8")) {
            field8 = yml.getString(gameID + ".fields.8");
        }
        if (yml.contains(gameID + ".fields.9")) {
            field9 = yml.getString(gameID + ".fields.9");
        }

        //check combinations

        if (field1.equals(field2) && field1.equals(field3) && field1 != "") {

            checker = true;

        } else if (field5.equals(field4) && field5.equals(field6) && field5 != "") {

            checker = true;

        } else if (field8.equals(field7) && field8.equals(field9) && field8 != "") {

            checker = true;

        } else if (field4.equals(field1) && field4.equals(field7) && field4 != "") {

            checker = true;

        } else if (field6.equals(field3) && field6.equals(field9) && field6 != "") {

            checker = true;

        } else if (field5.equals(field2) && field5.equals(field8) && field5 != "") {

            checker = true;

        } else if (field5.equals(field1) && field5.equals(field9) && field5 != "") {

            checker = true;

        } else if (field5.equals(field3) && field5.equals(field7) && field5 != "") {

            checker = true;

        }

        if (checker) {
            yml.set(gameID + ".status", "ending");
            try {
                yml.save(FileSystem.GameLog);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            timeoutList.remove(gameID);
        }

        return checker;

    }

    public boolean checkEnd(String gameID) {

        yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

        return yml.getString(gameID + ".status").equals("ending");

    }

    public static void checkTimeout(String gameId, Member player) {

        yml = YamlConfiguration.loadConfiguration(FileSystem.GameLog);

        long millis = System.currentTimeMillis();
        timeoutList.put(gameId, millis);

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {

                if (timeoutList.containsKey(gameId) & timeoutList.get(gameId).equals(millis)) {

                    EmbedBuilder banner = new EmbedBuilder();
                    banner.setColor(0xed4245);
                    banner.setImage("https://cdn.discordapp.com/attachments/985551183479463998/1012355494238761030/banner_timeout.png");

                    EmbedBuilder main = new EmbedBuilder();
                    main.setColor(0xed4245);
                    main.setTitle(":no_entry_sign: **ZEIT ABGELAUFEN**");
                    main.setDescription("> Schade. Es scheint als hat sich dein Mitspieler (" + player.getEffectiveName()  + ") seit über 10 Minuten nicht mehr gemeldet weshalb der Sieg an dich geht!");
                    main.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");
                    main.addField(":warning: Der Kanal wird in 30 Sekunden automatisch geschlossen", "", false);

                    Roonie.mainGuild.getThreadChannelById(gameId).sendMessageEmbeds(banner.build(), main.build()).queue();

                    MinigamesManager.addMatchToMember(Roonie.mainGuild.getMemberById(yml.getString(gameId + ".player1")), "tictactoe");
                    MinigamesManager.addMatchToMember(Roonie.mainGuild.getMemberById(yml.getString(gameId + ".player2")), "tictactoe");
                    MinigamesManager.addWinToMember(player, "tictactoe");

                    yml.set(gameId, null);
                    try {
                        yml.save(FileSystem.GameLog);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            Roonie.mainGuild.getThreadChannelById(gameId).delete().queue();
                        }
                    }, 1000 * 30);

                }

            }
        }, 1000 * 60 * 10);

    }

}
