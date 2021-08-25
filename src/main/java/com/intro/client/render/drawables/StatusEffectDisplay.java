package com.intro.client.render.drawables;

import com.google.common.collect.Ordering;
import com.intro.client.OsmiumClient;
import com.intro.client.render.Colors;
import com.intro.client.render.RenderManager;
import com.intro.common.config.Options;
import com.intro.common.config.options.StatusEffectDisplayMode;
import com.intro.common.config.options.Vector2Option;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class StatusEffectDisplay extends Scalable {

    public StatusEffectDisplay() {
        this.posX = (int) OsmiumClient.options.getVector2Option(Options.StatusEffectDisplayPosition).x;
        this.posY = (int) OsmiumClient.options.getVector2Option(Options.StatusEffectDisplayPosition).y;
        this.maxEffectsDisplayed = (int) OsmiumClient.options.getDoubleOption(Options.MaxStatusEffectsDisplayed).variable;
    }


    private final Minecraft mc = Minecraft.getInstance();

    private static StatusEffectDisplay instance;

    public int maxEffectsDisplayed;

    @Override
    public void render(PoseStack stack) {
        if(mc.player != null && (OsmiumClient.options.getEnumOption(Options.StatusEffectDisplayMode).variable == StatusEffectDisplayMode.CUSTOM || OsmiumClient.options.getEnumOption(Options.StatusEffectDisplayMode).variable == StatusEffectDisplayMode.BOTH)) {
            MobEffectTextureManager spriteManager = mc.getMobEffectTextures();

            OsmiumClient.options.put(Options.StatusEffectDisplayPosition, new Vector2Option(Options.StatusEffectDisplayPosition, this.posX, this.posY));

            stack.pushPose();
            {
                int offY = 0;
                List<MobEffectInstance> effects = mc.player.getActiveEffects().stream().toList();
                effects = Ordering.natural().reverse().sortedCopy(effects);

                this.width = 32;
                this.height = (effects.size() * 56) + (40);
                this.maxEffectsDisplayed = (int) OsmiumClient.options.getDoubleOption(Options.MaxStatusEffectsDisplayed).variable;

                if(effects.size() != 0) {
                    for(int i = 0; i < effects.size() && i < maxEffectsDisplayed; i++) {

                        MobEffectInstance effect = effects.get(i);
                        TextureAtlasSprite sprite = spriteManager.get(effect.getEffect());

                        int duration = effect.getDuration() / 20;
                        int mins = duration / 60;
                        int seconds = duration % 60;

                        String formattedSeconds;
                        if(seconds < 10) {
                            formattedSeconds = "0" + seconds;
                        } else {
                            formattedSeconds = String.valueOf(seconds);
                        }

                        String formattedTime;
                        if (effect.isNoCounter()) {
                            formattedTime = "∞";
                        } else {
                            formattedTime = mins + ":" + formattedSeconds;
                        }

                        String messageText = new TranslatableComponent(effect.getEffect().getDescriptionId()).getString() + (" " + (effect.getAmplifier() + 1) + ", " + formattedTime);

                        stack.pushPose();
                        {
                            RenderSystem.setShaderTexture(0, sprite.atlas().getId());
                            blit(stack, this.posX, this.posY + offY,this.getBlitOffset(), 32,32, sprite);
                            drawCenteredString(stack, mc.font, messageText, this.posX + width / 2, this.posY + offY + 40, Colors.WHITE.getColor().getInt());
                        }
                        stack.popPose();

                        offY += 56;
                    }
                }
            }
            stack.popPose();
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
