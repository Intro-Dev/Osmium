package dev.lobstershack.client.render.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

import java.util.concurrent.atomic.AtomicInteger;

public class BackAndForwardWidget extends SimpleWidget {

    private static final ResourceLocation FORWARD_BUTTON_LOCATION = new ResourceLocation("osmium", "/textures/gui/forward_button.png");
    private static final ResourceLocation BACK_BUTTON_LOCATION = new ResourceLocation("osmium", "/textures/gui/back_button.png");


    private final int textureGap;
    private int x, y;
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
        this.textureGap = textureGap;
        this.x = centerX;
        this.y = y;
        this.value = value;
        this.min = min;
        this.max = max;
        this.scale = scale;
    }



    @Override
    public void render(PoseStack stack, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACK_BUTTON_LOCATION);
        blit(stack, x - textureGap, y, 0, 0, (int) (32 * scale), (int) (32 * scale), (int) (32 * scale), (int) (32 * scale));
        RenderSystem.setShaderTexture(0, FORWARD_BUTTON_LOCATION);
        blit(stack, x + textureGap, y, 0, 0, (int) (32 * scale), (int) (32 * scale), (int) (32 * scale), (int) (32 * scale));
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int code) {
        if(code == GLFW.GLFW_MOUSE_BUTTON_1) {
            if(MathUtil.isPositionWithinBounds((int) mouseX, (int) mouseY, x - 32, y, 32, 32)) {
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
        return super.mouseClicked(mouseX, mouseY, code);
    }


}
