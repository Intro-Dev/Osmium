package dev.lobstershack.client.render.cosmetic;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.lobstershack.client.OsmiumClient;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.GameRenderer;
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
import org.jetbrains.annotations.NotNull;

public class ElytraRenderer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {


    private final ElytraModel<T> elytra;

    public ElytraRenderer(RenderLayerParent<T, M> renderLayerParent, EntityModelSet entityModelSet) {
        super(renderLayerParent);
        this.elytra = new ElytraModel<>(entityModelSet.bakeLayer(ModelLayers.ELYTRA));

    }

    @Override
    public void render(@NotNull PoseStack stack, @NotNull MultiBufferSource vertexConsumers, int light, @NotNull T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        try {
            ItemStack itemStack = entity.getItemBySlot(EquipmentSlot.CHEST);
            if (itemStack.is(Items.ELYTRA)) {
                stack.pushPose();
                stack.translate(0.0D, 0.0D, 0.125D);
                this.getParentModel().copyPropertiesTo(this.elytra);
                this.elytra.setupAnim(entity, limbAngle, tickDelta, animationProgress, headYaw, headPitch);
                if (entity.getStringUUID() != null) {
                    if ((OsmiumClient.cosmeticManager.getPlayerCape(entity.getStringUUID()) != null)) {
                        RenderSystem.enableDepthTest();
                        RenderSystem.setShader(GameRenderer::getPositionTexShader);
                        Cape playerCape = OsmiumClient.cosmeticManager.getPlayerCape(entity.getStringUUID());
                        ResourceLocation capeTexture = playerCape.getFrameTexture();
                        final VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(vertexConsumers, RenderType.armorCutoutNoCull(capeTexture), false, itemStack.hasFoil());
                        this.elytra.renderToBuffer(stack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
                    }
                    stack.popPose();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

