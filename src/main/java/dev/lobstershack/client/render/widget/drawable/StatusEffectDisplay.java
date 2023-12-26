package dev.lobstershack.client.render.widget.drawable;

import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.config.options.StatusEffectDisplayMode;
import dev.lobstershack.client.render.color.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

import java.util.List;

public class StatusEffectDisplay extends Scalable {

    public StatusEffectDisplay() {
        super(Options.StatusEffectDisplayPosition, 32, 40, Component.empty());
        this.maxEffectsDisplayed = Options.MaxStatusEffectsDisplayed.get().byteValue();
    }


    private final Minecraft mc = Minecraft.getInstance();

    private static StatusEffectDisplay instance;

    public int maxEffectsDisplayed;

    @Override
    public void render(GuiGraphics graphics) {
        PoseStack stack = graphics.pose();
        if(mc.player != null && Options.StatusEffectDisplayMode.get() == StatusEffectDisplayMode.CUSTOM ||Options.StatusEffectDisplayMode.get() == StatusEffectDisplayMode.BOTH) {
            this.visible = true;

            MobEffectTextureManager spriteManager = mc.getMobEffectTextures();
            stack.pushPose();
            {
                int offY = 0;
                List<MobEffectInstance> effects = mc.player.getActiveEffects().stream().toList();
                effects = Ordering.natural().reverse().sortedCopy(effects);

                this.width = 32;
                this.height = (effects.size() * 56) + (40);
                this.maxEffectsDisplayed = Options.MaxStatusEffectsDisplayed.get().byteValue();

                if(effects.size() != 0) {
                    for(int i = 0; i < effects.size() && i < maxEffectsDisplayed; i++) {

                        MobEffectInstance effect = effects.get(i);
                        TextureAtlasSprite sprite = spriteManager.get(effect.getEffect());
                        String formattedTime = MobEffectUtil.formatDuration(effect, 1.0F, Minecraft.getInstance().level.tickRateManager().tickrate()).getString();

                        String messageText = Component.translatable(effect.getEffect().getDescriptionId()).getString() + (" " + (effect.getAmplifier() + 1) + ", " + formattedTime);

                        stack.pushPose();
                        {
                            RenderSystem.setShaderTexture(0, sprite.atlasLocation());
                            graphics.blit(this.getX(), this.getY() + offY, 0, 32,32, sprite);
                            graphics.drawCenteredString(mc.font, messageText, this.getX() + width / 2, this.getY() + offY + 40, Colors.WHITE.getColor().getInt());
                        }
                        stack.popPose();

                        offY += 56;
                    }
                }
            }
            stack.popPose();
        } else {
            this.visible = false;
        }

    }


    public static StatusEffectDisplay getInstance() {
        if(instance == null) {
            instance = new StatusEffectDisplay();
            return instance;
        }
        return instance;
    }

    public static void invalidateInstance() {
        instance = null;
    }

}
