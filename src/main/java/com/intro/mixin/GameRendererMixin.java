package com.intro.mixin;

import com.intro.config.options.BooleanOption;
import com.intro.render.RenderManager;
import com.intro.render.shader.Shader;
import com.intro.render.shader.ShaderSystem;
import com.intro.util.OptionUtil;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    private static double previousFov = 90;

    @Inject(at = @At("HEAD"), method = "bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V", cancellable = true)
    public void bobViewWhenHurt(MatrixStack arg, float f, CallbackInfo info) {
        if(((BooleanOption) OptionUtil.Options.HurtbobbingEnabled.get()).variable)
            info.cancel();
    }

    @Inject(at = @At(value = "HEAD"), method = "renderWorld(FJLnet/minecraft/client/util/math/MatrixStack;)V")
    public void renderWorld(float tickDelta, long limitTime, MatrixStack stack, CallbackInfo info) {
        RenderManager.postRenderEvents(tickDelta, limitTime, stack);
    }


    @Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/render/WorldRenderer.drawEntityOutlinesFramebuffer()V"))
    public void render(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        for(Shader shader : ShaderSystem.getShaders().values()) {
            shader.draw();
        }
    }

}
