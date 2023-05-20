package dev.lobstershack.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.lobstershack.client.render.color.Color;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import org.joml.Matrix4f;

public class RenderUtil {

    public static void positionAccurateScale(PoseStack stack, float scale, int x, int y, int width, int height) {
        stack.translate((x + (width / 2f)), (y + (height / 2f)), 0);
        stack.scale(scale, scale, 0);
        stack.translate(-(x + (width / 2f)), -(y + (height / 2f)), 0);
    }

    public static void positionAccurateScale(PoseStack stack, float scale, double x, double y, double width, double height) {
        stack.translate((x + (width / 2f)), (y + (height / 2f)), 0);
        stack.scale(scale, scale, 0);
        stack.translate(-(x + (width / 2f)), -(y + (height / 2f)), 0);
    }

    public static void positionAccurateXScale(PoseStack stack, int x, float scale) {
        stack.translate(x, 0, 0);
        stack.scale(scale,  0, 0);
        stack.translate(-x, 0, 0);
    }

    public static void positionAccurateYScale(PoseStack stack, int y, float scale) {
        stack.translate(0, y, 0);
        stack.scale(scale,  0, 0);
        stack.translate(0, -y, 0);
    }

    public static void positionAccurateScale3d(PoseStack stack, float scale, int x, int y, int z, int width, int height, int depth) {
        stack.translate((x + (width / 2f)), (y + (height / 2f)), (z + (depth / 2f)));
        stack.scale(scale, scale, scale);
        stack.translate(-(x + (width / 2f)), -(y + (height / 2f)), -(z + (depth / 2f)));
    }

    public static void positionAccurateScale(PoseStack stack, float scale, double x, double y) {
        stack.translate(x, y, 0);
        stack.scale(scale, scale, scale);
        stack.translate(-x, -y, 0);
    }


    public static void addChainedFilledBoxVertices(PoseStack stack, VertexConsumer vertexConsumer, float x, float y, float z, float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b, float a) {
        Matrix4f matrix4f = stack.last().pose();
        x1 += x;
        x2 += x;
        y1 += y;
        y2 += y;
        z1 += z;
        z2 += z;

        vertexConsumer.vertex(matrix4f, x1, y1, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x1, y1, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x1, y1, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x1, y1, z2).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x1, y2, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x1, y2, z2).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x1, y2, z2).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x1, y1, z2).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y2, z2).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y1, z2).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y1, z2).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y1, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y2, z2).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y2, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y2, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y1, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x1, y2, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x1, y1, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x1, y1, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y1, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x1, y1, z2).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y1, z2).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y1, z2).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x1, y2, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x1, y2, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x1, y2, z2).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y2, z1).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y2, z2).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y2, z2).color(r, g, b, a).endVertex();
        vertexConsumer.vertex(matrix4f, x2, y2, z2).color(r, g, b, a).endVertex();
    }


    public static void renderScaledText(GuiGraphics graphics, Font font, Component text, double x, double y, int color, float scale, boolean shadowed) {
        renderScaledText(graphics, graphics.bufferSource(), font, text, x, y, color, scale, shadowed);
        graphics.bufferSource().endBatch();
    }

    public static void renderScaledText(GuiGraphics graphics, Font font, String text, double x, double y, int color, float scale, boolean shadowed) {
        renderScaledText(graphics, font, Component.literal(text), x, y, color, scale, shadowed);
    }


    public static void renderScaledText(GuiGraphics graphics, MultiBufferSource.BufferSource bufferSource, Font font, Component text, double x, double y, int color, float scale, boolean shadowed) {
        graphics.pose().pushPose();
        positionAccurateScale(graphics.pose(), scale, x, y);
        font.drawInBatch(text.getString(), (float) x, (float) y, color, shadowed, graphics.pose().last().pose(), bufferSource, Font.DisplayMode.NORMAL, 0, 15728880, font.isBidirectional());
        graphics.pose().popPose();
    }

    public static void renderScaledText(GuiGraphics graphics, MultiBufferSource.BufferSource bufferSource, Font font, String text, double x, double y, int color, float scale, boolean shadowed) {
        renderScaledText(graphics, bufferSource, font, Component.literal(text), x, y, color, scale, shadowed);
    }



    public static void renderScaledText(GuiGraphics graphics, Font font, String text, int x, int y, int color, float scale) {
        renderScaledText(graphics, font, text, x, y, color, scale, true);
    }

    public static void renderCenteredScaledText(GuiGraphics graphics, Font font, Component text, int x, int y, int color, float scale) {
        renderScaledText(graphics, font, text.getString(), x + font.width(text) / 2, y, color, scale);
    }

    public static void renderCenteredScaledText(GuiGraphics graphics, Font font, String text, int x, int y, int color, float scale) {
        renderScaledText(graphics, font, text, (x - font.width(text) / 2), y, color, scale);
    }

    public static void renderCenteredScaledText(GuiGraphics graphics, Font font, MultiBufferSource.BufferSource bufferSource, String text, int x, int y, int color, float scale) {
        renderScaledText(graphics, bufferSource, font, text, (x - font.width(text) / 2f), y, color, scale, true);
    }


    public static void fillGradient(PoseStack stack, VertexConsumer vertexConsumer, int x, int y, int width, int height, int startColor, int endColor) {
        Color start = new Color(startColor);
        Color end = new Color(endColor);
        Matrix4f matrix4f = stack.last().pose();
        vertexConsumer.vertex(matrix4f, x + width, y, 0).color(start.getFloatR(), start.getFloatG(), start.getFloatB(), start.getFloatA()).endVertex();
        vertexConsumer.vertex(matrix4f, x, y, 0).color(start.getFloatR(), start.getFloatG(), start.getFloatB(), start.getFloatA()).endVertex();
        vertexConsumer.vertex(matrix4f, x, y + height, 0).color(end.getFloatR(), end.getFloatG(), end.getFloatB(), end.getFloatA()).endVertex();
        vertexConsumer.vertex(matrix4f, x + width, y + height, 0).color(end.getFloatR(), end.getFloatG(), end.getFloatB(), end.getFloatA()).endVertex();
    }

    public static void fill(PoseStack stack, VertexConsumer vertexConsumer, int x, int y, int width, int height, int color) {
        Matrix4f matrix4f = stack.last().pose();
        Color c = new Color(color);
        vertexConsumer.vertex(matrix4f, x + width, y, 0).color(c.getFloatR(), c.getFloatG(), c.getFloatB(), c.getFloatA()).endVertex();
        vertexConsumer.vertex(matrix4f, x, y, 0).color(c.getFloatR(), c.getFloatG(), c.getFloatB(), c.getFloatA()).endVertex();
        vertexConsumer.vertex(matrix4f, x, y + height, 0).color(c.getFloatR(), c.getFloatG(), c.getFloatB(), c.getFloatA()).endVertex();
        vertexConsumer.vertex(matrix4f, x + width, y + height, 0).color(c.getFloatR(), c.getFloatG(), c.getFloatB(), c.getFloatA()).endVertex();
    }

    public static void fill(VertexConsumer vertexConsumer, int x, int y, int width, int height, int color) {
        Color c = new Color(color);
        vertexConsumer.vertex(x + width, y, 0).color(c.getFloatR(), c.getFloatG(), c.getFloatB(), c.getFloatA()).endVertex();
        vertexConsumer.vertex(x, y, 0).color(c.getFloatR(), c.getFloatG(), c.getFloatB(), c.getFloatA()).endVertex();
        vertexConsumer.vertex(x, y + height, 0).color(c.getFloatR(), c.getFloatG(), c.getFloatB(), c.getFloatA()).endVertex();
        vertexConsumer.vertex(x + width, y + height, 0).color(c.getFloatR(), c.getFloatG(), c.getFloatB(), c.getFloatA()).endVertex();
    }

    private static int adjustColorForDrawCall(int c) {
        return (c & -67108864) == 0 ? c | -16777216 : c;
    }

    public static Vector2d adjustCordsForSpatialScale(int x, int y, int scaleX, int scaleY, int scale) {
        x += scaleX;
        y += scaleY;
        x *= scale;
        y *= scale;
        x -= scaleX;
        y -= scaleY;
        return new Vector2d(x, y);
    }

}
