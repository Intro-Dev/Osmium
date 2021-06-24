package com.intro.module;

import com.intro.OsmiumOptions;
import com.intro.module.event.Event;
import com.intro.module.event.EventStartGame;
import com.intro.module.event.EventTick;
import net.minecraft.client.MinecraftClient;

public class Fullbright extends Module{

    public Fullbright() {
        super("Fullbright");
    }



    public void OnEvent(Event event) {

        if(event instanceof EventTick) {
            if(OsmiumOptions.FullbrightEnabled)
                mc.options.gamma = 100d;
        }
    }
}
