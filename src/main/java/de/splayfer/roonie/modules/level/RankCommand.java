package de.splayfer.roonie.modules.level;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.AttributedString;

public class RankCommand extends ListenerAdapter {

    protected BufferedImage backgroundImage;
    protected BufferedImage profileImage;
    protected BufferedImage xpBar;

    public void onSlashCommandInteraction (SlashCommandInteractionEvent event) {

        if (event.getName().equals("rank")) {

            Member member;

            if (event.getOptions().isEmpty()) {
                //eigener rank
                member = event.getMember();
            } else {
                member = event.getOption("nutzer").getAsMember();
            }

            //lets go

            String path = System.getProperty("user.dir");
            if (File.separator.equals("/"))
                path = "/opt/dockerfiles/clyde";
            URLConnection urlConnection;
            try {
                backgroundImage = ImageIO.read(new File(path + File.separator + "media" + File.separator + "rankImages" + File.separator + "card_background.png"));

                urlConnection = new URL(member.getUser().getEffectiveAvatarUrl()).openConnection();
                urlConnection.addRequestProperty("User-Agent", "Mozilla");
                urlConnection.setReadTimeout(5000);
                urlConnection.setConnectTimeout(5000);

                profileImage = ImageIO.read(urlConnection.getInputStream());

            } catch (IOException exception) {
                exception.printStackTrace();
            }

            Font leagueGothic = null;

            try {

                leagueGothic = Font.createFont(Font.TRUETYPE_FONT, new File(path + File.separator + "media" + File.separator + "fonts" + File.separator + "LeagueGothic-Regular.ttf"));
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(leagueGothic);

            } catch (Exception exception) {
                exception.printStackTrace();
            }

            int level = LevelManager.getLevel(member);
            int xp = LevelManager.getXp(member);

            //creating level txt

            AttributedString leveltxt = new AttributedString("LEVEL");
            leveltxt.addAttribute(TextAttribute.SIZE, 30);
            leveltxt.addAttribute(TextAttribute.FONT, leagueGothic.deriveFont(50f));

            AttributedString levelvalue = new AttributedString(String.valueOf(level));
            levelvalue.addAttribute(TextAttribute.SIZE, 30);
            levelvalue.addAttribute(TextAttribute.FONT, leagueGothic.deriveFont(80f));

            //creating xp txt

            AttributedString xptxt = new AttributedString("Aktuelle Xp: " + xp);
            xptxt.addAttribute(TextAttribute.FONT, leagueGothic.deriveFont(40f));
            xptxt.addAttribute(TextAttribute.SIZE,  30);

            BufferedImage container = new BufferedImage(934, 282, BufferedImage.TYPE_INT_ARGB);

            //creating username txt

            AttributedString usernametxt = getUsername(member);

            //creating xpbar info

            AttributedString xpbarinfo = new AttributedString(xp + " / " + LevelManager.getLevelStep(level + 1) + "XP");
            xpbarinfo.addAttribute(TextAttribute.FONT, leagueGothic.deriveFont(35f));
            xpbarinfo.addAttribute(TextAttribute.SIZE, 30);

            //set xp bar

            int xpziel = LevelManager.getLevelStep(level + 1);

            double step = (double) xpziel / 50;
            double xpcurrent = xp / step;

                try {
                    xpBar = ImageIO.read(new File(path + File.separator + "media" + File.separator + "rankImages" + File.separator + "line_" + (int) xpcurrent + ".png"));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

            //stacking images

            Graphics g = container.getGraphics();
            g.drawImage(backgroundImage, 0, 0, null);

            g.drawString(leveltxt.getIterator(), 720, 95);
            g.drawString(levelvalue.getIterator(), 820, 100);
            g.drawString(xptxt.getIterator(), 260, 150);

            g.drawString(usernametxt.getIterator(), 260, 80);
            g.drawImage(profileImage, 80, 35, 160, 160, null);

            g.drawImage(xpBar, 75, 215, 800, 35, null);
            g.drawString(xpbarinfo.getIterator(), 700, 200);

            g.dispose();

            try {
                ImageIO.write(container, "png", new File(path + File.separator + "media" + File.separator + "rankImages" + File.separator + "cache" + File.separator + member.getId() + ".png"));
            } catch (IOException exception) {
                exception.printStackTrace();
            }

            String id = member.getId();

            File tempFile = new File(path + File.separator + "media" + File.separator + "rankImages" + File.separator + "cache" + File.separator + id + ".png");

            event.reply("").addFiles(FileUpload.fromData(tempFile)).setEphemeral(true).queue();

            tempFile.deleteOnExit();

        }

    }

    public AttributedString getUsername(Member member) {

        String path = System.getProperty("user.dir");
        if (File.separator.equals("/"))
            path = "/opt/dockerfiles/clyde";

        Font leagueGothic = null;

        try {
            leagueGothic = Font.createFont(Font.TRUETYPE_FONT, new File(path + File.separator + "media" + File.separator + "fonts" + File.separator + "LeagueGothic-Regular.ttf"));
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(leagueGothic);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        String s = member.getEffectiveName();

        if (member.getEffectiveName().length() > 20) {

            s = member.getEffectiveName().substring(0, 17) + "...";

        }

        AttributedString userName = new AttributedString(s);
        userName.addAttribute(TextAttribute.FONT, leagueGothic.deriveFont(60f));
        userName.addAttribute(TextAttribute.SIZE, 30);

        return userName;
    }

}
