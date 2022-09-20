package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.common.config.Options;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

    @Shadow @Final private Minecraft minecraft;
    private BufferBuilder.RenderedBuffer lastFrameFrameBuffer;

    @Inject(at = @At("HEAD"), method = "renderSnowAndRain", cancellable = true)
    public void RenderWeather(CallbackInfo info) {
        if(OsmiumClient.options.getBooleanOption(Options.NoRainEnabled).get()) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)V", cancellable = true)
    public void addParticle(ParticleOptions particleData, boolean ignoreRange, boolean minimizeLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfo ci) {
        if(particleData == ParticleTypes.RAIN && OsmiumClient.options.getBooleanOption(Options.NoRainEnabled).get()) {
            ci.cancel();
        }
        if(particleData == ParticleTypes.FIREWORK && OsmiumClient.options.getBooleanOption(Options.FireworksDisabled).get()) {
            ci.cancel();
        }
        if((particleData == ParticleTypes.WARPED_SPORE || particleData == ParticleTypes.CRIMSON_SPORE || particleData == ParticleTypes.SPORE_BLOSSOM_AIR || particleData == ParticleTypes.FALLING_SPORE_BLOSSOM) && OsmiumClient.options.getBooleanOption(Options.DecreaseNetherParticles).get()) {
            ci.cancel();
        }
    }

    @Inject(at = @At("TAIL"), method = "renderLevel")
    public void postRenderLevel(PoseStack poseStack, float partialTick, long finishNanoTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f projectionMatrix, CallbackInfo ci) {
        // ShaderSystem.useShader(ShaderSystem.getOrCreateShader(new ResourceLocation("osmium", "/shaders/motion_blur/vert.glsl"), new ResourceLocation("osmium", "/shaders/motion_blur/frag.glsl")));
    }



}


