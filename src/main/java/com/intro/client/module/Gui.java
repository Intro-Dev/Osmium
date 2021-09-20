package com.intro.client.module;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventTick;
import com.intro.client.render.screen.OsmiumCapeOptionsScreen;
import com.intro.client.render.screen.OsmiumOptionsScreen;
import net.minecraft.client.Minecraft;

public class Gui {

    private final Minecraft mc = Minecraft.getInstance();

    public void onEvent(Event event) {
        if(mc.player != null) {
            if(event instanceof EventTick && event.isPre()) {
                if(OsmiumClient.menuKey.consumeClick()) {
                    if(mc.screen instanceof OsmiumOptionsScreen) {
                        mc.screen.onClose();
                    } else {
                        mc.setScreen(new OsmiumCapeOptionsScreen(null));
                    }
                }
            }
        }
    }
}
