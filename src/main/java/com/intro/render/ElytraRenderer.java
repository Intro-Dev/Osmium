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
import net.minecraft.client.render.entity.model.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.ArrayList;
import java.util.HashMap;

public class ElytraRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {


    private final ElytraEntityModel<T> elytra;
    private static final Identifier SKIN = new Identifier("textures/entity/elytra.png");

    @SuppressWarnings("unchecked")
    public ElytraRenderer(FeatureRendererContext<T, M> context, EntityModelLoader loader) {
        super(context);
        this.elytra = new ElytraEntityModel(loader.getModelPart(EntityModelLayers.ELYTRA));

    }

    @Override
    public void render(MatrixStack stack, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        try {
            ItemStack itemStack = entity.getEquippedStack(EquipmentSlot.CHEST);
            if (itemStack.isOf(Items.ELYTRA) && (CapeRenderer.CapeArray.get(entity.getUuidAsString()) != null)) {
                Identifier identifier4;
                if (entity instanceof AbstractClientPlayerEntity playerEntity) {
                    if (playerEntity.canRenderElytraTexture() && playerEntity.getElytraTexture() != null) {
                        identifier4 = playerEntity.getElytraTexture();
                    } else if (playerEntity.canRenderCapeTexture() && playerEntity.getCapeTexture() != null && playerEntity.isPartVisible(PlayerModelPart.CAPE)) {
                        identifier4 = playerEntity.getCapeTexture();
                    } else {
                        identifier4 = SKIN;
                    }
                } else {
                    identifier4 = SKIN;
                }

                stack.push();
                stack.translate(0.0D, 0.0D, 0.125D);
                this.getContextModel().copyStateTo(this.elytra);
                this.elytra.setAngles(entity, limbAngle, tickDelta, animationProgress, headYaw, headPitch);
                if(entity.getUuidAsString() != null) {
                    if(((EnumOption) OptionUtil.Options.CustomCapeMode.get()).variable == CapeRenderingMode.OPTIFINE && CapeRenderer.OptifineCapes.contains(entity.getUuidAsString())) {
                        final VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(CapeRenderer.CapeArray.get(entity.getUuidAsString())), false, itemStack.hasGlint());
                        this.elytra.render(stack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
                    } else if (((EnumOption) OptionUtil.Options.CustomCapeMode.get()).variable == CapeRenderingMode.ALL){
                        final VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, RenderLayer.getArmorCutoutNoCull(CapeRenderer.CapeArray.get(entity.getUuidAsString())), false, itemStack.hasGlint());
                        this.elytra.render(stack, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
                    }

                }
                stack.pop();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

