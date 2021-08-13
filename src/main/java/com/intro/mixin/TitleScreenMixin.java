package com.intro.mixin;

import com.intro.Osmium;
import com.intro.module.event.EventStartGame;
import com.intro.module.event.EventType;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        Osmium.EVENT_BUS.postEvent(new EventStartGame(), EventType.EVENT_START_GAME);
    }

}
