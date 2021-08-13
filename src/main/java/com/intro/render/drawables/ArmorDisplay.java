package com.intro.render.drawables;

import com.intro.Osmium;
import com.intro.config.options.Vector2Option;
import com.intro.render.Color;
import com.intro.render.Colors;
import com.intro.render.RenderManager;
import com.intro.render.renderer.BatchRenderer;
import com.intro.render.renderer.Renderer2D;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ArmorDisplay extends Drawable {

    public ArmorDisplay() {
        this.posX = (int) ((Vector2Option) Osmium.options.get(Osmium.options.StatusEffectDisplayPosition.identifier)).x;
        this.posY = (int) ((Vector2Option) Osmium.options.get(Osmium.options.StatusEffectDisplayPosition.identifier)).y;
        this.width = 20;
        this.height = 132;
    }

    private final Minecraft mc = Minecraft.getInstance();
    private static ArmorDisplay instance;

    @Override
    public void render(PoseStack stack) {
        if(mc.player != null) {
            ItemRenderer itemRenderer = mc.getItemRenderer();
            int offY = 0;
            // convert iterator to list
            List<ItemStack> stacks = StreamSupport.stream(mc.player.getArmorSlots().spliterator(), false).collect(Collectors.toList());

            // start batch renderer
            BatchRenderer renderer = new BatchRenderer();
            renderer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);

            // render background
            renderer.drawQuad(stack, this.posX, this.posY, this.getBlitOffset(), 20, 132, 0.1f, 0.1f, 0.1f, 0.2f);
            // batch render status bars
            for(ItemStack armorStack : stacks) {
                if(armorStack.getMaxDamage() != 0) {
                    renderStatusBar(stack, armorStack, renderer, this.posX + 2, this.posX + width - 2, this.posY + offY + 16);
                }
                offY += 32;
            }
            renderer.end();

            // these can't use batch rendering
            // sadge
            offY = 0;
            for(ItemStack armorStack : stacks) {
                if(armorStack.getMaxDamage() != 0) {
                    itemRenderer.renderGuiItem(armorStack, this.posX + 2, this.posY + offY);
                    drawCenteredString(stack, mc.font, String.valueOf(armorStack.getMaxDamage() - armorStack.getDamageValue()), (this.posX + this.width / 2), this.posY + offY + 20, Colors.WHITE.getColor().getInt());
                }
                offY += 32;
            }

        }
    }

    private void renderStatusBar(PoseStack stack, ItemStack itemStack, Renderer2D renderer, int x, int width, int y) {
        int j = itemStack.getBarColor();
        int barSize = itemStack.getBarWidth();
        int color = Color.fromRGBA(j >> 16 & 255, j >> 8 & 255, j & 255, 255);
        barSize = Mth.clamp(barSize, 0, width);
        renderer.drawHorizontalLine(stack, x, x + barSize + 1, y, color);
    }




    @Override
    public void destroySelf() {
        RenderManager.drawables.remove(this);
    }
    
    public static ArmorDisplay getInstance() {
        if(instance == null) {
            instance = new ArmorDisplay();
            return instance;
        }
        return instance;
    }
}
