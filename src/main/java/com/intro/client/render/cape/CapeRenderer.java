package com.intro.client.render.cape;

import com.intro.client.OsmiumClient;
import com.intro.common.config.Options;
import com.intro.common.config.options.CapeRenderingMode;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.Objects;

public class CapeRenderer extends RenderLayer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {

    public CapeRenderer(RenderLayerParent<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> parent) {
        super(parent);
    }

        public void render(PoseStack stack, MultiBufferSource multiBuffer, int light, AbstractClientPlayer entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        try {
            ItemStack itemStack = entity.getItemBySlot(EquipmentSlot.CHEST);
            if(OsmiumClient.options.getBooleanOption(Options.ShowOtherPlayersCapes).get() && !Objects.equals(entity.getStringUUID(), Minecraft.getInstance().player.getStringUUID())) {
                return;
            }

            if(!itemStack.is(Items.ELYTRA) && !entity.isInvisible() && entity.isCapeLoaded() && entity.isModelPartShown(PlayerModelPart.CAPE) && OsmiumClient.options.getEnumOption(Options.CustomCapeMode).get() == CapeRenderingMode.ALL || OsmiumClient.options.getEnumOption(Options.CustomCapeMode).get() == CapeRenderingMode.OPTIFINE){
                Minecraft.getInstance().getProfiler().push("OsmiumCapeRendering");
                stack.pushPose();
                stack.translate(0.0D, 0.0D, 0.125D);
                double d = Mth.lerp(tickDelta, entity.xCloakO, entity.xCloak) - Mth.lerp(tickDelta, entity.xo, entity.getX());
                double e = Mth.lerp(tickDelta, entity.yCloakO, entity.yCloak) - Mth.lerp(tickDelta, entity.yo, entity.getY());
                double m = Mth.lerp(tickDelta, entity.zCloakO, entity.zCloak) - Mth.lerp(tickDelta, entity.zo, entity.getZ());
                float n = entity.yBodyRotO + (entity.yBodyRot - entity.yBodyRotO);
                double o = Mth.sin(n * 0.017453292F);
                double p = -Mth.cos(n * 0.017453292F);
                float q = (float)e * 10.0F;
                q = Mth.clamp(q, -6.0F, 32.0F);
                float r = (float)(d * o + m * p) * 100.0F;
                r = Mth.clamp(r, 0.0F, 150.0F);
                float s = (float)(d * p - m * o) * 100.0F;
                s = Mth.clamp(s, -20.0F, 20.0F);
                if (r < 0.0F) {
                    r = 0.0F;
                }

                float t = Mth.lerp(tickDelta, entity.oBob, entity.bob);
                q += Mth.sin(Mth.lerp(tickDelta, entity.walkDistO, entity.walkDist) * 6.0F) * 32.0F * t;
                if (entity.isCrouching()) {
                    q += 25.0F;
                }

                stack.mulPose(Vector3f.XP.rotationDegrees(6.0F + r / 2.0F + q));
                stack.mulPose(Vector3f.ZP.rotationDegrees(s / 2.0F));
                stack.mulPose(Vector3f.YP.rotationDegrees(180.0F - s / 2.0F));


                if(CosmeticManager.playerCapes.get(entity.getStringUUID()) != null) {
                    RenderSystem.enableDepthTest();
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    Cape playerCape = CosmeticManager.playerCapes.get(entity.getStringUUID());
                    ResourceLocation capeTexture = playerCape.getFrameTexture();
                    // check if cape texture is null
                    // used to not crash until I optimised sub imaging
                    // now it creates the texture so fast the render thread can't keep up
                    if(capeTexture != null) {
                        RenderType capeRenderType = RenderType.entitySolid(capeTexture);
                        if (OsmiumClient.options.getEnumOption(Options.CustomCapeMode).get() == CapeRenderingMode.OPTIFINE && playerCape.isOptifine) {
                            final VertexConsumer vertexConsumer = multiBuffer.getBuffer(capeRenderType);
                            // the way mojang renders capes is horrifically inefficient
                            // and the thing is it would require a custom implementation of half the rendering engine to do it any other way
                            this.getParentModel().renderCloak(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
                        } else if(OsmiumClient.options.getEnumOption(Options.CustomCapeMode).get() == CapeRenderingMode.ALL) {
                            final VertexConsumer vertexConsumer = multiBuffer.getBuffer(capeRenderType);
                            this.getParentModel().renderCloak(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY);
                        }
                    }
                    RenderSystem.disableDepthTest();
                }

                stack.popPose();
                Minecraft.getInstance().getProfiler().pop();
        }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

