package dev.lobstershack.client.mixin.client;

import dev.lobstershack.client.feature.FreeLookManager;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Redirect(method = "turnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"))
    public void redirectPlayerRotUpdate(LocalPlayer instance, double yRot, double xRot) {
        FreeLookManager.getInstance().stopCameraEntityRotationUpdate(instance, yRot, xRot);
    }

}
