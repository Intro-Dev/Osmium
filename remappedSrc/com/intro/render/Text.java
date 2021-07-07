package com.intro.render;

import com.intro.render.screen.OsmiumGuiEditScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class Text implements Element, Drawable, Selectable {
    public String text = "";
    public int posX, posY;
    public int color;
    public boolean visible = false;
    public static final int HITBOX_PADDING = 20;

    private MinecraftClient mc = MinecraftClient.getInstance();

    public boolean guiElement = false;

    public Text(int posX, int posY, String text, int color) {
        this.posX = posX;
        this.posY = posY;
        this.text = text;
        this.color = color;
        RenderManager.textArrayList.add(this);
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
        RenderManager.textArrayList.remove(this);
    }

    public int getTextHeight() {
        return MinecraftClient.getInstance().textRenderer.fontHeight;
    }

    public int getTextWidth() {
        return MinecraftClient.getInstance().textRenderer.getWidth(this.text);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(this.guiElement) {
            if(mouseX + this.getTextWidth() < mc.currentScreen.width || mouseX - this.getTextWidth() < 0) {
                this.posX = (int) mouseX;
            }
            if(mouseY + this.getTextHeight() < mc.currentScreen.height || mouseY - this.getTextHeight() < 0) {
                this.posY = (int) mouseY;
            }
        }
        return false;
    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if(visible)
            mc.textRenderer.drawWithShadow(matrices, new LiteralText(this.text), this.posX, this.posY, this.color);
    }

    public boolean isPositionWithinBounds(int x, int y) {
        return x > this.getPosX() - HITBOX_PADDING && x < this.getPosX() + this.getTextWidth() + HITBOX_PADDING && y > this.getPosY() - HITBOX_PADDING && y < this.getPosY() + getTextHeight() + HITBOX_PADDING;
    }

    public int getCenter(int pos1, int pos2) {
        return (pos1 + pos2) / 2;
    }

    public void setCenterPosition(int x, int y) {
        this.posX = x + this.getTextWidth() / 2;
        this.posY = y + this.getTextHeight() / 2;

    }

    @Override
    public SelectionType getType() {
        return SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }
}
