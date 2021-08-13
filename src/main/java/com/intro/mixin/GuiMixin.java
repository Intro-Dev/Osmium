package com.intro.mixin;

import com.intro.Osmium;
import com.intro.config.options.EnumOption;
import com.intro.config.options.StatusEffectDisplayMode;
import com.intro.render.RenderManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Gui.class)

public class GuiMixin {
    @Inject(at = @At("TAIL"), method = "render")
    public void render(PoseStack stack, float f, CallbackInfo info) {
        RenderManager.renderHud(stack);
    }

    @Inject(at = @At("HEAD"), method = "renderEffects", cancellable = true)
    public void renderStatusEffectOverlay(PoseStack matrixStack, CallbackInfo ci) {
        if(((EnumOption) Osmium.options.get(Osmium.options.StatusEffectDisplayMode.identifier)).variable == StatusEffectDisplayMode.CUSTOM)
            ci.cancel();
    }
}
