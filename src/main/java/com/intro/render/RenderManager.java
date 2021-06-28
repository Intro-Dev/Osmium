package com.intro.render;

import com.intro.Osmium;
import com.intro.Vector2d;
import com.intro.module.event.EventDirection;
import com.intro.module.event.EventRender;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.*;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

public class RenderManager extends DrawableHelper{
    public static ArrayList<Text> textArrayList = new ArrayList<>();

    static MinecraftClient mc = MinecraftClient.getInstance();


    public void RenderHud(MatrixStack stack) {
        TextRenderer renderer = mc.textRenderer;
        mc.getProfiler().push("OsmiumHudRenderer");

        for(Text text : textArrayList) {
            if(text.visible) {
                renderer.drawWithShadow(stack, new LiteralText(text.text), text.posX, text.posY, text.color);


            }

        }
        mc.getProfiler().pop();
    }

    public void render(float tickDelta, long limitTime, MatrixStack matrix) {
        mc.getProfiler().push("OsmiumRenderer");
        EventRender EventRenderPre = new EventRender(EventDirection.PRE, tickDelta, limitTime, matrix);
        Osmium.EVENT_BUS.PostEvent(EventRenderPre);
        Osmium.EVENT_BUS.PostEvent(new EventRender(EventDirection.POST, tickDelta, limitTime, matrix));
        mc.getProfiler().pop();
    }

    public static RenderManager CreateInstance() {
        return new RenderManager();
    }

}
