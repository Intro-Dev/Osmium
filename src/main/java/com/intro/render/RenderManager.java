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

    public static final int HITBOX_PADDING = 20;



    static MinecraftClient mc = MinecraftClient.getInstance();


    public void RenderHud(MatrixStack stack) {
        TextRenderer renderer = mc.textRenderer;
        mc.getProfiler().push("OsmiumHudRenderer");
        //draw3DString(new BlockPos(0, 128, 0), "test", 0xffffff);
        for(Text text : textArrayList) {
            if(text.visible) {
                // this.fillGradient(stack, text.getPosX() - HITBOX_PADDING, text.getPosY() - HITBOX_PADDING, text.getPosX() + text.getTextWidth() + HITBOX_PADDING,  text.getPosY() + text.getTextHeight() + HITBOX_PADDING, 0xffffff, -804253680);
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


    public void draw3DString(BlockPos pos, String text, int color) {
        drawString(text, pos.getX(), pos.getY(), pos.getZ(), color, 5, false, 100, true);

    }

    public static void drawString(String string, double x, double y, double z, int color, float size, boolean center, float offset, boolean visibleThroughObjects) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        Camera camera = minecraftClient.gameRenderer.getCamera();
        if (camera.isReady() && minecraftClient.getEntityRenderDispatcher().gameOptions != null) {
            TextRenderer textRenderer = minecraftClient.textRenderer;
            double d = camera.getPos().x;
            double e = camera.getPos().y;
            double f = camera.getPos().z;
            MatrixStack matrixStack = RenderSystem.getModelViewStack();
            matrixStack.push();
            matrixStack.translate((float)(x - d), (float)(y - e) + 0.07F, (float)(z - f));
            matrixStack.method_34425(new Matrix4f(camera.getRotation()));
            matrixStack.scale(size, -size, size);
            RenderSystem.enableTexture();
            if (visibleThroughObjects) {
                RenderSystem.disableDepthTest();
            } else {
                RenderSystem.enableDepthTest();
            }

            RenderSystem.depthMask(true);
            matrixStack.scale(-1.0F, 1.0F, 1.0F);
            RenderSystem.applyModelViewMatrix();
            float g = center ? (float)(-textRenderer.getWidth(string)) / 2.0F : 0.0F;
            g -= offset / size;
            matrixStack.multiply(Vec3f.NEGATIVE_Y.getDegreesQuaternion(180));
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            textRenderer.draw(string, g, 0.0F, color, false, AffineTransformation.identity().getMatrix(), immediate, visibleThroughObjects, 0, 15728880);
            immediate.draw();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.enableDepthTest();
            matrixStack.pop();
            RenderSystem.applyModelViewMatrix();
        }
    }


    public static Vector2d to2D(float x, float y, float z, float cameraX, float cameraY, float cameraZ) {


        System.out.println(x);
        System.out.println(y);
        System.out.println(z);
        return new Vector2d(x / z, y / z);
    }







    public static RenderManager CreateInstance() {
        return new RenderManager();
    }

    public static void enableGL2D() {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_NICEST);
    }

    public static void disableGL2D() {

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_LINE_SMOOTH);
        GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);
        GL11.glHint(GL11.GL_POLYGON_SMOOTH_HINT, GL11.GL_DONT_CARE);
    }



}
