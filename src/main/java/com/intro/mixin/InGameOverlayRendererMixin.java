package com.intro.mixin;

import com.intro.config.BooleanOption;
import com.intro.config.OptionUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameOverlayRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameOverlayRenderer.class)
public class InGameOverlayRendererMixin {

    @Inject(at = @At("HEAD"), method = "renderFireOverlay(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/util/math/MatrixStack;)V", cancellable = true)
    private static void renderFireOverlay(MinecraftClient arg, MatrixStack arg2, CallbackInfo info) {
        if(((BooleanOption) OptionUtil.Options.NoFireEnabled.get()).variable) {
            arg2.push();
            arg2.translate(0, -0.3, 0);
            arg2.pop();
        }
    }
}
