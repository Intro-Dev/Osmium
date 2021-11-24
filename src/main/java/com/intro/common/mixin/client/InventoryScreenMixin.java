package com.intro.common.mixin.client;

import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends ScreenMixin implements Widget {

    private double scrollOffset;


    @Override
    public int modifyTooltipY(int y) {
        return (int) (y + scrollOffset);
    }


    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollDelta) {
        // disabled because im dumb and this won't be ready until after 1.18 comes out
        // scrollOffset += scrollDelta * 10;
        return super.mouseScrolled(mouseX, mouseY, scrollDelta);
    }

}
