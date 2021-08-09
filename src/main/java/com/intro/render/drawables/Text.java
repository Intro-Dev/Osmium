package com.intro.render.drawables;

import com.intro.render.RenderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class Text extends Drawable {
    public String text = "";
    public int color;

    public static final int HITBOX_PADDING = 20;

    private MinecraftClient mc = MinecraftClient.getInstance();

    public boolean guiElement = false;

    private boolean firstRun = true;

    public Text(int posX, int posY, String text, int color) {
        this.posX = posX;
        this.posY = posY;
        this.text = text;
        this.color = color;
        this.width = this.getTextWidth();
        this.height = this.getTextHeight();
        RenderManager.drawables.add(this);
    }

    public Text(int posX, int posY, Object text, int color) {
        this(posX, posY, String.valueOf(text), color);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setText(Object text) {
        this.text = String.valueOf(text);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void destroySelf() {
        RenderManager.drawables.remove(this);
    }

    public int getTextHeight() {
        return this.height;
    }

    public int getTextWidth() {
        return this.width;
    }


    @Override
    public void render(MatrixStack stack) {
        if(firstRun) {
            this.width = mc.textRenderer.getWidth(this.text);
            this.height = mc.textRenderer.fontHeight;
            firstRun = false;
        }

        TextRenderer renderer = mc.textRenderer;
        renderer.drawWithShadow(stack, new LiteralText(this.text), this.posX, this.posY, this.color);
    }
}
