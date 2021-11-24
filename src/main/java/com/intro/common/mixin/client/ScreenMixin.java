package com.intro.common.mixin.client;

import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Screen.class)
public abstract class ScreenMixin extends AbstractContainerEventHandlerMixin {

    @ModifyVariable(method = "renderTooltipInternal", ordinal = 5, at = @At(value = "INVOKE", target = "com/mojang/blaze3d/vertex/PoseStack.pushPose()V", shift = At.Shift.BEFORE))
    public int modifyTooltipY(int y) {
        return y;
    }

}
