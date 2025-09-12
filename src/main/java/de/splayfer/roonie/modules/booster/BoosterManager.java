package de.splayfer.roonie.modules.booster;

import de.splayfer.roonie.Roonie;

public class BoosterManager {

    public static void init() {
        Roonie.builder.addEventListeners(new BoosterNotification(), new BoosterWall());
    }
}