package com.intro.common.mixin.client;

import com.intro.client.render.color.Colors;
import com.intro.client.util.DebugUtil;
import com.intro.client.util.HypixelAbstractionLayer;
import com.intro.common.config.Options;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private EntityRenderDispatcher entityRenderDispatcher;
    @Shadow @Nullable private Frustum capturedFrustum;
    @Shadow @Final private RenderBuffers renderBuffers;

    @Shadow @Final private Vector3d frustumPos;

    @Shadow private Frustum cullingFrustum;

    @Inject(at = @At("HEAD"), method = "renderSnowAndRain", cancellable = true)
    public void RenderWeather(CallbackInfo info) {
        if(Options.NoRainEnabled.get()) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)V", cancellable = true)
    public void addParticle(ParticleOptions particleData, boolean ignoreRange, boolean minimizeLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfo ci) {
        if(particleData == ParticleTypes.RAIN && Options.NoRainEnabled.get()) {
            ci.cancel();
        }
        if(particleData == ParticleTypes.FIREWORK && Options.FireworksDisabled.get()) {
            ci.cancel();
        }
        if((particleData == ParticleTypes.WARPED_SPORE || particleData == ParticleTypes.CRIMSON_SPORE || particleData == ParticleTypes.SPORE_BLOSSOM_AIR || particleData == ParticleTypes.FALLING_SPORE_BLOSSOM) && Options.DecreaseNetherParticles.get()) {
            ci.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "renderLevel")
    public void postRenderLevel(PoseStack stack, float partialTicks, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        // takes gpu usage down from like 40% in highly populated areas
        Frustum usedFrustum;
        if(capturedFrustum != null) {
            usedFrustum = capturedFrustum;
            usedFrustum.prepare(frustumPos.x, this.frustumPos.y, this.frustumPos.z);
        } else {
            usedFrustum = cullingFrustum;
        }
        MultiBufferSource.BufferSource source = this.renderBuffers.bufferSource();
        assert minecraft.level != null;
        for(Player player : minecraft.level.players()) {
            if(!HypixelAbstractionLayer.isPlayerNpc(player)) {
                if (DebugUtil.DEBUG || (Minecraft.getInstance().getCurrentServer() != null && entityRenderDispatcher.shouldRender(player, usedFrustum, minecraft.gameRenderer.getMainCamera().getPosition().x,  minecraft.gameRenderer.getMainCamera().getPosition().y,  minecraft.gameRenderer.getMainCamera().getPosition().z))) {
                    if (DebugUtil.DEBUG || Minecraft.getInstance().getCurrentServer().ip.contains("hypixel.net")) {
                        stack.pushPose();
                        {
                            Vec3 vec3 = entityRenderDispatcher.getRenderer(player).getRenderOffset(player, partialTicks);
                            double interpolatedX = Mth.lerp(partialTicks, player.xOld, player.getX()) - camera.getPosition().x + vec3.x();
                            double interpolatedY = Mth.lerp(partialTicks, player.yOld, player.getY()) - camera.getPosition().y + vec3.y();
                            double interpolatedZ = Mth.lerp(partialTicks, player.zOld, player.getZ()) - camera.getPosition().z + vec3.z();
                            stack.translate(interpolatedX, interpolatedY, interpolatedZ);
                            if (Options.LevelHeadEnabled.get()) {
                                stack.pushPose();
                                stack.translate(0, 0.25, 0);
                                try {
                                    renderCustomColorNameTag(player, Component.literal(Component.translatable("osmium.level").getString() + HypixelAbstractionLayer.getPlayerLevel(player.getStringUUID())), stack, source, this.entityRenderDispatcher.getPackedLightCoords(player, partialTicks), Colors.ORANGE.getColor().getInt());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                stack.popPose();
                            }
                        }
                        stack.popPose();
                    }
                }
            }
        }
        source.endBatch();
    }

    private void renderCustomColorNameTag(Player entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int color) {
        double distance = this.entityRenderDispatcher.distanceToSqr(entity);
        if (!(distance > 4096.0D)) {
            boolean discrete = !entity.isDiscrete();
            float entityHeight = entity.getBbHeight() + 0.5F;
            int deadmausOffset = "deadmau5".equals(component.getString()) ? -10 : 0;
            poseStack.pushPose();
            poseStack.translate(0.0D, entityHeight, 0.0D);
            poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
            poseStack.scale(-0.025F, -0.025F, 0.025F);
            Matrix4f matrix4f = poseStack.last().pose();
            float backgroundOpacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
            int bitShiftedOpacity = (int)(backgroundOpacity * 255.0F) << 24;
            Font font = minecraft.font;
            float h = (float)(-font.width(component) / 2);
            if (discrete) {
                // use default if player is behind a wall to not make sudo-wallhack
                font.drawInBatch(component, h, (float)deadmausOffset, color, false, matrix4f, multiBufferSource, false, 0, light);
            } else {
                font.drawInBatch(component, h, (float)deadmausOffset, -1, false, matrix4f, multiBufferSource, false, bitShiftedOpacity, light);
            }
            poseStack.popPose();
        }
    }



}


