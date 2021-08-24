package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.common.config.Options;
import com.intro.common.config.options.SneakMode;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {

    @Shadow
    private Entity entity;

    @Shadow
    private float eyeHeight;




    @Inject(at = @At("HEAD"), method = "tick")
    public void changeEyeHeight(CallbackInfo info) {
        // smooth but no squish
        if (this.entity != null) {
            if (OsmiumClient.options.getEnumOption(Options.SneakMode).variable == SneakMode.INSTANT) {
                this.eyeHeight = this.entity.getEyeHeight();
            } else if (OsmiumClient.options.getEnumOption(Options.SneakMode).variable == SneakMode.SMOOTH) {
                this.eyeHeight = (float) Mth.lerp(0.8, this.eyeHeight, this.entity.getEyeHeight());
            } else {
                this.eyeHeight += (this.entity.getEyeHeight() - this.eyeHeight) * 0.5F;
            }
        }
    }

}
