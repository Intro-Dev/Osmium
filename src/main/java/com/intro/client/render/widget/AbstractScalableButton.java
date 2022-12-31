package com.intro.client.render.widget;

import com.intro.client.util.RenderUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public class AbstractScalableButton extends Button {

    private double scale;

    public Component getTooltip() {
        return tooltip;
    }

    public void setTooltip(Component tooltip) {
        this.tooltip = tooltip;
    }

    private Component tooltip;

    public AbstractScalableButton(int i, int j, int k, int l, Component component, OnPress onPress) {
        super(i, j, k, l, component, onPress, Button.DEFAULT_NARRATION);
        this.scale = 1f;
    }

    public AbstractScalableButton(int i, int j, int k, int l, Component component, OnPress onPress, double scale) {
        super(i, j, k, l, component, onPress, Button.DEFAULT_NARRATION);
        this.scale = scale;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public AbstractScalableButton(int i, int j, int k, int l, Component component, OnPress onPress, double scale, Component tooltip) {
        super(i, j, k, l, component, onPress, Button.DEFAULT_NARRATION);
        this.tooltip = tooltip;
        this.scale = scale;
    }


    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float tickDelta) {
        if (this.visible) {
            this.isHovered = mouseX >= this.getX() && mouseY >= this.getY() && mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
            int k = this.getYImage(this.isHovered);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.blit(stack, this.getX(), this.getY(), 0, 46 + k * 20, this.width / 2, this.height);
            this.blit(stack, this.getX() + this.width / 2, this.getY(), 200 - this.width / 2, 46 + k * 20, this.width / 2, this.height);
            this.renderBg(stack, minecraft, mouseX, mouseY);
            int l = this.active ? 16777215 : 10526880;
            // scale has to be rounded or text rendering looses precision
            RenderUtil.renderScaledText(stack, font, this.getMessage().getString(), this.getX() + this.width / 2 - (font.width(this.getMessage().getString()) / 2), this.getY() + (this.height - 8) / 2, l | Mth.ceil(this.alpha * 255.0F) << 24, (float) scale);
            if(tooltip != null && this.isHovered) Minecraft.getInstance().screen.renderTooltip(stack, tooltip, mouseX, mouseY);
        }

    }


}
