package com.intro.client.render.drawables;

import com.intro.client.render.RenderManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.TextComponent;

public class Text extends Scalable {
    public String text;
    public int color;

    private final Minecraft mc = Minecraft.getInstance();

    public Text(int posX, int posY, String text, int color) {
        this.posX = posX;
        this.posY = posY;
        this.text = text;
        this.color = color;
        RenderManager.drawables.add(this);
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

    @Override
    public void onPositionChange(int newX, int newY, int oldX, int oldY) {

    }

    @Override
    public void render(PoseStack stack) {
        this.width = mc.font.width(this.text);
        this.height = mc.font.lineHeight;

        Font renderer = mc.font;
        renderer.drawShadow(stack, new TextComponent(this.text), this.posX, this.posY, this.color);
    }

    @Override
    public void onScaleChange(double oldScale, double newScale) {

    }
}
