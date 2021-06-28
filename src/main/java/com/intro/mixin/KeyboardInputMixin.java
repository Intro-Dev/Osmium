package com.intro.mixin;

import com.intro.config.BooleanOption;
import com.intro.config.OptionUtil;
import com.intro.module.ToggleSneak;
import net.minecraft.client.input.Input;
import net.minecraft.client.input.KeyboardInput;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/*
 Adapted from Tweakeroo mod under GNU GPL
 Tweakeroo guthub : https://github.com/maruohon/tweakeroo/

 @author maruohon
 @author Intro
 */
@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {

    @Inject(method = "tick(Z)V", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/input/KeyboardInput;sneaking:Z",
            ordinal = 0,
            shift = At.Shift.AFTER,
            opcode = Opcodes.PUTFIELD))
    private void tick(boolean slowDown, CallbackInfo ci) {
        if(ToggleSneak.sneaking && ((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()).variable) {
            this.sneaking = true;
        }
    }
}
