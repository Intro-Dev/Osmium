package dev.lobstershack.client.render.widget.drawable;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

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

    public static void reloadDrawables() {
        drawables.clear();
        ArmorDisplay.invalidateInstance();
        StatusEffectDisplay.invalidateInstance();
        PingDisplay.invalidateInstance();
        CpsDisplay.invalidateInstance();
        FpsDisplay.invalidateInstance();
        Keystrokes.invalidateInstance();
        ToggleSneak.invalidateInstance();
        initDrawables();
    }

    public static void renderHud(GuiGraphics graphics) {
        mc.getProfiler().push("OsmiumHudRenderer");
        if(!mc.options.renderDebug) {
            for(Drawable element : drawables) {
                if(element instanceof Scalable scalable) {
                    graphics.pose().pushPose();
                    scalable.scaleWithPositionIntact(graphics.pose());
                    scalable.render(graphics);
                    graphics.pose().popPose();
                } else {
                    element.render(graphics);
                }
            }
        }
        mc.getProfiler().pop();
    }

    public static void addDrawable(Drawable drawable) {
        drawables.add(drawable);
    }


}


