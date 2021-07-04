package com.intro.mixin;

import com.intro.Osmium;
import com.intro.config.BooleanOption;
import com.intro.config.DoubleOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAbilities.class)
public class PlayerAbilitiesMixin {

    @Shadow public boolean allowFlying;

    @Inject(method = "getFlySpeed", at = @At("HEAD"), cancellable = true)
    public void getFlySpeed(CallbackInfoReturnable<Float> cir) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if(allowFlying && mc.player != null && ((BooleanOption) Osmium.options.get("FlyBoostEnabled")).variable ) {
            cir.setReturnValue(MathHelper.clamp((float) ((DoubleOption) Osmium.options.get("FlyBoostAmount")).variable / 5, 0.05f, 0.5f));
        }
    }
}
