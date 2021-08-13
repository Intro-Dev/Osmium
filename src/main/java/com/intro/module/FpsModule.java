package com.intro.module;

import com.intro.Osmium;
import com.intro.config.options.BooleanOption;
import com.intro.config.options.Vector2Option;
import com.intro.mixin.MinecraftAccessor;
import com.intro.module.event.Event;
import com.intro.module.event.EventTick;
import com.intro.render.drawables.Text;
import com.intro.util.OptionUtil;
import net.minecraft.client.Minecraft;

public class FpsModule {

    public int fps = 0;
    private final Text FpsText;

    private final Minecraft mc = Minecraft.getInstance();


    public FpsModule() {
        Text tmp;
        try {
            tmp = new Text((int) ((Vector2Option) Osmium.options.get("FpsDisplayPosition")).x, (int) ((Vector2Option) Osmium.options.get("FpsDisplayPosition")).y, "Fps", 0xffffff);
        } catch (NullPointerException e) {
            OptionUtil.ShouldResaveOptions = true;
            OptionUtil.LOGGER.warn("Options file is corrupt, creating a new one!");
            tmp = new Text(5, 5, "Fps", 0xffffff);
        }
        FpsText = tmp;
        FpsText.guiElement = true;
        FpsText.visible = false;
    }

    public void onEvent(Event event) {
        if(FpsText != null) {
            if(event instanceof EventTick && mc.player != null) {
                FpsText.visible = ((BooleanOption) Osmium.options.get("FpsEnabled")).variable;
                if(((BooleanOption) Osmium.options.get("FpsEnabled")).variable) {
                    FpsText.text = String.valueOf(((MinecraftAccessor) Minecraft.getInstance()).getFps());
                    Osmium.options.put("FpsDisplayPosition", new Vector2Option("FpsDisplayPosition", FpsText.posX, FpsText.posY));
                }
            }
        }
    }
}
