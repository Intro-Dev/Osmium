package com.intro.mixin;

import com.intro.Osmium;
import com.intro.config.options.EnumOption;
import com.intro.config.options.StatusEffectDisplayMode;
import com.intro.render.RenderManager;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)

public class InGameHudMixin {
    @Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
    public void render(MatrixStack stack, float f, CallbackInfo info) {
        RenderManager.renderHud(stack);
    }

    @Inject(at = @At("HEAD"), method = "renderStatusEffectOverlay", cancellable = true)
    public void renderStatusEffectOverlay(MatrixStack matrices, CallbackInfo ci) {
        if(((EnumOption) Osmium.options.get(Osmium.options.StatusEffectDisplayMode.identifier)).variable == StatusEffectDisplayMode.CUSTOM)
            ci.cancel();
    }
}
