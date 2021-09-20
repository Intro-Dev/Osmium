package com.intro.client.render.widget;

import com.intro.client.util.MathUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicInteger;

public class BackAndForwardWidget extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {

    private static final ResourceLocation FORWARD_BUTTON_LOCATION = new ResourceLocation("osmium", "/textures/gui/forward_button.png");
    private static final ResourceLocation BACK_BUTTON_LOCATION = new ResourceLocation("osmium", "/textures/gui/back_button.png");


    private final int textureGap;
    private int x, y;
    private AtomicInteger value;

    private int min, max;


    public BackAndForwardWidget(int centerX, int y, int textureGap, AtomicInteger value, int min, int max) {
        this.textureGap = textureGap;
        this.x = centerX;
        this.y = y;
        this.value = value;
        this.min = min;
        this.max = max;
    }



    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACK_BUTTON_LOCATION);
        blit(stack, x - textureGap, y, 0, 0, 32, 32, 32, 32);
        RenderSystem.setShaderTexture(0, FORWARD_BUTTON_LOCATION);
        blit(stack, x + textureGap, y, 0, 0, 32, 32, 32, 32);
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.NONE;
    }


    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int code) {
        if(code == GLFW.GLFW_MOUSE_BUTTON_1) {
            if(MathUtil.isPositionWithinBounds((int) mouseX, (int) mouseY, x, y, 32, 32)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                value.decrementAndGet();
                value.set(Mth.clamp(value.get(), min, max));
            }
            if(MathUtil.isPositionWithinBounds((int) mouseX, (int) mouseY, x + textureGap, y, 32, 32)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                value.incrementAndGet();
                value.set(Mth.clamp(value.get(), min, max));
            }
        }
        return GuiEventListener.super.mouseClicked(mouseX, mouseY, code);
    }
}
