package com.intro.module;

import com.intro.config.BooleanOption;
import com.intro.config.OptionUtil;
import com.intro.module.event.Event;
import com.intro.module.event.EventTick;

public class Fullbright extends Module{

    public Fullbright() {
        super("Fullbright");
    }



    public void OnEvent(Event event) {

        if(event instanceof EventTick) {
            if(((BooleanOption) OptionUtil.Options.FullbrightEnabled.get()).variable)
                mc.options.gamma = 100d;
        }
    }
}
