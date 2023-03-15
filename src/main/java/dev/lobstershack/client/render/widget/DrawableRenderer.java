package dev.lobstershack.client.render.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.render.widget.drawables.*;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;

public class DrawableRenderer {

    public static final ArrayList<Drawable> drawables = new ArrayList<>();

    private static final Minecraft mc = Minecraft.getInstance();

    public static boolean shouldRenderHud = true;

    public static void initDrawables() {
         addDrawable(ArmorDisplay.getInstance());
         addDrawable(StatusEffectDisplay.getInstance());
         addDrawable(PingDisplay.getInstance());
         addDrawable(CpsDisplay.getInstance());
         addDrawable(FpsDisplay.getInstance());
         addDrawable(Keystrokes.getInstance());
         addDrawable(ToggleSneak.getInstance());
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

    public static void addDrawable(Drawable drawable) {
        drawables.add(drawable);
    }


}


