package com.intro.render;

import com.intro.Osmium;
import com.intro.module.event.EventDirection;
import com.intro.module.event.EventRender;
import com.intro.module.event.EventType;
import com.intro.render.drawables.Drawable;
import com.intro.render.drawables.StatusEffectDisplay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;

public class RenderManager extends DrawableHelper {

    public static ArrayList<Drawable> drawables = new ArrayList<>();

    private static MinecraftClient mc = MinecraftClient.getInstance();

    public static void initDrawables() {
        addDrawable(StatusEffectDisplay.getInstance());
    }

    public static void renderHud(MatrixStack stack) {
        mc.getProfiler().push("OsmiumHudRenderer");
        stack.push();
        for(Drawable element : drawables) {
            if(element.visible) {
                stack.push();
                element.render(stack);
                stack.pop();
            }

        }
        stack.pop();
        mc.getProfiler().pop();
    }

    public static void postRenderEvents(float tickDelta, long limitTime, MatrixStack matrix) {
        mc.getProfiler().push("OsmiumRenderer");
        EventRender EventRenderPre = new EventRender(EventDirection.PRE, tickDelta, limitTime, matrix);
        Osmium.EVENT_BUS.postEvent(EventRenderPre, EventType.EVENT_RENDER);
        mc.getProfiler().pop();
    }


    public static void addDrawable(Drawable drawable) {
        drawables.add(drawable);
    }


}
