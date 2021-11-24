package com.intro.client.module;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventTick;
import com.intro.common.config.Options;
import net.minecraft.client.Minecraft;

public class FullBright {

    private final Minecraft mc = Minecraft.getInstance();

    public void onEvent(Event event) {
        if(event instanceof EventTick && event.isPost()) {
            if(OsmiumClient.options.getBooleanOption(Options.FullbrightEnabled).get())
                mc.options.gamma = 100d;
        }
    }
}
