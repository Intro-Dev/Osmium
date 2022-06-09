package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.common.config.Options;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LightTexture.class)
public class LightTextureMixin {

    private float lastBrightness = 0;

    private long lastCalled = System.currentTimeMillis();

    private boolean alreadyActive = false;

    @Redirect(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Ljava/lang/Double;floatValue()F"))
    public float changeBrightnessLevel(Double gamma) {
        if(OsmiumClient.options.getBooleanOption(Options.FullbrightEnabled).get()) {
            if(!alreadyActive) {
                lastBrightness = gamma.floatValue();
                alreadyActive = true;
            }
            float delta = (System.currentTimeMillis() % lastCalled) / 1000f;
            float currentBrightness = Mth.lerp(delta, lastBrightness, 100);
            lastBrightness = currentBrightness;
            lastCalled = System.currentTimeMillis();
            return currentBrightness;
        }
        alreadyActive = false;
        lastCalled = System.currentTimeMillis();
        return gamma.floatValue();
    }
}
