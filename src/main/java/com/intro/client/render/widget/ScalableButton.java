package com.intro.client.render.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ScalableButton extends Button {

    private float scale;

    public ScalableButton(int i, int j, int k, int l, Component component, OnPress onPress, float scale) {
        super(i, j, k, l, component, onPress);
        this.scale = scale;
    }

    public ScalableButton(int i, int j, int k, int l, Component component, OnPress onPress, OnTooltip onTooltip) {
        super(i, j, k, l, component, onPress, onTooltip);
    }

    public int getScaledX() {
        return (int) (x + (this.getScaledWidth() / (2 * scale)) - this.getScaledWidth() / 2);
    }


    public int getScaledY() {
        return (int) (x + (this.getScaledHeight() / (2 * scale)) - this.getScaledHeight() / 2);
    }

    public int getScaledWidth() {
        return (int) (this.width * scale);
    }

    public int getScaledHeight() {
        return (int) (this.height * scale);
    }

    @Override
    public void render(PoseStack poseStack, int i, int j, float f) {
        if (this.visible) {
            this.isHovered = i >= this.getScaledX() && j >= this.getScaledY() && i < this.getScaledX() + this.getScaledWidth() && j < this.getScaledY() + this.getScaledHeight();
            this.renderButton(poseStack, i, j, f);
        }
    }

    @Override
    public void renderButton(PoseStack poseStack, int i, int j, float f) {
        super.renderButton(poseStack, i, j, f);
        if (this.isHovered()) {
            this.renderToolTip(poseStack, i, j);
        }
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        i *= scale;
        j *= scale;
        return super.keyPressed(i, j, k);
    }

}
