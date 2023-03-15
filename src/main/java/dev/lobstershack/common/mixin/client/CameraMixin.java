package dev.lobstershack.common.mixin.client;

import dev.lobstershack.client.module.FreeLookManager;
import dev.lobstershack.common.config.Options;
import dev.lobstershack.common.config.options.SneakMode;
import net.minecraft.client.Camera;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.joml.Vector2f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow
    private Entity entity;

    @Shadow
    private float eyeHeight;

    @Shadow
    protected abstract void setRotation(float yRot, float xRot);

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Camera;setRotation(FF)V"), method = "setup")
    public void stopCameraSync(Camera instance, float yRot, float xRot) {
        Vector2f newCameraPos = FreeLookManager.getInstance().processCameraTick(yRot, xRot);
        this.setRotation(newCameraPos.y(), newCameraPos.x());
    }


    @Inject(at = @At("HEAD"), method = "tick")
    public void changeEyeHeight(CallbackInfo info) {
        // smooth but no squish
        if (this.entity != null) {
            if (Options.SneakMode.get() == SneakMode.INSTANT) {
                this.eyeHeight = this.entity.getEyeHeight();
            } else if (Options.SneakMode.get() == SneakMode.SMOOTH) {
                this.eyeHeight = (float) Mth.lerp(0.8, this.eyeHeight, this.entity.getEyeHeight());
            } else {
                this.eyeHeight += (this.entity.getEyeHeight() - this.eyeHeight) * 0.5F;
            }
        }
    }

}
