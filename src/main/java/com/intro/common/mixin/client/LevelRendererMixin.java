package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.client.render.color.Color;
import com.intro.client.util.RenderUtil;
import com.intro.common.config.Options;
import com.intro.common.config.options.BlockOutlineMode;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {


    @Shadow private ClientLevel level;


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

    /**
     * @author Intro
     * @reason use custom outline drawing with improved compatibility
     */
    @Overwrite
    private void renderHitOutline(PoseStack stack, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState) {
        if(OsmiumClient.options.getEnumOption(Options.BlockOutlineMode).get() == BlockOutlineMode.LINES) {
            drawShapeOutline(stack, vertexConsumer, blockState.getShape(entity.level, blockPos, CollisionContext.of(entity)), (double)blockPos.getX() - d, (double)blockPos.getY() - e, (double)blockPos.getZ() - f, OsmiumClient.options.getColorOption(Options.BlockOutlineColor).get());
        } else if(OsmiumClient.options.getEnumOption(Options.BlockOutlineMode).get() == BlockOutlineMode.QUADS) {
            drawShapeFull(stack, blockState.getShape(entity.level, blockPos, CollisionContext.of(entity)), (double)blockPos.getX() - d, (double)blockPos.getY() - e, (double)blockPos.getZ() - f, OsmiumClient.options.getColorOption(Options.BlockOutlineColor).get());
        } else {
            drawShapeOutline(stack, vertexConsumer, blockState.getShape(this.level, blockPos, CollisionContext.of(entity)), (double)blockPos.getX() - d, (double)blockPos.getY() - e, (double)blockPos.getZ() - f, new Color(0.0F, 0.0F, 0.0F, 0.4F));
        }
    }

    private void drawShapeOutline(PoseStack stack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double x, double y, double z, Color color) {
        PoseStack.Pose pose = stack.last();
        voxelShape.forAllEdges((edgeX1, edgeY1, edgeZ1, edgeX2, edgeY2, edgeZ2) -> {
            float edgeXDiff = (float)(edgeX2 - edgeX1);
            float edgeYDiff = (float)(edgeY2 - edgeY1);
            float edgeZDiff = (float)(edgeZ2 - edgeZ1);
            float pythagorean = Mth.sqrt(edgeXDiff * edgeXDiff + edgeYDiff * edgeYDiff + edgeZDiff * edgeZDiff);
            edgeXDiff /= pythagorean;
            edgeYDiff /= pythagorean;
            edgeZDiff /= pythagorean;
            vertexConsumer.vertex(pose.pose(), (float)(edgeX1 + x), (float)(edgeY1 + y), (float)(edgeZ1 + z)).color(color.getFloatR(), color.getFloatG(), color.getFloatB(), (float) OsmiumClient.options.getDoubleOption(Options.BlockOutlineAlpha).get().byteValue()).normal(pose.normal(), edgeXDiff, edgeYDiff, edgeZDiff).endVertex();
            vertexConsumer.vertex(pose.pose(), (float)(edgeX2 + x), (float)(edgeY2 + y), (float)(edgeZ2 + z)).color(color.getFloatR(), color.getFloatG(), color.getFloatB(), (float) OsmiumClient.options.getDoubleOption(Options.BlockOutlineAlpha).get().byteValue()).normal(pose.normal(), edgeXDiff, edgeYDiff, edgeZDiff).endVertex();
        });
    }

    private void drawShapeFull(PoseStack stack, VoxelShape voxelShape, double x, double y, double z, Color color) {
        stack.pushPose();
        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR);
        renderVoxelShape(stack, builder, voxelShape, x, y, z, color.getFloatR(), color.getFloatG(), color.getFloatB(), OsmiumClient.options.getDoubleOption(Options.BlockOutlineAlpha).get().floatValue(), 0.01d);
        BufferBuilder.RenderedBuffer renderedBuffer = builder.end();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        BufferUploader.drawWithShader(renderedBuffer);
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
        stack.popPose();
    }

    public void renderVoxelShape(PoseStack stack, VertexConsumer vertexConsumer, VoxelShape voxelShape, double x, double y, double z, float r, float g, float b, float a, double expand) {
        List<AABB> list = voxelShape.toAabbs();
        for (AABB aABB : list) {
            aABB = aABB.inflate(expand);
            RenderUtil.addChainedFilledBoxVertices(stack, vertexConsumer, (float) x, (float) y, (float) z, (float) aABB.minX, (float) aABB.minY, (float) aABB.minZ, (float) aABB.maxX, (float) aABB.maxY, (float) aABB.maxZ, r, g, b, a);
        }
    }

}


