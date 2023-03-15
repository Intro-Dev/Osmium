package dev.lobstershack.common.mixin.client;

import dev.lobstershack.common.config.Options;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    private static float brightness = 0;

    private static boolean fullbrightPreviouslyEnabled = false;


    @Inject(method = "getBrightness", at = @At(value = "HEAD"), cancellable = true)
    private static void changeBrightnessLevel(DimensionType dimensionType, int lightLevel, CallbackInfoReturnable<Float> cir) {
        // hardcoded because I cant be bothered to not
        float increment = 0.0002f;
        float max = 1f;

        if(Options.FulbrightEnabled.get()) {
            float f = (float)lightLevel / 15.0f;
            float g = f / (4.0f - 3.0f * f);
            float normalBrightNess = Mth.lerp(dimensionType.ambientLight(), g, 1.0f);
            if(!fullbrightPreviouslyEnabled) {
                brightness = normalBrightNess;
                fullbrightPreviouslyEnabled = true;
            }
            // cool animation
            brightness += increment;
            brightness = Mth.clamp(brightness, 0, max);
            cir.setReturnValue(brightness);
        }
    }
}
