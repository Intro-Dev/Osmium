package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.client.render.Color;
import com.intro.client.util.OptionUtil;
import com.intro.common.config.options.BlockOutlineMode;
import com.intro.common.config.options.BooleanOption;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {


    @Shadow @Final private RenderBuffers renderBuffers;

    private final ResourceLocation BAKED_TEXTURE = new ResourceLocation("osmium", "/textures/gui/gradient.png");

    private Minecraft mc = Minecraft.getInstance();


    @Inject(at = @At("HEAD"), method = "renderSnowAndRain", cancellable = true)
    public void RenderWeather(CallbackInfo info) {
        if(((BooleanOption) OptionUtil.Options.NoRainEnabled.get()).variable) {
            info.cancel();
        }
    }

    @Inject(at = @At("HEAD"), method = "addParticle(Lnet/minecraft/core/particles/ParticleOptions;ZZDDDDDD)V", cancellable = true)
    public void addParticle(ParticleOptions particleData, boolean ignoreRange, boolean minimizeLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfo ci) {
        if(particleData == ParticleTypes.RAIN && (((BooleanOption) OsmiumClient.options.NoRainEnabled.get()).variable)) {
            ci.cancel();
        }
        if(particleData == ParticleTypes.FIREWORK && (((BooleanOption) OsmiumClient.options.FireworksDisabled.get()).variable)) {
            ci.cancel();
        }
        if((particleData == ParticleTypes.WARPED_SPORE || particleData == ParticleTypes.CRIMSON_SPORE || particleData == ParticleTypes.SPORE_BLOSSOM_AIR || particleData == ParticleTypes.FALLING_SPORE_BLOSSOM) && (((BooleanOption) OsmiumClient.options.DecreaseNetherParticles.get()).variable)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderHitOutline", at = @At("HEAD"), cancellable = true)
    public void renderBlockOutline(PoseStack stack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        if(OsmiumClient.options.getEnumOption(OsmiumClient.options.BlockOutlineMode.identifier).variable == BlockOutlineMode.LINES) {
            drawShapeOutline(stack, vertexConsumer, blockState.getShape(entity.level, blockPos, CollisionContext.of(entity)), (double)blockPos.getX() - d, (double)blockPos.getY() - e, (double)blockPos.getZ() - f, OsmiumClient.options.getColorOption(OsmiumClient.options.BlockOutlineColor.identifier).color);
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
            vertexConsumer.vertex(pose.pose(), (float)(k + x), (float)(l + y), (float)(m + z)).color(color.getFloatR(), color.getFloatG(), color.getFloatB(), (float) OsmiumClient.options.getDoubleOption(OsmiumClient.options.BlockOutlineAlpha.identifier).variable).normal(pose.normal(), q, r, s).endVertex();
            vertexConsumer.vertex(pose.pose(), (float)(n + x), (float)(o + y), (float)(p + z)).color(color.getFloatR(), color.getFloatG(), color.getFloatB(), (float) OsmiumClient.options.getDoubleOption(OsmiumClient.options.BlockOutlineAlpha.identifier).variable).normal(pose.normal(), q, r, s).endVertex();
        });
    }

}


