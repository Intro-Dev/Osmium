package com.intro.module;

import com.intro.config.BooleanOption;
import com.intro.config.OptionUtil;
import com.intro.mixin.MinecraftClientAccessor;
import com.intro.module.event.Event;
import com.intro.module.event.EventJoinWorld;
import com.intro.module.event.EventTick;
import com.intro.render.Text;
import net.minecraft.client.MinecraftClient;

public class FpsModule extends Module{

    public int fps = 0;
    private Text FpsText;


    public FpsModule() {
        super("Fps");
    }

    public void OnEvent(Event event) {
        if(FpsText != null) {
            if(event instanceof EventTick && mc.player != null && !(String.valueOf(((MinecraftClientAccessor) MinecraftClient.getInstance()).getCurrentFps()).equals(FpsText.text)) && ((BooleanOption) OptionUtil.Options.FpsEnabled).variable){
                FpsText.text = String.valueOf(((MinecraftClientAccessor) MinecraftClient.getInstance()).getCurrentFps());
            }
        }
        if(event instanceof EventJoinWorld) {
            assert mc.currentScreen != null;
            FpsText = new Text(mc.currentScreen.width / 2 - 120, mc.currentScreen.height / 2 + 250, "", 0xffffff);
        }
    }
}
