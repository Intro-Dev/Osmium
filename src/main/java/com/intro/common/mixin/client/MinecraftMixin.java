package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.EventDirection;
import com.intro.client.module.event.EventTick;
import com.intro.client.module.event.EventType;
import com.intro.client.util.OptionUtil;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {

    @Inject(at = @At("HEAD"), method = "tick")
    public void preTick(CallbackInfo info) {
        OsmiumClient.EVENT_BUS.postEvent(new EventTick(EventDirection.PRE), EventType.EVENT_TICK);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void postTick(CallbackInfo info) {
        OsmiumClient.EVENT_BUS.postEvent(new EventTick(EventDirection.POST), EventType.EVENT_TICK);
    }

    @Inject(at = @At("HEAD"), method = "close")
    public void close(CallbackInfo ci) {
        OptionUtil.save();
    }




}