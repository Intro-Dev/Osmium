package com.intro.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.TranslatableText;

public class TextRenderer {

    private MatrixStack stack;

    public void begin(MatrixStack stack) {
        stack.push();
        MinecraftClient.getInstance().getProfiler().push("TextRenderer");
        this.stack = stack;
    }

    public void drawText(Text text) {
        MinecraftClient.getInstance().textRenderer.draw(stack, new TranslatableText(text.text), text.posX, text.posY, text.color);

    }

    public void end() {
        stack.pop();
        MinecraftClient.getInstance().getProfiler().pop();
    }

}
