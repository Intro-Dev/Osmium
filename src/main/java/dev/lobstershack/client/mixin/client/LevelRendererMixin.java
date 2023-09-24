package dev.lobstershack.client.mixin.client;

import dev.lobstershack.client.config.Options;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import org.jetbrains.annotations.Nullable;
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
}


