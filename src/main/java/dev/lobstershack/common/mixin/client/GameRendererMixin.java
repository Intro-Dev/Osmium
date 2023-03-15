package dev.lobstershack.common.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.lobstershack.client.event.EventBuss;
import dev.lobstershack.client.event.EventDirection;
import dev.lobstershack.client.event.EventRender;
import dev.lobstershack.client.event.EventType;
import dev.lobstershack.common.config.Options;
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
    public void renderWorld(float partialTicks, long finishTimeNano, PoseStack stack, CallbackInfo ci) {
        EventRender EventRenderPre = new EventRender(EventDirection.PRE, partialTicks, finishTimeNano, stack);
        EventBuss.postEvent(EventRenderPre, EventType.EVENT_RENDER);
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V"))
    public void redirectPostShaders(float partialTicks, long nanoTime, boolean renderLevel, CallbackInfo ci) {

    }



}
