package com.intro.mixin;

import com.intro.Osmium;
import com.intro.module.event.EventDirection;
import com.intro.module.event.EventTick;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(at = @At("HEAD"), method = "tick")
    public void preTick(CallbackInfo info) {
        Osmium.EVENT_BUS.PostEvent(new EventTick(EventDirection.PRE));
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void postTick(CallbackInfo info) {
        Osmium.EVENT_BUS.PostEvent(new EventTick(EventDirection.POST));
    }


}
