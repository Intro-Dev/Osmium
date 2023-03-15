package dev.lobstershack.client.render.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.util.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
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
            RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
            RenderSystem.enableBlend();
            RenderSystem.enableDepthTest();
            blitNineSliced(stack, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 20, 4, 200, 20, 0, this.getTextureY());
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            // magic numbers
            // thanks mojang
            int color = this.active ? 16777215 : 10526880;
            RenderUtil.renderScaledText(stack, Minecraft.getInstance().font, this.getMessage().getString(), this.getX() + this.width / 2 - (Minecraft.getInstance().font.width(this.getMessage().getString()) / 2), this.getY() + (this.height - 8) / 2, color | Mth.ceil(this.alpha * 255.0F) << 24, (float) scale);
            if(tooltip != null && this.isHovered) Minecraft.getInstance().screen.renderTooltip(stack, tooltip, mouseX, mouseY);
        }
    }

    private int getTextureY() {
        int i = 1;
        if (!this.active) {
            i = 0;
        } else if (this.isHoveredOrFocused()) {
            i = 2;
        }

        return 46 + i * 20;
    }


}
