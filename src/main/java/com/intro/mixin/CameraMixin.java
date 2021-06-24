package com.intro.mixin;

import com.intro.Osmium;
import com.intro.OsmiumOptions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
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
        if (this.focusedEntity != null) {
            // smooth but no squish
            if (OsmiumOptions.NoSquishySneak) {
                this.cameraY = this.focusedEntity.getStandingEyeHeight();
            } else if (OsmiumOptions.SmoothSneak) {
                this.cameraY += (this.focusedEntity.getStandingEyeHeight() - this.cameraY) * 0.8F;
            } else {
                this.cameraY += (this.focusedEntity.getStandingEyeHeight() - this.cameraY) * 0.5F;
            }

        }
    }
}
