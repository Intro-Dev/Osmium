package com.intro.common.mixin.client;

import com.intro.common.config.Options;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightTexture.class)
public class LightTextureMixin {


    private float brightness = 0;

    private boolean fullbrightPreviouslyEnabled = false;

    @Redirect(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F"))
    public float changeBrightnessLevel(Double inputGamma) {
        // hardcoded because I cant be bothered to not
        float increment = 0.0001f;
        float max = 1f;

        if(Options.FulbrightEnabled.get()) {
            if(!fullbrightPreviouslyEnabled) {
                brightness = inputGamma.floatValue();
                fullbrightPreviouslyEnabled = true;
            }
            // cool animation
            brightness += increment;
            brightness = Mth.clamp(brightness, 0, max);
            return brightness;
        }
        brightness -= increment;
        brightness = Mth.clamp(brightness, inputGamma.floatValue(), max);
        fullbrightPreviouslyEnabled = false;
        return brightness;
    }
}
