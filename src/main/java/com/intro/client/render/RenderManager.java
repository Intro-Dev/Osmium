package com.intro.client.render;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.EventDirection;
import com.intro.client.module.event.EventRender;
import com.intro.client.module.event.EventType;
import com.intro.client.render.drawables.ArmorDisplay;
import com.intro.client.render.drawables.Drawable;
import com.intro.client.render.drawables.StatusEffectDisplay;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class RenderManager {

    public static final ArrayList<Drawable> drawables = new ArrayList<>();

    private static final Minecraft mc = Minecraft.getInstance();

    public static void initDrawables() {
        // for some reason ArmorDisplay has to be first, or it doesn't render transparency
        // I don't know why this happens
        // why does this happen
        // well hi there
        // these features aren't' t done yet
        // but are still being implemented
         addDrawable(ArmorDisplay.getInstance());
         addDrawable(StatusEffectDisplay.getInstance());
    }

    public static void renderHud(PoseStack stack) {
        mc.getProfiler().push("OsmiumHudRenderer");
        for(Drawable element : drawables) {
            if(element.visible) {
                element.render(stack);
            }
        }
        mc.getProfiler().pop();
    }

    public static void postRenderEvents(float tickDelta, long limitTime, PoseStack stack) {
        mc.getProfiler().push("OsmiumRenderer");
        EventRender EventRenderPre = new EventRender(EventDirection.PRE, tickDelta, limitTime, stack);
        OsmiumClient.EVENT_BUS.postEvent(EventRenderPre, EventType.EVENT_RENDER);
        mc.getProfiler().pop();
    }

    public static void addDrawable(Drawable drawable) {
        drawables.add(drawable);
    }


}


