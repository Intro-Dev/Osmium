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

        public void render(PoseStack stack, MultiBufferSource multiBuffer, int light, AbstractClientPlayer player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        try {
            ItemStack itemStack = player.getItemBySlot(EquipmentSlot.CHEST);
            if(OsmiumClient.options.getBooleanOption(Options.ShowOtherPlayersCapes).get() && !Objects.equals(player.getStringUUID(), Minecraft.getInstance().player.getStringUUID())) {
                return;
            }

            if(!itemStack.is(Items.ELYTRA) && !player.isInvisible() && player.isCapeLoaded() && player.isModelPartShown(PlayerModelPart.CAPE) && OsmiumClient.options.getEnumOption(Options.CustomCapeMode).get() == CapeRenderingMode.ALL || OsmiumClient.options.getEnumOption(Options.CustomCapeMode).get() == CapeRenderingMode.OPTIFINE){
                Minecraft.getInstance().getProfiler().push("OsmiumCapeRendering");
                stack.pushPose();
                stack.translate(0.0D, 0.0D, 0.125D);
                double d = Mth.lerp((double)tickDelta, player.xCloakO, player.xCloak) - Mth.lerp((double)tickDelta, player.xo, player.getX());
                double e = Mth.lerp((double)tickDelta, player.yCloakO, player.yCloak) - Mth.lerp((double)tickDelta, player.yo, player.getY());
                double f = Mth.lerp((double)tickDelta, player.zCloakO, player.zCloak) - Mth.lerp((double)tickDelta, player.zo, player.getZ());
                float g = player.yBodyRotO + (player.yBodyRot - player.yBodyRotO);
                double h = (double)Mth.sin(g * 0.017453292F);
                double i = (double)(-Mth.cos(g * 0.017453292F));
                float j = (float)e * 10.0F;
                j = Mth.clamp(j, -6.0F, 32.0F);
                float k = (float)(d * h + f * i) * 100.0F;
                k = Mth.clamp(k, 0.0F, 150.0F);
                float l = (float)(d * i - f * h) * 100.0F;
                l = Mth.clamp(l, -20.0F, 20.0F);
                if (k < 0.0F) {
                    k = 0.0F;
                }

                float m = Mth.lerp(tickDelta, player.oBob, player.bob);
                j += Mth.sin(Mth.lerp(tickDelta, player.walkDistO, player.walkDist) * 6.0F) * 32.0F * m;
                if (player.isCrouching()) {
                    j += 25.0F;
                }

                stack.mulPose(Vector3f.XP.rotationDegrees(6.0F + k / 2.0F + j));
                stack.mulPose(Vector3f.ZP.rotationDegrees(l / 2.0F));
                stack.mulPose(Vector3f.YP.rotationDegrees(180.0F - l / 2.0F));


                if(CosmeticManager.playerCapes.get(player.getStringUUID()) != null) {
                    RenderSystem.enableDepthTest();
                    RenderSystem.setShader(GameRenderer::getPositionTexShader);
                    Cape playerCape = CosmeticManager.playerCapes.get(player.getStringUUID());
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

