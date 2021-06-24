package com.intro.mixin;

import com.intro.OsmiumOptions;
import com.intro.render.RenderManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(at = @At("HEAD"), method = "bobViewWhenHurt(Lnet/minecraft/client/util/math/MatrixStack;F)V", cancellable = true)
    public void bobViewWhenHurt(MatrixStack arg, float f, CallbackInfo info) {
        if(OsmiumOptions.HurtBobbingModEnabled)
            info.cancel();
    }

    @Inject(at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = { "ldc=hand" }), locals = LocalCapture.CAPTURE_FAILSOFT, method = "renderWorld")
    public void renderWorld(float tickDelta, long limitTime, MatrixStack matrix, CallbackInfo info, boolean bl, Camera camera, MatrixStack matrixStack2, Matrix4f matrix4f) {
        RenderManager.CreateInstance().render(tickDelta, limitTime, matrix, info, bl, camera, matrixStack2, matrix4f);
    }
}
