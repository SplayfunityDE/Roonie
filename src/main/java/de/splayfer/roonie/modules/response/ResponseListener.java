package de.splayfer.roonie.modules.response;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

public class ResponseListener extends ListenerAdapter {

    public void onMessageReceived(MessageReceivedEvent event) {

        if (!event.getAuthor().isBot()) {

            String message = event.getMessage().getContentStripped().trim();

            if (Response.existsResponse(message)) {

                Response response = Response.getResponse(message);

                switch (response.getType()) {

                    case "msg":

                    case "pic":

                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage(response.getValue()).queue();

                        break;

                    case "reaction":

                        event.getMessage().addReaction(event.getGuild().getEmojiById(response.getValue())).queue();

                        break;

                    case "url":

                        event.getChannel().sendTyping().queue();
                        event.getChannel().sendMessage(response.getValue()).setActionRow(Button.secondary("link", "Schau es dir an!").withUrl(response.getValue()).withEmoji(Emoji.fromFormatted("\uD83D\uDD17"))).queue();

                        break;

                }

            }


        }


    }

}