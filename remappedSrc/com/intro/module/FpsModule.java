package com.intro.module;

import com.intro.Osmium;
import com.intro.config.BooleanOption;
import com.intro.config.OptionUtil;
import com.intro.config.Vector2Option;
import com.intro.mixin.MinecraftClientAccessor;
import com.intro.module.event.Event;
import com.intro.module.event.EventRender;
import com.intro.module.event.EventTick;
import com.intro.module.event.EventType;
import com.intro.render.RenderManager;
import com.intro.render.Text;
import net.minecraft.client.MinecraftClient;

public class FpsModule extends Module{

    public int fps = 0;
    private Text FpsText;


    public FpsModule() {
        super("Fps");
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

    @EventListener( ListenedEvents = {EventType.EVENT_TICK} )
    public void OnEvent(Event event) {
        if(FpsText != null) {
            if(event instanceof EventTick && mc.player != null) {
                FpsText.visible = ((BooleanOption) Osmium.options.get("FpsEnabled")).variable;
                if(((BooleanOption) Osmium.options.get("FpsEnabled")).variable) {
                    FpsText.text = String.valueOf(((MinecraftClientAccessor) MinecraftClient.getInstance()).getCurrentFps());
                    Osmium.options.put("FpsDisplayPosition", new Vector2Option("FpsDisplayPosition", FpsText.posX, FpsText.posY));
                }
            }
        }
    }
}
