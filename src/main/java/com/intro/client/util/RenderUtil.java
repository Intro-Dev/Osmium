package com.intro.client.util;

import com.intro.client.render.color.Color;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;

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


    public static void renderScaledText(PoseStack stack, Font font, Component text, double x, double y, int color, float scale, boolean shadowed) {
        stack.pushPose();
        positionAccurateScale(stack, scale, x, y);
        int ignored = shadowed ? font.drawShadow(stack, text, (int) x, (int) y, color) : font.draw(stack, text, (int) x, (int) y, color);
        stack.popPose();
    }

    public static void renderScaledText(PoseStack stack, MultiBufferSource.BufferSource bufferSource, Font font, Component text, double x, double y, int color, float scale, boolean shadowed) {
        stack.pushPose();
        positionAccurateScale(stack, scale, x, y);
        font.drawInBatch(text.getString(), (float) x, (float) y, color, shadowed, stack.last().pose(), bufferSource, false, 0, 15728880, font.isBidirectional());
        stack.popPose();
    }

    public static void renderScaledText(PoseStack stack, MultiBufferSource.BufferSource bufferSource, Font font, String text, double x, double y, int color, float scale, boolean shadowed) {
        stack.pushPose();
        int textWidth = font.width(text);
        // translate by text width or else it is off a bit
        positionAccurateScale(stack, scale, x, y, textWidth, 0);
        font.drawInBatch(text, (float) x, (float) y, color, shadowed, stack.last().pose(), bufferSource, false, 0, 15728880, font.isBidirectional());
        stack.popPose();
    }

    public static void renderScaledText(PoseStack stack, Font font, String text, double x, double y, int color, float scale, boolean shadowed) {
        stack.pushPose();
        positionAccurateScale(stack, scale, x, y);
        int ignored = shadowed ? font.drawShadow(stack, text, (int) x, (int) y, color) : font.draw(stack, text, (int) x, (int) y, color);
        stack.popPose();
    }

    public static void renderScaledText(PoseStack stack, Font font, String text, int x, int y, int color, float scale) {
        stack.pushPose();
        int textWidth = font.width(text);
        int textHeight = font.lineHeight;
        positionAccurateScale(stack, scale, x, y, textWidth, textHeight);
        font.drawShadow(stack, text, x, y, color);
        stack.popPose();
    }

    public static void renderCenteredScaledText(PoseStack stack, Font font, Component text, int x, int y, int color, float scale) {
        renderScaledText(stack, font, text.getString(), x + font.width(text) / 2, y, color, scale);
    }

    public static void renderCenteredScaledText(PoseStack stack, Font font, String text, int x, int y, int color, float scale) {
        renderScaledText(stack, font, text, (x - font.width(text) / 2), y, color, scale);
    }

    public static void renderCenteredScaledText(PoseStack stack, Font font, MultiBufferSource.BufferSource bufferSource, String text, int x, int y, int color, float scale) {
        renderScaledText(stack, bufferSource, font, text, (x - font.width(text) / 2f), y, color, scale, true);
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
