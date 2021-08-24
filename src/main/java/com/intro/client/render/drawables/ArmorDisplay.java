package com.intro.client.render.drawables;

import com.intro.client.OsmiumClient;
import com.intro.client.render.Color;
import com.intro.client.render.Colors;
import com.intro.client.render.RenderManager;
import com.intro.common.config.Options;
import com.intro.common.config.options.Vector2Option;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ArmorDisplay extends Drawable {

    public ArmorDisplay() {
        this.posX = (int) OsmiumClient.options.getVector2Option(Options.ArmorDisplayPosition).x;
        this.posY = (int) OsmiumClient.options.getVector2Option(Options.ArmorDisplayPosition).y;
        this.width = 20;
        this.height = 190;
    }

    private final Minecraft mc = Minecraft.getInstance();
    private static ArmorDisplay instance;

    @Override
    public void render(PoseStack stack) {
        if(mc.player != null && OsmiumClient.options.getBooleanOption(Options.ArmorDisplayEnabled).variable) {

            OsmiumClient.options.put(Options.ArmorDisplayPosition, new Vector2Option(Options.ArmorDisplayPosition, this.posX, this.posY));

            ItemRenderer itemRenderer = mc.getItemRenderer();
            int offY = 0;
            List<ItemStack> stacks = StreamSupport.stream(mc.player.getArmorSlots().spliterator(), false).collect(Collectors.toList());
            stacks.addAll(StreamSupport.stream(mc.player.getHandSlots().spliterator(), false).collect(Collectors.toList()));

            fill(stack, this.posX - 2, this.posY, this.posX + width + 2, this.posY + height, new Color(0.1f, 0.1f, 0.1f, 0.2f).getInt());

            for(ItemStack itemStack : stacks) {
                if(itemStack.getMaxDamage() != 0 && itemStack.isDamageableItem()) {
                    renderStatusBar(stack, itemStack, this.posX + 2, this.posX + width - 2, this.posY + offY + 16);
                }
                offY += 32;
            }

            offY = 0;
            for(ItemStack itemStack : stacks) {
                itemRenderer.renderGuiItem(itemStack, this.posX + 2, this.posY + offY);
                if(itemStack.isDamageableItem()) {
                    if(itemStack.getMaxDamage() != 0) {
                        drawCenteredString(stack, mc.font, String.valueOf(itemStack.getMaxDamage() - itemStack.getDamageValue()), (this.posX + this.width / 2), this.posY + offY + 20, Colors.WHITE.getColor().getInt());
                    }
                } else {
                    if(!itemStack.is(Items.AIR))
                        drawCenteredString(stack, mc.font, String.valueOf(itemStack.getCount()), (this.posX + this.width / 2), this.posY + offY + 20, Colors.WHITE.getColor().getInt());
                }
                offY += 32;
            }

        }
    }

    private void renderStatusBar(PoseStack stack, ItemStack itemStack, int x, int width, int y) {
        int j = itemStack.getBarColor();
        int barSize = itemStack.getBarWidth();
        int color = Color.fromRGBA(j >> 16 & 255, j >> 8 & 255, j & 255, 255);
        barSize = Mth.clamp(barSize, 0, width);
        hLine(stack, x, x + barSize + 1, y, color);
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
