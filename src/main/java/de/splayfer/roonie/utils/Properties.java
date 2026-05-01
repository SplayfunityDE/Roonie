package de.splayfer.roonie.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class Properties {

    @Value("${roonie.token}")
    private String token;

    @Value("${roonie.path}")
    private String path;

    @Value("${splayfunity.mainGuild}")
    private String mainGuild;

    @Value("${splayfunity.emojiServerGuild}")
    private String emojiServerGuild;

    @Value("${splayfunity.emojiServerGuild2}")
    private String emojiServerGuild2;

    @Value("${splayfunity.autoroles}")
    private String[] autoroles;

}
