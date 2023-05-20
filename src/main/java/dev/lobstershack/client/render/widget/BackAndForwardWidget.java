package dev.lobstershack.client.render.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.lobstershack.client.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicInteger;

public class BackAndForwardWidget extends SimpleWidget {

    private static final ResourceLocation FORWARD_BUTTON_LOCATION = new ResourceLocation("osmium", "/textures/gui/forward_button.png");
    private static final ResourceLocation BACK_BUTTON_LOCATION = new ResourceLocation("osmium", "/textures/gui/back_button.png");


    private final int textureGap;
    private int centerX;
    private AtomicInteger value;

    private int min, max;

    private float scale;

    /**
     *
     * @param centerX
     * @param y
     * @param textureGap
     * @param value
     * @param min
     * @param max
     * @param scale how much the texture should be scaled by
     */
    public BackAndForwardWidget(int centerX, int y, int textureGap, AtomicInteger value, int min, int max, float scale) {
        super((int) (centerX - textureGap - 32 * scale), y, (int) (textureGap + 32 * scale), 32, Component.empty());
        this.textureGap = textureGap;
        this.centerX = centerX;
        this.value = value;
        this.min = min;
        this.max = max;
        this.scale = scale;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        graphics.blit(BACK_BUTTON_LOCATION, centerX - textureGap, this.getY(), 0, 0, (int) (32 * scale), (int) (32 * scale), (int) (32 * scale), (int) (32 * scale));
        graphics.blit(FORWARD_BUTTON_LOCATION, centerX + textureGap, this.getY(), 0, 0, (int) (32 * scale), (int) (32 * scale), (int) (32 * scale), (int) (32 * scale));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int code) {
        if(code == GLFW.GLFW_MOUSE_BUTTON_1) {
            if(MathUtil.isPositionWithinBounds((int) mouseX, (int) mouseY, centerX - 32, this.getY(), 32, 32)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                value.decrementAndGet();
                value.set(Mth.clamp(value.get(), min, max));
            }
            if(MathUtil.isPositionWithinBounds((int) mouseX, (int) mouseY, centerX + textureGap, this.getY(), 32, 32)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                value.incrementAndGet();
                value.set(Mth.clamp(value.get(), min, max));
            }
        }
        return super.mouseClicked(mouseX, mouseY, code);
    }


}
