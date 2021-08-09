package com.intro.module;

import com.intro.config.options.BooleanOption;
import com.intro.module.event.Event;
import com.intro.module.event.EventTick;
import com.intro.util.OptionUtil;
import net.minecraft.client.MinecraftClient;

public class FullBright {

    private final MinecraftClient mc = MinecraftClient.getInstance();

    public void onEvent(Event event) {
        if(event instanceof EventTick && event.isPost()) {
            if(((BooleanOption) OptionUtil.Options.FullbrightEnabled.get()).variable)
                mc.options.gamma = 100d;
        }
    }
}
