package dev.lobstershack.client.feature;

import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.client.render.screen.OsmiumOptionsScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;

public class Gui {

    private final Minecraft mc = Minecraft.getInstance();

    private static Gui INSTANCE;

    public void registerEventListeners() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(mc.player != null) {
                if(OsmiumClient.menuKey.consumeClick()) {
                    mc.setScreen(new OsmiumOptionsScreen(null));
                }
            }
        });
    }

    public static Gui getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new Gui();
        }
        return INSTANCE;
    }
}
