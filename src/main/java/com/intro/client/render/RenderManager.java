package com.intro.client.render;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.EventDirection;
import com.intro.client.module.event.EventRender;
import com.intro.client.module.event.EventType;
import com.intro.client.render.drawables.*;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class RenderManager {

    public static final ArrayList<Drawable> drawables = new ArrayList<>();

    private static final Minecraft mc = Minecraft.getInstance();

    public static void initDrawables() {
         addDrawable(ArmorDisplay.getInstance());
         addDrawable(StatusEffectDisplay.getInstance());
         addDrawable(PingDisplay.getInstance());
         addDrawable(CpsDisplay.getInstance());
         addDrawable(FpsDisplay.getInstance());
         addDrawable(Keystrokes.getInstance());
    }

    public static void renderHud(PoseStack stack) {
        mc.getProfiler().push("OsmiumHudRenderer");
        if(!mc.options.renderDebug) {
            for(Drawable element : drawables) {
                if(element instanceof Scalable scalable) {
                    stack.pushPose();
                    scalable.scaleWithPositionIntact(stack);
                    scalable.render(stack);
                    stack.popPose();
                } else {
                    element.render(stack);
                }
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


