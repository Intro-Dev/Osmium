package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.client.render.cape.CapeRenderer;
import com.intro.client.render.cape.ElytraRenderer;
import com.intro.client.render.color.Colors;
import com.intro.client.util.HypixelAbstractionLayer;
import com.intro.common.config.Options;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public PlayerRendererMixin(EntityRendererProvider.Context ctx, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = ("<init>"), at = @At("RETURN"))
    public void ConstructorMixinPlayerEntityRenderer(EntityRendererProvider.Context ctx, boolean slim, CallbackInfo ci) {
        this.addLayer(new CapeRenderer(this));
        this.addLayer(new ElytraRenderer<>(this, ctx.getModelSet()));
    }

    @Inject(method = "renderNameTag", at = @At(value = "INVOKE", target = "net/minecraft/client/renderer/entity/LivingEntityRenderer.renderNameTag(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/network/chat/Component;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", ordinal = 1))
    public void renderLevelHead(AbstractClientPlayer abstractClientPlayer, Component component, PoseStack stack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {

        if (Minecraft.getInstance().getCurrentServer() != null) {
            if (Minecraft.getInstance().getCurrentServer().ip.contains("hypixel.net")) {
                if (HypixelAbstractionLayer.canUseHypixelService() && OsmiumClient.options.getBooleanOption(Options.LevelHeadEnabled).get()) {
                    stack.pushPose();
                    stack.translate(0, 0.25, 0);
                    try {
                        renderCustomColorNameTag(abstractClientPlayer, new TextComponent(new TranslatableComponent("osmium.level").getString() + HypixelAbstractionLayer.getPlayerLevel(abstractClientPlayer.getStringUUID())), stack, multiBufferSource, i, Colors.ORANGE.getColor().getInt());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    stack.popPose();
                }
            }
        }
    }

    private void renderCustomColorNameTag(Player entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int color) {
        double distance = this.entityRenderDispatcher.distanceToSqr(entity);
        if (!(distance > 4096.0D)) {
            boolean discrete = !entity.isDiscrete();
            float entityHeight = entity.getBbHeight() + 0.5F;
            int deadmauOffset = "deadmau5".equals(component.getString()) ? -10 : 0;
            poseStack.pushPose();
            poseStack.translate(0.0D, entityHeight, 0.0D);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = poseStack.last().pose();
            float backgroundOpacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int bitShiftedOpacity = (int)(backgroundOpacity * 255.0F) << 24;
            Font font = this.getFont();
            float h = (float)(-font.width(component) / 2);
            if (discrete) {
                // use default if player is behind a wall to not make sudo-wallhack
                font.drawInBatch(component, h, (float)deadmauOffset, color, false, matrix4f, multiBufferSource, false, 0, light);
            } else {
                font.drawInBatch(component, h, (float)deadmauOffset, -1, false, matrix4f, multiBufferSource, false, bitShiftedOpacity, light);
            }


            poseStack.popPose();
        }
    }
}
