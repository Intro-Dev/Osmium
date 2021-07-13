package com.intro.mixin;

import com.intro.Osmium;
import com.intro.config.BooleanOption;
import com.intro.config.OptionUtil;
import com.intro.render.Color;
import com.intro.render.Colors;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
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
        if(parameters == ParticleTypes.FIREWORK && (((BooleanOption) Osmium.options.FireworksDisabled.get()).variable)) {
            ci.cancel();
        }
        if((parameters == ParticleTypes.WARPED_SPORE || parameters == ParticleTypes.CRIMSON_SPORE || parameters == ParticleTypes.SPORE_BLOSSOM_AIR || parameters == ParticleTypes.FALLING_SPORE_BLOSSOM) && (((BooleanOption) Osmium.options.DecreaseNetherParticles.get()).variable)) {
            ci.cancel();
        }
    }

    @Inject(method = "drawBlockOutline", at = @At("HEAD"), cancellable = true)
    public void renderBlockOutline(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if(Osmium.options.getBooleanOption(Osmium.options.CustomBlockOutline.identifier).variable) {
            drawShapeOutline(matrices, vertexConsumer, blockState.getOutlineShape(entity.world, blockPos, ShapeContext.of(entity)), (double)blockPos.getX() - d, (double)blockPos.getY() - e, (double)blockPos.getZ() - f, Colors.RED.getColor());
            ci.cancel();
        }
    }

    private static void drawShapeOutline(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, Color color) {
        net.minecraft.client.util.math.MatrixStack.Entry entry = matrices.peek();
        voxelShape.forEachEdge((k, l, m, n, o, p) -> {
            float q = (float)(n - k);
            float r = (float)(o - l);
            float s = (float)(p - m);
            float t = MathHelper.sqrt(q * q + r * r + s * s);
            q /= t;
            r /= t;
            s /= t;
            vertexConsumer.vertex(entry.getModel(), (float)(k + d), (float)(l + e), (float)(m + f)).color(color.getFloatR(), color.getFloatG(), color.getFloatB(), color.getFloatA()).normal(entry.getNormal(), q, r, s).next();
            vertexConsumer.vertex(entry.getModel(), (float)(n + d), (float)(o + e), (float)(p + f)).color(color.getFloatR(), color.getFloatG(), color.getFloatB(), color.getFloatA()).normal(entry.getNormal(), q, r, s).next();
        });
    }

}
