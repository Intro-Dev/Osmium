package com.intro.module;

import com.intro.Osmium;
import com.intro.module.event.Event;
import com.intro.module.event.EventTick;
import com.intro.render.screen.OsmiumOptionsScreen;
import net.minecraft.client.Minecraft;

public class Gui {

    private final Minecraft mc = Minecraft.getInstance();

    public void onEvent(Event event) {
        if(mc.player != null) {
            if(event instanceof EventTick && event.isPre()) {
                if(Osmium.menuKey.consumeClick()) {
                    if(mc.screen instanceof OsmiumOptionsScreen) {
                        mc.screen.onClose();
                    } else {
                        mc.setScreen(new OsmiumOptionsScreen(null));
                    }
                }
            }
        }
    }
}
