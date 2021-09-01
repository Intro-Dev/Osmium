package com.intro.client.render.cape;

import com.intro.client.OsmiumClient;
import com.intro.client.render.texture.DynamicAnimation;
import com.intro.common.config.Options;
import com.intro.common.config.options.CapeRenderingMode;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CapeRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public CapeRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

    public void render(PoseStack stack, MultiBufferSource multiBuffer, int light, AbstractClientPlayer entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        try {
            ItemStack itemStack = entity.getItemBySlot(EquipmentSlot.CHEST);
            if(!itemStack.is(Items.ELYTRA) && !entity.isInvisible() && entity.isCapeLoaded() && entity.isModelPartShown(PlayerModelPart.CAPE) && OsmiumClient.options.getEnumOption(Options.CustomCapeMode).variable == CapeRenderingMode.ALL || OsmiumClient.options.getEnumOption(Options.CustomCapeMode).variable == CapeRenderingMode.OPTIFINE){
                stack.pushPose();
                stack.translate(0.0D, 0.0D, 0.125D);
                double d = Mth.lerp(tickDelta, entity.xCloakO, entity.xCloak) - Mth.lerp(tickDelta, entity.xo, entity.getX());
                double e = Mth.lerp(tickDelta, entity.yCloakO, entity.yCloak) - Mth.lerp(tickDelta, entity.yo, entity.getY());
                double f = Mth.lerp(tickDelta, entity.zCloakO, entity.zCloak) - Mth.lerp(tickDelta, entity.zo, entity.getZ());
                float g = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO);
                double h = Mth.sin(g * 0.017453292F);
                double i = -Mth.cos(g * 0.017453292F);
                float j = (float)e * 10.0F;
                j = Mth.clamp(j, -6.0F, 32.0F);
                float k = (float)(d * h + f * i) * 100.0F;

                k = Mth.clamp(k, 0.0F, 150.0F);
                float l = (float)(d * i - f * h) * 100.0F;

                l = Mth.clamp(l, -20.0F, 20.0F);
                if (k < 0.0F) {
                    k = 0.0F;
                }

                float m = Mth.lerp(tickDelta, entity.oBob, entity.bob);
                j += Mth.sin(Mth.lerp(tickDelta, entity.walkDistO, entity.walkDist) * 6.0F) * 32.0F * m;
                if (entity.isCrouching()) {
                    j += 25.0F;
                }

                stack.mulPose(Vector3f.XP.rotationDegrees(6.0F + k / 2.0F + j));
                stack.mulPose(Vector3f.ZP.rotationDegrees(l / 2.0F));
                stack.mulPose(Vector3f.YP.rotationDegrees(180.0F - l / 2.0F));


                if(CapeHandler.capes.get(entity.getStringUUID()) != null) {
                    Cape playerCape = CapeHandler.capes.get(entity.getStringUUID());


                    if (OsmiumClient.options.getEnumOption(Options.CustomCapeMode).variable == CapeRenderingMode.OPTIFINE && playerCape.isOptifine) {
                        final VertexConsumer vertexConsumer = multiBuffer.getBuffer(RenderType.entitySolid(playerCape.texture.getAnimationLocation()));
                        this.getParentModel().renderCloak(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
                    } else if(OsmiumClient.options.getEnumOption(Options.CustomCapeMode).variable == CapeRenderingMode.ALL) {
                        final VertexConsumer vertexConsumer = multiBuffer.getBuffer(RenderType.entitySolid(playerCape.texture.getAnimationLocation()));
                        this.getParentModel().renderCloak(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
                    } else {
                        DynamicAnimation capeAnimation = CapeHandler.capes.get(entity.getStringUUID()).texture;
                        final VertexConsumer vertexConsumer = multiBuffer.getBuffer(RenderType.entitySolid(capeAnimation.getAnimationLocation()));
                        // NO_OVERLAY offsets for uv
                        this.getParentModel().renderCloak(stack, vertexConsumer, light, capeAnimation.getPackedAnimationUV());
                        capeAnimation.nextFrame();
                    }
                }

                stack.popPose();
                Minecraft.getInstance().getProfiler().pop();
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

