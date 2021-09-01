package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.client.render.color.Color;
import com.intro.common.config.Options;
import com.intro.common.config.options.BlockOutlineMode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {


    @Inject(at = @At("HEAD"), method = "renderSnowAndRain", cancellable = true)
    public void RenderWeather(CallbackInfo info) {
        if(OsmiumClient.options.getBooleanOption(Options.NoRainEnabled).variable) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)V", cancellable = true)
    public void addParticle(ParticleOptions particleData, boolean ignoreRange, boolean minimizeLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfo ci) {
        if(particleData == ParticleTypes.RAIN && OsmiumClient.options.getBooleanOption(Options.NoRainEnabled).variable) {
            ci.cancel();
        }
        if(particleData == ParticleTypes.FIREWORK && OsmiumClient.options.getBooleanOption(Options.FireworksDisabled).variable) {
            ci.cancel();
        }
        if((particleData == ParticleTypes.WARPED_SPORE || particleData == ParticleTypes.CRIMSON_SPORE || particleData == ParticleTypes.SPORE_BLOSSOM_AIR || particleData == ParticleTypes.FALLING_SPORE_BLOSSOM) && OsmiumClient.options.getBooleanOption(Options.DecreaseNetherParticles).variable) {
            ci.cancel();
        }
    }

    @Inject(method = "renderHitOutline", at = @At("HEAD"), cancellable = true)
    public void renderBlockOutline(PoseStack stack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if(OsmiumClient.options.getEnumOption(Options.BlockOutlineMode).variable == BlockOutlineMode.LINES) {
            drawShapeOutline(stack, vertexConsumer, blockState.getShape(entity.level, blockPos, CollisionContext.of(entity)), (double)blockPos.getX() - d, (double)blockPos.getY() - e, (double)blockPos.getZ() - f, OsmiumClient.options.getColorOption(Options.BlockOutlineColor).color);
            ci.cancel();
        }
    }

    private void drawShapeOutline(PoseStack stack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double x, double y, double z, Color color) {
        PoseStack.Pose pose = stack.last();
        voxelShape.forAllEdges((k, l, m, n, o, p) -> {
            float q = (float)(n - k);
            float r = (float)(o - l);
            float s = (float)(p - m);
            float t = Mth.sqrt(q * q + r * r + s * s);
            q /= t;
            r /= t;
            s /= t;
            RenderSystem.lineWidth(20);
            vertexConsumer.vertex(pose.pose(), (float)(k + x), (float)(l + y), (float)(m + z)).color(color.getFloatR(), color.getFloatG(), color.getFloatB(), (float) OsmiumClient.options.getDoubleOption(Options.BlockOutlineAlpha).variable).normal(pose.normal(), q, r, s).endVertex();
            vertexConsumer.vertex(pose.pose(), (float)(n + x), (float)(o + y), (float)(p + z)).color(color.getFloatR(), color.getFloatG(), color.getFloatB(), (float) OsmiumClient.options.getDoubleOption(Options.BlockOutlineAlpha).variable).normal(pose.normal(), q, r, s).endVertex();
        });
    }

}


