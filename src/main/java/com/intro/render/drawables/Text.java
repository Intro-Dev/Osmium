package com.intro.render.drawables;

import com.intro.render.RenderManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.TextComponent;

public class Text extends Drawable {
    public String text = "";
    public int color;

    public static final int HITBOX_PADDING = 20;

    private Minecraft mc = Minecraft.getInstance();

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
    public void render(PoseStack stack) {
        if(firstRun) {
            this.width = mc.font.width(this.text);
            this.height = mc.font.lineHeight;
            firstRun = false;
        }
        // stack.push();
        Font renderer = mc.font;
        renderer.drawShadow(stack, new TextComponent(this.text), this.posX, this.posY, this.color);
        // stack.pop();
    }
}
