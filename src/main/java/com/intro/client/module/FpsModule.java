package com.intro.client.module;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventTick;
import com.intro.client.render.drawables.Text;
import com.intro.client.util.OptionUtil;
import com.intro.common.config.options.BooleanOption;
import com.intro.common.config.options.Vector2Option;
import com.intro.common.mixin.client.MinecraftAccessor;
import net.minecraft.client.Minecraft;

public class FpsModule {

    public int fps = 0;
    private final Text FpsText;

    private final Minecraft mc = Minecraft.getInstance();


    public FpsModule() {
        Text tmp;
        try {
            tmp = new Text((int) ((Vector2Option) OsmiumClient.options.get("FpsDisplayPosition")).x, (int) ((Vector2Option) OsmiumClient.options.get("FpsDisplayPosition")).y, "Fps", 0xffffff);
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
                FpsText.visible = ((BooleanOption) OsmiumClient.options.get("FpsEnabled")).variable;
                if(((BooleanOption) OsmiumClient.options.get("FpsEnabled")).variable) {
                    FpsText.text = String.valueOf(((MinecraftAccessor) Minecraft.getInstance()).getFps());
                    OsmiumClient.options.put("FpsDisplayPosition", new Vector2Option("FpsDisplayPosition", FpsText.posX, FpsText.posY));
                }
            }
        }
    }
}
