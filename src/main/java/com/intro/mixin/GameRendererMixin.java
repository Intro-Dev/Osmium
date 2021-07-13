package com.intro.mixin;

import com.intro.Osmium;
import com.intro.config.BooleanOption;
import com.intro.config.EnumOption;
import com.intro.config.OptionUtil;
import com.intro.config.ZoomMode;
import com.intro.render.RenderManager;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
        RenderManager.CreateInstance().render(tickDelta, limitTime, stack);
    }

    @Inject(method = "getFov", at= @At("RETURN"), cancellable = true)
    private void getFov(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> info) {
        double fov = info.getReturnValue();
        if(Osmium.zoomKey.isPressed()) {
            if(((EnumOption) OptionUtil.Options.ZoomMode.get()).variable == ZoomMode.SMOOTH) {
                fov *= MathHelper.lerp(tickDelta, previousFov, 0.25);
                previousFov = fov;
                info.setReturnValue(fov);
            }
            if(((EnumOption) OptionUtil.Options.ZoomMode.get()).variable == ZoomMode.HARD) {
                info.setReturnValue(fov / 3);
            }
        }
    }

}
