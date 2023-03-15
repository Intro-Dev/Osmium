package dev.lobstershack.client.module;

import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.client.event.Event;
import dev.lobstershack.client.render.screen.OsmiumOptionsScreen;
import net.minecraft.client.Minecraft;

public class Gui {

    private final Minecraft mc = Minecraft.getInstance();

    private static Gui INSTANCE;

    public void onEvent(Event event) {
        if(mc.player != null) {
            if(OsmiumClient.menuKey.consumeClick()) {
                mc.setScreen(new OsmiumOptionsScreen(null));
            }
        }
    }

    public static Gui getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Gui();
        }
        return INSTANCE;
    }
}
