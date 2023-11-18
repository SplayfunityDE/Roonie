package de.splayfer.roonie.schedule;

import de.splayfer.roonie.Roonie;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Icon;
import net.dv8tion.jda.api.entities.Member;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.Timer;
import java.util.TimerTask;

public class BannerCounter {

    protected static Guild guild;
    protected static BufferedImage background;

    public static void updateBannerMemberCount() throws IOException {

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {

                try {

                    guild = Roonie.shardMan.awaitReady().getGuildById("873506353551925308");

                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }

                String membercount = String.valueOf(guild.getMemberCount());

                int onlineMemberCountInt = 0;

                for (Member m: guild.getMembers()) {

                    if (!m.getOnlineStatus().equals(OnlineStatus.OFFLINE)) {

                        onlineMemberCountInt = onlineMemberCountInt + 1;

                    }

                }

                String onlinecount = String.valueOf(onlineMemberCountInt);

                Font doctorGlitch = null;

                try {

                    doctorGlitch = Font.createFont(Font.TRUETYPE_FONT, new File(System.getProperty("user.dir") + File.separator + "media" + File.separator + "fonts" + File.separator + "Doctor Glitch.otf"));
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(doctorGlitch);

                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                try {
                    background = ImageIO.read(new File(System.getProperty("user.dir") + File.separator + "media" + File.separator + "bannerMemberCountimages" + File.separator + "mainbanner.png"));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                //creating text

                //stacking images

                BufferedImage container = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);

                Graphics g = container.getGraphics();

                g.drawImage(background, 0, 0, null);

                AttributedString member = null;

                switch (membercount.length()) {

                    case 1:

                    case 2:

                    case 3:

                        member = new AttributedString(membercount);
                        member.addAttribute(TextAttribute.SIZE, 30);
                        member.addAttribute(TextAttribute.FONT, doctorGlitch.deriveFont(120f));

                        break;

                    case 4:

                        member = new AttributedString(membercount.charAt(0) + "." + membercount.substring(1, 3) + " K");
                        member.addAttribute(TextAttribute.SIZE, 30);
                        member.addAttribute(TextAttribute.FONT, doctorGlitch.deriveFont(120f));

                        break;

                    case 5:

                        member = new AttributedString(membercount.substring(0, 2) + "." + membercount.charAt(2) + " K");
                        member.addAttribute(TextAttribute.SIZE, 30);
                        member.addAttribute(TextAttribute.FONT, doctorGlitch.deriveFont(120f));

                        break;

                    case 6:

                        member = new AttributedString(membercount.substring(0, 3) + " K");
                        member.addAttribute(TextAttribute.SIZE, 30);
                        member.addAttribute(TextAttribute.FONT, doctorGlitch.deriveFont(120f));

                        break;

                }

                AttributedString online = null;

                switch (onlinecount.length()) {

                    case 1:

                    case 2:

                    case 3:

                        online = new AttributedString(onlinecount);
                        online.addAttribute(TextAttribute.SIZE, 30);
                        online.addAttribute(TextAttribute.FONT, doctorGlitch.deriveFont(120f));

                        break;

                    case 4:

                        online = new AttributedString(onlinecount.substring(0, 1) + "." + onlinecount.substring(1, 3) + " K");
                        online.addAttribute(TextAttribute.SIZE, 30);
                        online.addAttribute(TextAttribute.FONT, doctorGlitch.deriveFont(120f));

                        break;

                    case 5:

                        online = new AttributedString(onlinecount.substring(0, 2) + "." + onlinecount.charAt(2) + " K");
                        online.addAttribute(TextAttribute.SIZE, 30);
                        online.addAttribute(TextAttribute.FONT, doctorGlitch.deriveFont(120f));

                        break;

                    case 6:

                        online = new AttributedString(onlinecount.substring(0, 3) + " K");
                        online.addAttribute(TextAttribute.SIZE, 30);
                        online.addAttribute(TextAttribute.FONT, doctorGlitch.deriveFont(120f));

                        break;

                }

                g.drawString(online.getIterator(), 650, 930);
                g.drawString(member.getIterator(), 1250, 930);

                g.dispose();

                try {

                    ImageIO.write(container, "png", new File(System.getProperty("user.dir") + File.separator + "media" + File.separator + "bannerMemberCountimages" + File.separator + "cache" + File.separator + "banner.png"));

                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                File finalBanner = new File(System.getProperty("user.dir") + File.separator + "media" + File.separator + "bannerMemberCountimages" + File.separator + "cache" + File.separator + "banner.png");

                try {
                    guild.getManager().setBanner(Icon.from(finalBanner)).queue();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                try {
                    updateBannerMemberCount();
                } catch (IOException exception) {
                    exception.printStackTrace();
                }

                System.out.println("[] Banner updated succesfully");

                t.cancel();

            }
        }, 300000);


    }
}
