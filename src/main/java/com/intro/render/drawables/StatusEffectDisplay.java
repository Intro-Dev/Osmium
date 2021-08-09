package com.intro.render.drawables;

import com.intro.Osmium;
import com.intro.config.options.BooleanOption;
import com.intro.config.options.Vector2Option;
import com.intro.render.Colors;
import com.intro.render.RenderManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;

public class StatusEffectDisplay extends Drawable {

    private MinecraftClient mc = MinecraftClient.getInstance();
    private TextRenderer renderer = mc.textRenderer;
    
    private static StatusEffectDisplay instance;

    @Override
    public void render(MatrixStack stack) {
        if(mc.currentScreen == null && mc.player != null &&  ((BooleanOption) Osmium.options.get(Osmium.options.StatusEffectDisplayEnabled.identifier)).variable) {
            this.posX = (int) ((Vector2Option) Osmium.options.get(Osmium.options.StatusEffectDisplayPosition.identifier)).x;
            this.posY = (int) ((Vector2Option) Osmium.options.get(Osmium.options.StatusEffectDisplayPosition.identifier)).y;

            stack.push();
            {
                for(StatusEffectInstance effect : mc.player.getStatusEffects()) {
                    renderer.drawWithShadow(stack, effect.getEffectType().getName(), this.posX, this.posY, Colors.WHITE.getColor().getInt());
                }
            }
            stack.pop();
        }

    }

    @Override
    public void destroySelf() {
        RenderManager.drawables.remove(this);
    }
    
    public static StatusEffectDisplay getInstance() {
        if(instance == null) {
            instance = new StatusEffectDisplay();
            return instance;
        }
        return instance;
    }
}
