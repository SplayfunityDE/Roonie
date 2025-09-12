package de.splayfer.roonie.modules.tempchannel;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public enum TcEmbeds {

    //// TempChannels

    // Settings

    TC_SETTINGS_BANUSER (new EmbedBuilder()
            .setColor(0xE03F3F)
            .setThumbnail("https://cdn.discordapp.com/attachments/883278317753626655/887743062900637806/705579165276962906.png")
            .setTitle("Banne einen Nutzer von deinem Kanal!")
            .setDescription("Hier kannst du einen Nutzer mithilfe der Buttons einen Nutzer permanent von diesem Kanal bannen. Bitte benutze diese Funktion nur, wenn es keinen anderen Ausweg gibt. Das Trolling durch diese Funktion kann bestraft werden!")),

    TC_SETTINGS_MUTEUSER (new EmbedBuilder()
            .setColor(0xE03F3F)
            .setThumbnail("https://cdn.discordapp.com/attachments/883278317753626655/887744919769337938/CacOeAC.png")
            .setTitle("Muten einen Nutzer in deinem Kanal!")
            .setDescription("Hier kannst du einen Nutzer in deinem Kanal stummschalten. Ist diese Funktion aktiviert, kann niemand mehr diesen Nutzer hören.")),

    TC_SETTINGS_MODS(new EmbedBuilder()
            .setColor(0x3b76c4)
            .setThumbnail("https://cdn.discordapp.com/attachments/883278317753626655/887743384071073853/3446-blurple-certifiedmoderator.png")
            .setTitle("Füge Moderatoren auf deinem Kanal hinzu!")
            .setDescription("Hier kannst du bestimmten Nutzer Berechtigungen innerhalb deines Kanals erteilen! Tu dies nur bei Personen, denen du vertraust!")),

    TC_SETTINGS_REGION (new EmbedBuilder()
            .setColor(0x3FE065)
            .setThumbnail("https://cdn.discordapp.com/attachments/883278317753626655/887746327293866004/map-1272165__340.png")
            .setTitle("Setze eine Region für deinen Kanal!")
            .setDescription("Hier kannst du die Region auswählen, aus der dein Kanal die Audio der Nutzer empfängt. Wir empfehlen dir Brasilien, da dort die Auslastung am geringsten ist!")),

    TC_SETTINGS_USERLIMIT (new EmbedBuilder()
            .setColor(0x3b76c4)
            .setThumbnail("https://cdn.discordapp.com/attachments/883278317753626655/887747127286050869/8263-blurple-members.png")
            .setTitle("Setzte ein Nutzerlimit für deinen Kanal!")
            .setDescription("Hier kannst du ein Limit für alle Nutzer aus diesem Kanal setzen. Wenn dieses Limit erfüllt ist kann kein neuer Nutzer mehr joinen!")),

    TC_SETTINGS_SETNAME (new EmbedBuilder()
            .setColor(0x24B794)
            .setThumbnail("https://cdn.discordapp.com/attachments/883278317753626655/887742833254088744/4533-language.png")
            .setTitle("Setze einen Namen für deinen Kanal!")
            .setDescription("Hier kannst du den Namen deines Sprachkanals ändern! Bitte benutze hierfür keine obszönen Ausdrücke!")),

    TC_SETTINGS_SETACTIVITY (new EmbedBuilder()
            .setColor(0xfec865)
            .setTitle("Starte eine Aktivität!")
            .setDescription("Hier kannst du für alle Nutzer aus deinem Kanal eine Aktivität starten!")
            .setThumbnail("https://cdn.discordapp.com/attachments/906251556637249547/923637440055500810/1200px-Toicon-icon-fandom-game.png")),

    //// General

    SUCCESS_BANNER (new EmbedBuilder()
            .setColor(0x43b480)
            .setImage("https://cdn.discordapp.com/attachments/880725442481520660/914518380088819742/banner_erfolg.png"));

    private EmbedBuilder eb;

    private TcEmbeds(EmbedBuilder embedBuilder) {

        this.eb = embedBuilder;

    }

    public MessageEmbed getEmbed() {

        return this.eb.build();

    }

    public EmbedBuilder getBuilder() {

        return this.eb;

    }

}
