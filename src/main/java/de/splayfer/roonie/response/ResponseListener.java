package de.splayfer.roonie.response;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ResponseListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {

        if (!event.getAuthor().isBot()) {

            String message = event.getMessage().getContentStripped().trim();

            if (ResponseManager.existsResponse(message)) {

                ResponseManager.Response response = ResponseManager.getResponse(message);

                switch (response.type()) {

                    case "msg":

                    case "pic":

                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage(response.value()).queue();

                        break;

                    case "reaction":

                        event.getMessage().addReaction(event.getGuild().getEmojiById(response.value())).queue();

                        break;

                    case "url":

                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage(response.value()).setActionRow(Button.secondary("link", "Schau es dir an!").withUrl(response.value()).withEmoji(Emoji.fromFormatted("\uD83D\uDD17"))).queue();

                        break;

                }

            }


        }


    }

}
