package com.intro.mixin;

import com.intro.config.options.BooleanOption;
import com.intro.module.ToggleSneak;
import com.intro.util.OptionUtil;
import net.minecraft.client.player.Input;
import net.minecraft.client.player.KeyboardInput;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/*
 Adapted from Tweakeroo mod under GNU GPL
 Tweakeroo github : https://github.com/maruohon/tweakeroo/

 @author maruohon
 @author Intro
 */
@Mixin(KeyboardInput.class)
public class KeyboardInputMixin extends Input {

    @Inject(method = "tick(Z)V", at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/client/player/KeyboardInput;shiftKeyDown:Z",
            ordinal = 0,
            shift = At.Shift.AFTER,
            opcode = Opcodes.PUTFIELD))
    private void tick(boolean slowDown, CallbackInfo ci) {
        if(ToggleSneak.sneaking && ((BooleanOption) OptionUtil.Options.ToggleSneakEnabled.get()).variable) {
            this.shiftKeyDown = true;
        }
    }
}
