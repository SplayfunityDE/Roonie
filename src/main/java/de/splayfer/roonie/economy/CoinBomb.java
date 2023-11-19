package de.splayfer.roonie.economy;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class CoinBomb extends ListenerAdapter {

    private static boolean coinbomb = false;
    private static MessageChannel coinBombchannel = null;

    public static void startCoinBomb(MessageChannel channel) {

        if (!isCoinBomb()) {

            coinbomb = true;
            coinBombchannel = channel;

            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {

                    coinbomb = false;
                    coinBombchannel = null;

                    t.cancel();

                }
            }, 10 * 1000);

            int authorcount = 0;
            StringBuilder mentions = new StringBuilder();

            for (Message m : channel.getHistory().retrievePast(100).complete()) {

                if (authorcount <= 7) {
                    if (!mentions.toString().contains(m.getAuthor().getId())) {
                        authorcount++;
                        mentions.append(m.getAuthor().getAsMention());

                    }

                } else {
                    break;
                }

            }

            Message an = channel.sendMessage("> **EINE COINBOMBE WURDE GEPLATZT! REAGIERT SCHNELL MIT ALLEN \uD83E\uDE99 EMOJIS BEVOR ES JEMAND ANDERS TUT!**" + "  ||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||\u200B||||||||||||| " + mentions).complete();

            //adding reactions

            List<Emoji> emojis = List.of(Emoji.fromCustom("coin1", 1002493266903695360L, false), Emoji.fromCustom("coin2", 1002493284117135410L, false), Emoji.fromCustom("coin3", 1002493321450631220L, false));

            an.addReaction(emojis.get(0)).queue();
            an.addReaction(emojis.get(1)).queue();


            for (Message m : channel.getHistory().retrievePast(7).complete()) {

                Random random = new Random();
                int count = random.nextInt(3); // 0 - 2

                for (int i = 0; i < count + 1; i++) {

                    m.addReaction(emojis.get(i)).queue();

                }

            }

        }

    }

    private static boolean isCoinBomb() {

        return coinbomb;

    }

    private static boolean checkCoinBombChannel(MessageChannel channel) {

        boolean check;

        if (coinBombchannel != null) {
            check = channel.getId().equals(coinBombchannel.getId());
        } else {
            check = false;
        }

        return check;
    }

    private static boolean checkCoinBombReaction(MessageReactionAddEvent event) {

        boolean check;

        if (event.getReaction().retrieveUsers().complete().contains(event.getJDA().getUserById("886209763178844212"))) {

            List<Emoji> emojis = List.of(Emoji.fromCustom("coin1", 1002493266903695360L, false), Emoji.fromCustom("coin2", 1002493284117135410L, false), Emoji.fromCustom("coin3", 1002493321450631220L, false));

            check = emojis.contains(event.getEmoji());
        } else {
            check = false;
        }

        return check;

    }

    public void onMessageReceived(MessageReceivedEvent event) {

        if (isCoinBomb()) {

            if (checkCoinBombChannel(event.getChannel())) {

                Random random = new Random();
                int count = random.nextInt(3); // 0 - 2

                List<Emoji> emojis = List.of(Emoji.fromCustom("coin1", 1002493266903695360L, false), Emoji.fromCustom("coin2", 1002493284117135410L, false), Emoji.fromCustom("coin3", 1002493321450631220L, false));

                for (int i = 0; i < count + 1; i++) {

                    event.getMessage().addReaction(emojis.get(i)).queue();

                }

            }

        }

    }

    public void onMessageReactionAdd(MessageReactionAddEvent event) {

        if (!event.getUser().isBot()) {

            if (checkCoinBombReaction(event)) {

                event.getReaction().clearReactions().queue();

                EconomyManager.addMoneyToUser(event.getMember(), 500);

            }

        }

    }

}
