package com.intro.common.mixin.client;

import com.intro.client.render.RenderManager;
import com.intro.common.config.Options;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(at = @At("HEAD"), method = "bobHurt", cancellable = true)
    public void bobViewWhenHurt(PoseStack arg, float f, CallbackInfo info) {
        if(Options.HurtBobbingEnabled.get())
            info.cancel();
    }

    @Inject(at = @At(value = "HEAD"), method = "renderLevel")
    public void renderWorld(float partialTicks, long finishTimeNano, PoseStack matrixStack, CallbackInfo ci) {
        RenderManager.postRenderEvents(partialTicks, finishTimeNano, matrixStack);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V"))
    public void redirectPostShaders(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {

    }



}
