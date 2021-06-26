package com.intro.render;

import com.intro.config.CapeRenderingMode;
import com.intro.config.EnumOption;
import com.intro.config.OptionUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.HashMap;

public class CapeRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public static HashMap<String, Identifier> CapeArray = new HashMap<>();
    public static ArrayList<String> OptifineCapes = new ArrayList<>();

    public CapeRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    public void render(MatrixStack stack, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        try {

            if(!entity.isInvisible() && entity.canRenderCapeTexture() && entity.isPartVisible(PlayerModelPart.CAPE) && (((EnumOption) OptionUtil.Options.CustomCapeMode.get()).variable == CapeRenderingMode.ALL || ((EnumOption) OptionUtil.Options.CustomCapeMode.get()).variable == CapeRenderingMode.OPTIFINE)){
                MinecraftClient.getInstance().getProfiler().push("OsmiumCapeRender");
                stack.push();
                stack.translate(0.0D, 0.0D, 0.125D);
                final double d = MathHelper.lerp(tickDelta, entity.prevCapeX, entity.capeX) - MathHelper.lerp(tickDelta, entity.prevX, entity.getX());
                final double e = MathHelper.lerp(tickDelta, entity.prevCapeY, entity.capeY) - MathHelper.lerp(tickDelta, entity.prevY, entity.getY());
                final double m = MathHelper.lerp(tickDelta, entity.prevCapeZ, entity.capeZ) - MathHelper.lerp(tickDelta, entity.prevZ, entity.getZ());
                final float n = entity.prevBodyYaw + (entity.bodyYaw - entity.prevBodyYaw);
                final double o = MathHelper.sin(n * 0.017453292F);
                final double p = -MathHelper.cos(n * 0.017453292F);
                float q = (float)e * 10.0F;
                q = MathHelper.clamp(q, -6.0F, 32.0F);
                float r = (float)(d * o + m * p) * 100.0F;
                r = MathHelper.clamp(r, 0.0F, 150.0F);
                float s = (float)(d * p - m * o) * 100.0F;
                s = MathHelper.clamp(s, -20.0F, 20.0F);
                if (r < 0.0F) {
                    r = 0.0F;
                }
                final float t = MathHelper.lerp(tickDelta, entity.prevStrideDistance, entity.strideDistance);
                q += MathHelper.sin(MathHelper.lerp(tickDelta, entity.prevHorizontalSpeed, entity.horizontalSpeed) * 6.0F) * 32.0F * t;
                if (entity.isInSneakingPose()) {
                    q += 25.0F;
                }

                stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(6.0F + r / 2.0F + q));
                stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(s / 2.0F));
                stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - s / 2.0F));
                if(entity.getUuidAsString() != null && (CapeArray.get(entity.getUuidAsString()) != null)) {
                    if(((EnumOption) OptionUtil.Options.CustomCapeMode.get()).variable == CapeRenderingMode.OPTIFINE && OptifineCapes.contains(entity.getUuidAsString())) {
                        final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(CapeArray.get(entity.getUuidAsString())));
                        this.getContextModel().renderCape(stack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
                    } else if (((EnumOption) OptionUtil.Options.CustomCapeMode.get()).variable == CapeRenderingMode.ALL){
                        System.out.println(CapeArray.get(entity.getUuidAsString()));
                        final VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntitySolid(CapeArray.get(entity.getUuidAsString())));
                        this.getContextModel().renderCape(stack, vertexConsumer, light, OverlayTexture.DEFAULT_UV);
                    }

                }
                MinecraftClient.getInstance().getProfiler().pop();
                stack.pop();
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

