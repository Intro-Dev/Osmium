package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.EventStartGame;
import com.intro.client.module.event.EventType;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))
    public void initPost(CallbackInfo ci) {
        OsmiumClient.EVENT_BUS.postEvent(new EventStartGame(), EventType.EVENT_START_GAME);
    }
}
