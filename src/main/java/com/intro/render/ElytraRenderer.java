package com.intro.render;

import com.intro.config.options.CapeRenderingMode;
import com.intro.config.options.EnumOption;
import com.intro.util.OptionUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ElytraRenderer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {


    private final ElytraModel<T> elytra;
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation("textures/entity/elytra.png");

    public ElytraRenderer(RenderLayerParent<T, M> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.elytra = new ElytraModel(entityModelSet.bakeLayer(ModelLayers.ELYTRA));

    }

    @Override
    public void render(PoseStack stack, MultiBufferSource vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        try {
            ItemStack itemStack = entity.getItemBySlot(EquipmentSlot.CHEST);
            if (itemStack.is(Items.ELYTRA)) {

                stack.pushPose();
                stack.translate(0.0D, 0.0D, 0.125D);
                this.getParentModel().copyPropertiesTo(this.elytra);
                this.elytra.setupAnim(entity, limbAngle, tickDelta, animationProgress, headYaw, headPitch);
                if(entity.getStringUUID() != null) {
                    if((CapeRenderer.CapeArray.get(entity.getStringUUID()) != null)) {
                        if(((EnumOption) OptionUtil.Options.CustomCapeMode.get()).variable == CapeRenderingMode.OPTIFINE && CapeRenderer.OptifineCapes.contains(entity.getStringUUID())) {
                            // FIXME might break depending on OverlayTexture
                            // just mess with it until it works
                            final VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(vertexConsumers, RenderType.armorCutoutNoCull(CapeRenderer.CapeArray.get(entity.getStringUUID())), false, itemStack.hasFoil());
                            this.elytra.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                        }
                    } else {
                        final VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(vertexConsumers, RenderType.armorCutoutNoCull(DEFAULT_TEXTURE), false, itemStack.hasFoil());
                        this.elytra.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                    }

                }
                stack.popPose();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

