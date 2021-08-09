package com.intro.mixin;

import com.intro.config.options.EnumOption;
import com.intro.config.options.SneakMode;
import com.intro.util.OptionUtil;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public class CameraMixin {

    @Shadow
    private Entity focusedEntity;

    @Shadow
    private float lastCameraY;

    @Shadow
    private float cameraY;

    @Shadow
    private float pitch;

    @Shadow
    private float yaw;



    @Inject(at = @At("HEAD"), method = "updateEyeHeight")
    public void changeEyeHeight(CallbackInfo info) {
        // smooth but no squish
        if (this.focusedEntity != null) {
            if (((EnumOption) OptionUtil.Options.SneakMode.get()).variable == SneakMode.INSTANT) {
                this.cameraY = this.focusedEntity.getStandingEyeHeight();
            } else if (((EnumOption) OptionUtil.Options.SneakMode.get()).variable == SneakMode.SMOOTH) {
                this.cameraY = (float) MathHelper.lerp(0.8, this.cameraY, this.focusedEntity.getStandingEyeHeight());
            } else {
                this.cameraY += (this.focusedEntity.getStandingEyeHeight() - this.cameraY) * 0.5F;
            }
        }
    }

}
