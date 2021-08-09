package com.intro.module;

import com.intro.Osmium;
import com.intro.module.event.Event;
import com.intro.module.event.EventTick;
import com.intro.render.screen.OsmiumOptionsScreen;
import net.minecraft.client.MinecraftClient;

public class Gui {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public void onEvent(Event event) {
        if(mc.player != null) {
            if(event instanceof EventTick && event.isPre()) {
                if(Osmium.menuKey.wasPressed()) {
                    if(mc.currentScreen instanceof OsmiumOptionsScreen) {
                        mc.currentScreen.onClose();
                    } else {
                        mc.openScreen(new OsmiumOptionsScreen(null));
                    }
                }
            }
        }
    }
}
