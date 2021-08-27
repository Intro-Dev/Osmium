package com.intro.client.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

public class RenderUtil {

    public static void positionAccurateScale(PoseStack stack, float scale, int x, int y, int width, int height) {
        stack.translate((x + (width / 2f)), (y + (height / 2f)), 0);
        stack.scale(scale, scale, 0);
        stack.translate(-(x + (width / 2f)), -(y + (height / 2f)), 0);
    }

    public static void renderScaledText(PoseStack stack, Font font, Component text, int x, int y, int color, float scale) {
        stack.pushPose();
        int textWidth = font.width(text);
        int textHeight = font.lineHeight;
        positionAccurateScale(stack, scale, x, y, textWidth, textHeight);
        font.drawShadow(stack, text, x, y, color);
        stack.popPose();
    }

    public static void renderCenteredScaledText(PoseStack stack, Font font, Component text, int x, int y, int color, float scale) {
        renderScaledText(stack, font, text, (x - font.width(text) / 2), y, color, scale);
    }
}
