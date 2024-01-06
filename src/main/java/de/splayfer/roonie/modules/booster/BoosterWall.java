package de.splayfer.roonie.modules.booster;

import de.splayfer.roonie.Roonie;
import de.splayfer.roonie.config.Config;
import de.splayfer.roonie.utils.enums.Channels;
import de.splayfer.roonie.utils.enums.Embeds;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.FileUpload;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class BoosterWall extends ListenerAdapter {

    protected static BufferedImage background;

    public static void updateBoosterStats() {
        if (Config.existsConfig("booster")) {
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    String path = System.getProperty("user.dir");
                    if (File.separator.equals("/"))
                        path = "/bot";
                    List<Member> boosterList;
                    if (Roonie.mainGuild.getBoosters().size() > 40)
                        boosterList = Roonie.mainGuild.getBoosters().subList(0, 15);
                    else
                        boosterList = Roonie.mainGuild.getBoosters();
                    try {
                        background = ImageIO.read(new File(path + File.separator + "media" + File.separator + "boosterImg" + File.separator + "boosterBannerTemplate.png"));
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }

                    BufferedImage container = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);

                    Graphics g = container.getGraphics();
                    g.drawImage(background, 0, 0, null);

                    Font openSans = null;
                    try {
                        openSans = Font.createFont(Font.TRUETYPE_FONT, new File(path + File.separator + "media" + File.separator + "fonts" + File.separator + "OpenSans_Condensed-ExtraBold.ttf"));
                    } catch (FontFormatException e) {
                        e.printStackTrace();
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                    ge.registerFont(openSans);

                    int i = 1;
                    for (Member m : boosterList) {

                        int aspect = 0;

                        AttributedString name;

                        if (m.getEffectiveName().length() > 25) {
                            name = new AttributedString(m.getEffectiveName().substring(0, 22) + "...");
                            name.addAttribute(TextAttribute.FONT, openSans.deriveFont(35f));
                            aspect = 5;
                        } else if (m.getEffectiveName().length() > 15) {
                            name = new AttributedString(m.getEffectiveName());
                            name.addAttribute(TextAttribute.FONT, openSans.deriveFont(35f));
                            aspect = 5;
                        } else {
                            name = new AttributedString(m.getEffectiveName());
                            name.addAttribute(TextAttribute.FONT, openSans.deriveFont(50f));
                        }

                        switch (i) {
                            case 1:
                                g.drawString(name.getIterator(), 175, 430 - aspect);
                                break;
                            case 2:
                                g.drawString(name.getIterator(), 175, 550 - aspect);
                                break;
                            case 3:
                                g.drawString(name.getIterator(), 175, 670 - aspect);
                                break;
                            case 4:
                                g.drawString(name.getIterator(), 175, 785 - aspect);
                                break;
                            case 5:
                                g.drawString(name.getIterator(), 175, 900 - aspect);
                                break;
                            case 6:
                                g.drawString(name.getIterator(), 790, 430 - aspect);
                                break;
                            case 7:
                                g.drawString(name.getIterator(), 790, 550 - aspect);
                                break;
                            case 8:
                                g.drawString(name.getIterator(), 790, 670 - aspect);
                                break;
                            case 9:
                                g.drawString(name.getIterator(), 790, 785 - aspect);
                                break;
                            case 10:
                                g.drawString(name.getIterator(), 790, 900 - aspect);
                                break;
                            case 11:
                                g.drawString(name.getIterator(), 1400, 430 - aspect);
                                break;
                            case 12:
                                g.drawString(name.getIterator(), 1400, 550 - aspect);
                                break;
                            case 13:
                                g.drawString(name.getIterator(), 1400, 670 - aspect);
                                break;
                            case 14:
                                g.drawString(name.getIterator(), 1400, 785 - aspect);
                                break;
                            case 15:
                                g.drawString(name.getIterator(), 1400, 900 - aspect);
                                break;
                        }
                        i++;
                    }
                    //stacking images
                    g.dispose();

                    try {
                        ImageIO.write(container, "png", new File(path + File.separator + "media" + File.separator + "boosterImg" + File.separator + "boosterBanner.png"));
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }

                    File tempFile = new File(path + File.separator + "media" + File.separator + "boosterImg" + File.separator + "boosterBanner.png");

                    Message m = Channels.MEDIACHANNEL.getMessageChannel(Roonie.mainGuild).sendFiles(FileUpload.fromData(tempFile)).complete();
                    String picUrl = m.getAttachments().get(0).getUrl();
                    m.delete().queueAfter(10, TimeUnit.SECONDS);
                    Message msg = Config.getConfigChannel("booster").retrieveMessageById(Config.getConfigMessageId("booster")).complete();

                    EmbedBuilder message = new EmbedBuilder();
                    message.setColor(0x28346d);
                    message.setTitle(msg.getEmbeds().get(1).getTitle());
                    message.setDescription(msg.getEmbeds().get(1).getDescription());
                    message.addField(msg.getEmbeds().get(1).getFields().get(0));
                    message.addField(msg.getEmbeds().get(1).getFields().get(1));
                    message.setImage(picUrl);

                    List<Button> buttons = new ArrayList<>();
                    buttons.add(Button.secondary(msg.getActionRows().get(0).getButtons().get(0).getId(), msg.getActionRows().get(0).getButtons().get(0).getLabel()).withEmoji(msg.getActionRows().get(0).getButtons().get(0).getEmoji()));
                    msg.editMessageEmbeds(Embeds.BANNER_BOOSTER, message.build()).setActionRow(buttons).queue();
                    tempFile.deleteOnExit();
                    System.out.println("[Splayfer] Booster update");
                }
            }, 0, 120 * 1000);
        }
    }

    public void onButtonInteraction (ButtonInteractionEvent event) {

        if (event.getButton().getId().equals("boostinfo")) {
            EmbedBuilder banner = new EmbedBuilder();
            banner.setColor(0x28346d);
            banner.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905401313720954920/banner_booster.png");

            EmbedBuilder message = new EmbedBuilder();
            message.setColor(0x28346d);
            message.setTitle(":sparkles: Details zum Server Boosting");
            message.setDescription("Hier wirst du über alles wichtige aufgeklärt, was du über das Server Boosting wissen musst!");
            message.addField("Wie bekomme ich Boosts?", "Boosts kannst du über folgende Möglichkeiten erhalten: \n" +
                    "<:x_:906141028657037343> Direkter Kauf eines Boosts (4,50$) \n" +
                    "<:x_:906141028657037343> Abonnieren von Discord Nitro \n" +
                    "<:x_:906141028657037343> Erhalten eines Nitro Geschenkes", false);
            message.addField("Vorteile als Booster", "Da Booster unseren Server mit ihrem Vorhaben unterstützen, werden diese auch mit bestimmten Vorteilen belohnt! \n" +
                    "<:x_:906141028657037343> Den exklusiven Booster Rang \n" +
                    "<:x_:906141028657037343> Erstelle Diskussionen (Threads) \n" +
                    "<:x_:906141028657037343> Erhalte 2x Level XP \n" +
                    "<:x_:906141028657037343> Beantrage deinen eigenen Bot", false);
            message.setImage("https://cdn.discordapp.com/attachments/880725442481520660/905443533824077845/auto_faqw.png");

            event.replyEmbeds(banner.build(), message.build()).setEphemeral(true).queue();
        }
    }
}