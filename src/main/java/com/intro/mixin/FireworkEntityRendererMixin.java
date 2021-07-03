package com.intro.mixin;

import com.intro.Osmium;
import com.intro.config.BooleanOption;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.FireworkEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireworkEntityRenderer.class)
public class FireworkEntityRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(FireworkRocketEntity fireworkRocketEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if(((BooleanOption) Osmium.options.FireworksDisabled.get()).variable) {
            ci.cancel();
        }
    }
}
