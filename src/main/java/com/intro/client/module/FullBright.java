package com.intro.client.module;

import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventTick;
import com.intro.client.util.OptionUtil;
import com.intro.common.config.options.BooleanOption;
import net.minecraft.client.Minecraft;

public class FullBright {

    private final Minecraft mc = Minecraft.getInstance();

    public void onEvent(Event event) {
        if(event instanceof EventTick && event.isPost()) {
            if(((BooleanOption) OptionUtil.Options.FullbrightEnabled.get()).variable)
                mc.options.gamma = 100d;
        }
    }
}
