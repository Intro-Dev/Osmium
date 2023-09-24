package dev.lobstershack.client.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.config.Options;
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

}
