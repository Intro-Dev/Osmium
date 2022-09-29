package com.intro.common.mixin.client;

import com.intro.client.render.drawables.ToggleSneak;
import com.intro.common.config.Options;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/*
 Adapted from Tweakeroo mod under GNU GPL
 Tweakeroo GitHub : https://github.com/maruohon/tweakeroo/

 @author maruohon
 @author Intro
 */
@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {

    @Inject(method = "tick", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/player/KeyboardInput;shiftKeyDown:Z",
            ordinal = 0,
            shift = At.Shift.AFTER,
            opcode = Opcodes.PUTFIELD))
    private void tick(boolean bl, float f, CallbackInfo ci) {
        if(ToggleSneak.getInstance().shouldSneak() && Options.ToggleSneakEnabled.get()) {
            this.shiftKeyDown = true;
        }
    }
}
