package dev.lobstershack.client.render.widget.drawables;

import com.google.common.collect.Ordering;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.render.color.Colors;
import dev.lobstershack.common.config.Options;
import dev.lobstershack.common.config.options.StatusEffectDisplayMode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;

import java.util.List;

public class StatusEffectDisplay extends Scalable {

    public StatusEffectDisplay() {
        super(Options.StatusEffectDisplayPosition);
        this.maxEffectsDisplayed = Options.MaxStatusEffectsDisplayed.get().byteValue();
    }


    private final Minecraft mc = Minecraft.getInstance();

    private static StatusEffectDisplay instance;

    public int maxEffectsDisplayed;

    @Override
    public void render(PoseStack stack) {
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
                        String formattedTime = MobEffectUtil.formatDuration(effect, 1.0F).getString();

                        String messageText = Component.translatable(effect.getEffect().getDescriptionId()).getString() + (" " + (effect.getAmplifier() + 1) + ", " + formattedTime);

                        stack.pushPose();
                        {
                            RenderSystem.setShaderTexture(0, sprite.atlasLocation());
                            blit(stack, this.posX, this.posY + offY, 0, 32,32, sprite);
                            drawCenteredString(stack, mc.font, messageText, this.posX + width / 2, this.posY + offY + 40, Colors.WHITE.getColor().getInt());
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

}
