package dev.lobstershack.client.mixin.client;

import dev.lobstershack.client.event.EventBuss;
import dev.lobstershack.client.event.EventStartGame;
import dev.lobstershack.client.event.EventType;
import net.minecraft.client.gui.screens.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {

    @Inject(method = "init", at = @At("TAIL"))
    public void initPost(CallbackInfo ci) {
        EventBuss.postEvent(new EventStartGame(), EventType.EVENT_START_GAME);
    }
}
