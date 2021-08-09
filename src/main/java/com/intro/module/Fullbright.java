package com.intro.module;

import com.intro.config.options.BooleanOption;
import com.intro.module.event.Event;
import com.intro.module.event.EventTick;
import com.intro.module.event.EventType;
import com.intro.util.OptionUtil;

public class Fullbright extends Module{

    public Fullbright() {
        super("Fullbright");
    }


    @EventListener(ListenedEvents = { EventType.EVENT_TICK })
    public void OnEvent(Event event) {
        if(event instanceof EventTick && event.isPost()) {
            if(((BooleanOption) OptionUtil.Options.FullbrightEnabled.get()).variable)
                mc.options.gamma = 100d;
        }
    }
}
