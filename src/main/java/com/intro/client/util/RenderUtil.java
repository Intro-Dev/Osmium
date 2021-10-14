package com.intro.client.util;

import com.intro.client.render.color.Color;
import com.intro.common.mixin.client.FontInvoker;
import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.ApiStatus;

import java.util.Objects;

public class RenderUtil {

    private static InternalTextRenderer textRenderer;

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

    public static void beginTextBatch(Font font, MultiBufferSource buffers, Font.DisplayMode displayMode) {
        textRenderer = new InternalTextRenderer(font, buffers, displayMode);
    }

    public static void renderShadowedTextInBatch(PoseStack stack, String text, int x, int y, int color) {
        FormattedCharSequence sequence = new TextComponent(text).getVisualOrderText();
        drawInternal(stack, sequence, x, y, color, 15728880, true);
    }

    private static void drawInternal(PoseStack stack, FormattedCharSequence text, int x, int y, int color, int light, boolean shadowed) {
        color = adjustColorForDrawCall(color);
        textRenderer.addTextToDrawCall(stack, text, x, y, shadowed, color, light);
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



    public static void drawTextBatch() {
        textRenderer.submitDrawCall();
    }


    /**
     * <p>Right now this kinda works</p>
     * <p>All render parameters are set, but it only renders black boxes in place of characters</p>
     * <p>Will finish later</p>
     */
    @ApiStatus.Experimental
    private static class InternalTextRenderer {

        private final Font font;
        private final MultiBufferSource buffers;
        private final Font.DisplayMode displayMode;

        private RenderType textRenderType;

        public InternalTextRenderer(Font font, MultiBufferSource buffers, Font.DisplayMode displayMode) {
            this.font = font;
            this.buffers = buffers;
            this.displayMode = displayMode;
        }

        public void addTextToDrawCall(PoseStack stack, FormattedCharSequence text, int x, int y, boolean shadowed, int textColor, int light) {
            var ref = new Object() {
                int xOffset = 0;
            };

            text.accept((l, style, charCode) -> {

                FontSet fontSet = ((FontInvoker) font).invokeGetFontSet(style.getFont());
                Color color;

                if(style.getColor() != null) {
                    color = new Color(Objects.requireNonNull(style.getColor()).getValue());
                } else {
                    color = new Color(textColor);
                }

                boolean bold = style.isBold();
                float brightnessMultiplier = shadowed ? 0.25F : 1.0F;
                color.multiply(brightnessMultiplier);

                GlyphInfo glyphInfo = fontSet.getGlyphInfo(charCode);
                BakedGlyph bakedGlyph = style.isObfuscated() && charCode != 32 ? fontSet.getRandomGlyph(glyphInfo) : fontSet.getGlyph(charCode);
                this.textRenderType = bakedGlyph.renderType(this.displayMode);

                if(!(bakedGlyph instanceof EmptyGlyph)) {
                    float boldOffset = bold ? glyphInfo.getBoldOffset() : 0.0F;
                    float shadowOffset = shadowed ? glyphInfo.getShadowOffset() : 0.0F;
                    VertexConsumer vertexConsumer = this.buffers.getBuffer(bakedGlyph.renderType(this.displayMode));
                    ((FontInvoker) font).invokeRenderChar(bakedGlyph, bold, style.isItalic(), boldOffset, x + shadowOffset + ref.xOffset, y + shadowOffset, stack.last().pose(), vertexConsumer, color.getFloatR(), color.getFloatG(), color.getFloatB(), color.getFloatA(), light);
                }

                ref.xOffset += glyphInfo.getAdvance(bold);
                return true;
            });
        }

        public void submitDrawCall() {
            BufferBuilder builder = (BufferBuilder) this.buffers.getBuffer(textRenderType);
            builder.end();
            RenderSystem.setShader(GameRenderer::getRendertypeTextShader);
            BufferUploader.end(builder);
        }



    }




}
