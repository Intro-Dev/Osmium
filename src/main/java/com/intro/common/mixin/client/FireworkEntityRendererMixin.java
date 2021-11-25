package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.common.config.Options;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.FireworkEntityRenderer;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkEntityRenderer.class)
public class FireworkEntityRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(FireworkRocketEntity fireworkRocketEntity, float f, float g, PoseStack matrixStack, MultiBufferSource multiBuffer, int i, CallbackInfo ci) {
        if(OsmiumClient.options.getBooleanOption(Options.FireworksDisabled).get()) {
            ci.cancel();
        }
    }
}
