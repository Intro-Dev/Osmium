package dev.lobstershack.client.render.widget.drawable;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.render.color.Color;
import dev.lobstershack.client.render.color.Colors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ArmorDisplay extends Scalable {

    public ArmorDisplay() {
        super(Options.ArmorDisplayPosition, 20, 190, Component.empty());
    }

    private final Minecraft mc = Minecraft.getInstance();
    private static ArmorDisplay instance;

    @Override
    public void render(GuiGraphics graphics) {
        if(mc.player != null && Options.ArmorDisplayEnabled.get()) {
            this.visible = true;

            int offY = 0;

            List<ItemStack> stacks = StreamSupport.stream(mc.player.getArmorSlots().spliterator(), false).collect(Collectors.toList());
            stacks.addAll(StreamSupport.stream(mc.player.getHandSlots().spliterator(), false).collect(Collectors.toList()));

            graphics.fill(this.getX() - 2, this.getY(), this.getX() + width + 2, this.getY() + height, Colors.BACKGROUND_GRAY.getColor().getInt());

            for(ItemStack itemStack : stacks) {
                if(itemStack.getMaxDamage() != 0 && itemStack.isDamageableItem()) {
                    renderStatusBar(graphics, itemStack, this.getX() + 2, this.getX() + width - 2, this.getY() + offY + 16);
                }
                offY += 32;
            }

            offY = 0;
            for(ItemStack itemStack : stacks) {
                renderScaledItemStack(itemStack, this.getX() + 2, this.getY() + offY);
                if(itemStack.isDamageableItem()) {
                    if(itemStack.getMaxDamage() != 0) {
                        graphics.drawCenteredString(mc.font, String.valueOf(itemStack.getMaxDamage() - itemStack.getDamageValue()), (this.getX() + this.width / 2), this.getY() + offY + 20, Colors.WHITE.getColor().getInt());
                    }
                } else {
                    if(!itemStack.is(Items.AIR))
                        graphics.drawCenteredString(mc.font, String.valueOf(itemStack.getCount()), (this.getX() + this.width / 2), this.getY() + offY + 20, Colors.WHITE.getColor().getInt());
                }
                offY += 32;
            }
        } else {
            this.visible = false;
        }
    }

    private void renderStatusBar(GuiGraphics graphics, ItemStack itemStack, int x, int width, int y) {
        int j = itemStack.getBarColor();
        int barSize = itemStack.getBarWidth();
        int color = Color.fromRGBA(j >> 16 & 255, j >> 8 & 255, j & 255, 255);
        barSize = Mth.clamp(barSize, 0, width);
        graphics.hLine(x, x + barSize + 1, y, color);
    }

    // why is minecraft rendering code so bad
    // this is the function for rendering a block in the gui,
    // and you can't even make it any better because the way that its done is so deeply ingrained into the game that it would take a rewrite of half the rendering pipeline to make it any better
    private void renderScaledItemStack(ItemStack item, int x, int y) {
        BakedModel model = mc.getItemRenderer().getModel(item, null, null, 0);
        mc.getTextureManager().getTexture(InventoryMenu.BLOCK_ATLAS).setFilter(false, false);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        PoseStack poseStack = RenderSystem.getModelViewStack();
        poseStack.pushPose();
        scaleWithPositionIntact(poseStack);
        poseStack.translate(x, y, 100.0F);
        poseStack.translate(8.0D, 8.0D, 0.0D);
        poseStack.scale(1.0F, -1.0F, 1.0F);
        poseStack.scale(16.0F, 16.0F, 16.0F);
        RenderSystem.applyModelViewMatrix();
        PoseStack poseStack2 = new PoseStack();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        boolean usesLight = !model.usesBlockLight();
        if (usesLight) {
            Lighting.setupForFlatItems();
        }
        mc.getItemRenderer().render(item, ItemDisplayContext.GUI, false, poseStack2, bufferSource, 15728880, OverlayTexture.NO_OVERLAY, model);
        bufferSource.endBatch();
        RenderSystem.enableDepthTest();
        if (usesLight) {
            Lighting.setupFor3DItems();
        }

        poseStack.popPose();
        RenderSystem.applyModelViewMatrix();

    }

    public static ArmorDisplay getInstance() {
        if(instance == null) {
            instance = new ArmorDisplay();
            return instance;
        }
        return instance;
    }

    public static void invalidateInstance() {
        instance = null;
    }

}
