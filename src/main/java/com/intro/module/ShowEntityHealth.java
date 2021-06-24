package com.intro.module;

import com.intro.module.event.Event;
import com.intro.module.event.EventRender;
import com.intro.render.RenderManager;
import net.minecraft.client.MinecraftClient;

public class ShowEntityHealth extends Module{

    public ShowEntityHealth() {
        super("ShowEntityHealth");
    }



    public void OnEvent(Event event) {
        if(event instanceof EventRender) {
            EventRender eventRender = (EventRender) event;
            RenderManager manager = new RenderManager();
            // manager.drawLine(100, 100, 0, 200, 200, 00, 0xffffff, eventRender.getMatrixStack());
        }
    }
}
