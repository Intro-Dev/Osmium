package com.intro.render.drawables;

import com.google.common.collect.Ordering;
import com.intro.Osmium;
import com.intro.config.options.DoubleOption;
import com.intro.config.options.EnumOption;
import com.intro.config.options.StatusEffectDisplayMode;
import com.intro.config.options.Vector2Option;
import com.intro.render.Colors;
import com.intro.render.RenderManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.StatusEffectSpriteManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.text.TranslatableText;

import java.util.List;

public class StatusEffectDisplay extends Drawable {

    public StatusEffectDisplay() {
        this.posX = (int) ((Vector2Option) Osmium.options.get(Osmium.options.StatusEffectDisplayPosition.identifier)).x;
        this.posY = (int) ((Vector2Option) Osmium.options.get(Osmium.options.StatusEffectDisplayPosition.identifier)).y;
        this.maxEffectsDisplayed = (int) ((DoubleOption) Osmium.options.get(Osmium.options.MaxStatusEffectsDisplayed.identifier)).variable;
    }


    private final MinecraftClient mc = MinecraftClient.getInstance();

    private static StatusEffectDisplay instance;

    public float scale = 1f;

    public int maxEffectsDisplayed = 0;

    @Override
    public void render(MatrixStack stack) {
        if(mc.player != null && (((EnumOption) Osmium.options.get(Osmium.options.StatusEffectDisplayMode.identifier)).variable == StatusEffectDisplayMode.CUSTOM || ((EnumOption) Osmium.options.get(Osmium.options.StatusEffectDisplayMode.identifier)).variable == StatusEffectDisplayMode.BOTH)) {
            TextRenderer renderer = mc.textRenderer;
            StatusEffectSpriteManager spriteManager = mc.getStatusEffectSpriteManager();

            Osmium.options.put(Osmium.options.StatusEffectDisplayPosition.identifier, new Vector2Option("StatusEffectDisplayPosition", this.posX, this.posY));

            stack.push();
            {
                int offY = 0;
                List<StatusEffectInstance> effects = mc.player.getStatusEffects().stream().toList();
                effects = Ordering.natural().reverse().sortedCopy(effects);


                this.width = (int) (32 * scale);
                this.height = (int) ((effects.size() * 56 * scale) + (40 * scale));

                for(int i = 0; i < effects.size() && i < maxEffectsDisplayed; i++) {
                    StatusEffectInstance effect = effects.get(i);
                    Sprite sprite = spriteManager.getSprite(effect.getEffectType());

                    int duration = effect.getDuration() / 20;
                    int mins = duration / 60;
                    int seconds = duration % 60;

                    String formattedSeconds = "";
                    if(seconds < 10) {
                        formattedSeconds = "0" + seconds;
                    } else {
                        formattedSeconds = String.valueOf(seconds);
                    }

                    String messageText = new TranslatableText(effect.getEffectType().getTranslationKey()).getString() + (", " + mins + ":" + formattedSeconds);
                    if (effect.isPermanent()) messageText = "∞";


                    stack.push();
                    {
                        RenderSystem.setShaderTexture(0, sprite.getAtlas().getId());
                        drawSprite(stack, this.posX, (int) (this.posY + (offY * scale)),0, (int) (32 * scale), (int) (32 * scale), sprite);
                        drawCenteredText(stack, mc.textRenderer, messageText, this.posX + (mc.textRenderer.getWidth(messageText) / 2), (int) (this.posY + (offY * scale) + (40 * scale)), Colors.WHITE.getColor().getInt());
                    }
                    stack.pop();

                    offY += 56;
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
