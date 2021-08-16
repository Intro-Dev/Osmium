package com.intro.client.render.drawables;

import com.google.common.collect.Ordering;
import com.intro.client.OsmiumClient;
import com.intro.client.render.Colors;
import com.intro.client.render.RenderManager;
import com.intro.client.render.renderer.BatchRenderer2d;
import com.intro.common.config.options.DoubleOption;
import com.intro.common.config.options.EnumOption;
import com.intro.common.config.options.StatusEffectDisplayMode;
import com.intro.common.config.options.Vector2Option;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.MobEffectTextureManager;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class StatusEffectDisplay extends Drawable {

    public StatusEffectDisplay() {
        this.posX = (int) ((Vector2Option) OsmiumClient.options.get(OsmiumClient.options.StatusEffectDisplayPosition.identifier)).x;
        this.posY = (int) ((Vector2Option) OsmiumClient.options.get(OsmiumClient.options.StatusEffectDisplayPosition.identifier)).y;
        this.maxEffectsDisplayed = (int) ((DoubleOption) OsmiumClient.options.get(OsmiumClient.options.MaxStatusEffectsDisplayed.identifier)).variable;
    }


    private final Minecraft mc = Minecraft.getInstance();

    private static StatusEffectDisplay instance;

    public float scale = 1f;

    public int maxEffectsDisplayed = 0;

    @Override
    public void render(PoseStack stack) {
        if(mc.player != null && (((EnumOption) OsmiumClient.options.get(OsmiumClient.options.StatusEffectDisplayMode.identifier)).variable == StatusEffectDisplayMode.CUSTOM || ((EnumOption) OsmiumClient.options.get(OsmiumClient.options.StatusEffectDisplayMode.identifier)).variable == StatusEffectDisplayMode.BOTH)) {
            MobEffectTextureManager spriteManager = mc.getMobEffectTextures();

            OsmiumClient.options.put(OsmiumClient.options.StatusEffectDisplayPosition.identifier, new Vector2Option("StatusEffectDisplayPosition", this.posX, this.posY));

            stack.pushPose();
            {
                int offY = 0;
                List<MobEffectInstance> effects = mc.player.getActiveEffects().stream().toList();
                effects = Ordering.natural().reverse().sortedCopy(effects);


                this.width = (int) (32 * scale);
                this.height = (int) ((effects.size() * 56 * scale) + (40 * scale));

                // use batch rendering for sprites
                // can improve rendering performance
                BatchRenderer2d renderer = new BatchRenderer2d();
                renderer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
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
                    if (effect.isAmbient()) {
                        formattedTime = "∞";
                    } else {
                        formattedTime = mins + ":" + formattedSeconds;
                    }

                    String messageText = new TranslatableComponent(effect.getEffect().getDescriptionId()).getString() + (" " + (effect.getAmplifier() + 1) + ", " + formattedTime);



                    stack.pushPose();
                    {
                        RenderSystem.setShaderTexture(0, sprite.atlas().getId());
                        renderer.drawSprite(stack, this.posX, (int) (this.posY + (offY * scale)),this.getBlitOffset(), (int) (32 * scale), (int) (32 * scale), sprite);
                        drawCenteredString(stack, mc.font, messageText, this.posX, (int) (this.posY + (offY * scale) + (40 * scale)), Colors.WHITE.getColor().getInt());
                    }
                    stack.popPose();

                    offY += 56;
                }
                renderer.end();
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