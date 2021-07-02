package com.intro.mixin;

import com.intro.Osmium;
import com.intro.config.BooleanOption;
import com.intro.config.OptionUtil;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {


    @Inject(at = @At("HEAD"), method = "renderWeather", cancellable = true)
    public void RenderWeather(CallbackInfo info) {
        if(((BooleanOption) OptionUtil.Options.NoRainEnabled.get()).variable) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "addParticle(Lnet/minecraft/particle/ParticleEffect;ZZDDDDDD)V", cancellable = true)
    public void addParticle(ParticleEffect parameters, boolean shouldAlwaysSpawn, boolean important, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfo ci) {
        if(parameters == ParticleTypes.RAIN && (((BooleanOption) Osmium.options.NoRainEnabled.get()).variable)) {
            ci.cancel();
        }
    }

}
