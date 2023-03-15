package dev.lobstershack.common.mixin.client;

import dev.lobstershack.common.config.Options;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Abilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Abilities.class)
public class AbilitiesMixin {

    @Shadow public boolean flying;

    private final Minecraft mc = Minecraft.getInstance();

    @Inject(method = "getFlyingSpeed", at = @At("HEAD"), cancellable = true)
    public void getFlyingSpeed(CallbackInfoReturnable<Float> cir) {
        if(flying && mc.player != null && Options.FlyBoostEnabled.get()) {
            cir.setReturnValue(Mth.clamp((float) Options.FlyBoostAmount.get().byteValue() / 40, 0.05f, 0.5f));
        }
    }
}
