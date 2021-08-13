package com.intro.render;

import com.intro.Osmium;
import com.intro.module.event.EventDirection;
import com.intro.module.event.EventRender;
import com.intro.module.event.EventType;
import com.intro.render.drawables.ArmorDisplay;
import com.intro.render.drawables.Drawable;
import com.intro.render.drawables.StatusEffectDisplay;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class RenderManager {

    public static ArrayList<Drawable> drawables = new ArrayList<>();

    private static Minecraft mc = Minecraft.getInstance();

    public static void initDrawables() {
        // for some reason ArmorDisplay has to be first, or it doesn't render transparency
        // I don't know why this happens
        // why does this happen
        addDrawable(ArmorDisplay.getInstance());
        addDrawable(StatusEffectDisplay.getInstance());
    }

    public static void renderHud(PoseStack stack) {
        mc.getProfiler().push("OsmiumHudRenderer");
        for(Drawable element : drawables) {
            if(element.visible) {
                // make a copy of the stack for each Drawable, so they don't interact
                element.render(stack);
            }
        }
        mc.getProfiler().pop();
    }

    public static void postRenderEvents(float tickDelta, long limitTime, PoseStack stack) {
        mc.getProfiler().push("OsmiumRenderer");
        EventRender EventRenderPre = new EventRender(EventDirection.PRE, tickDelta, limitTime, stack);
        Osmium.EVENT_BUS.postEvent(EventRenderPre, EventType.EVENT_RENDER);
        mc.getProfiler().pop();
    }

    public static void addDrawable(Drawable drawable) {
        drawables.add(drawable);
    }


}


