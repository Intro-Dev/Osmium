package dev.lobstershack.client.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.config.Options;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRendererMixin {

    /**
     @author Intro
     @reason Make LowFire work because it now uses BufferBuilder.
     */
    @Redirect(method = "renderFire", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V"))
    private static void renderFire(PoseStack stack, float f, float g, float h) {
        //
        if(Options.NoFireEnabled.get()) {
            stack.translate(f, g - 0.2f, h);
        } else {
            stack.translate(f, g, h);
        }
    }


}
