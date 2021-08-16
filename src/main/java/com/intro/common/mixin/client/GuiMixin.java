package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.client.render.RenderManager;
import com.intro.common.config.options.EnumOption;
import com.intro.common.config.options.StatusEffectDisplayMode;
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
        if(((EnumOption) OsmiumClient.options.get(OsmiumClient.options.StatusEffectDisplayMode.identifier)).variable == StatusEffectDisplayMode.CUSTOM)
            ci.cancel();
    }
}