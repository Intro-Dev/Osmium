package com.intro.render;

import com.intro.Osmium;
import com.intro.Vector2d;
import com.intro.module.event.EventDirection;
import com.intro.module.event.EventRender;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.debug.DebugRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.*;

import java.util.ArrayList;

public class RenderManager {
    public static ArrayList<Text> textArrayList = new ArrayList<Text>();


    static MinecraftClient mc = MinecraftClient.getInstance();


    public void RenderHud(MatrixStack stack) {
        TextRenderer renderer = mc.textRenderer;
        mc.getProfiler().push("OsmiumHudRenderer");
        //draw3DString(new BlockPos(0, 128, 0), "test", 0xffffff);
        for(Text text : textArrayList) {

            renderer.drawWithShadow(stack, new LiteralText(text.text), text.posX, text.posY, text.color);
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

    public static void drawRect(double x, double y, double width, double height, int color) {

        double leftModifiable = x;
        double topModifiable = y;
        double rightModifiable = width;
        double bottomModifiable = height;
        if (leftModifiable < rightModifiable) {
            double i = leftModifiable;
            leftModifiable = rightModifiable;
            rightModifiable = i;
        }
        if (topModifiable < bottomModifiable) {
            double j = topModifiable;
            topModifiable = bottomModifiable;
            bottomModifiable = j;
        }

        Color c = new Color(color);
        RenderSystem.clearColor(c.getR(), c.getG(), c.getB(), c.getA());

        /*GlStateManager._enableBlend();
        // GlStateManager._disableTexture();
        GlStateManager._blendFuncSeparate(770, 771, 1, 0);
        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
        buffer.begin(VertexFormat.DrawMode.TRIANGLE_STRIP, VertexFormats.POSITION_COLOR);
        buffer.vertex(leftModifiable, bottomModifiable, 0.0).next();
        buffer.vertex(rightModifiable, bottomModifiable, 0.0).next();
        buffer.vertex(rightModifiable, topModifiable, 0.0).next();
        buffer.vertex(leftModifiable, topModifiable, 0.0).next();
        Tessellator.getInstance().draw();
        // GlStateManager._enableTexture();
        GlStateManager._disableBlend();



         */

    }

    public void draw3dBox(int posX, int posY, int posZ, int color) {
        Color c = new Color(color);
        DebugRenderer.drawBox(new BlockPos(posX, posY, posZ), 1, c.getR(), c.getG(), c.getB(), c.getA());
    }






    public static RenderManager CreateInstance() {
        return new RenderManager();
    }



}
