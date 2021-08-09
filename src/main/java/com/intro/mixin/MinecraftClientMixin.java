package com.intro.mixin;

import com.intro.Osmium;
import com.intro.module.event.EventDirection;
import com.intro.module.event.EventRenderPostTick;
import com.intro.module.event.EventTick;
import com.intro.module.event.EventType;
import com.intro.util.OptionUtil;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

    @Inject(at = @At("HEAD"), method = "tick")
    public void preTick(CallbackInfo info) {
        Osmium.EVENT_BUS.postEvent(new EventTick(EventDirection.PRE), EventType.EVENT_TICK);
    }

    @Inject(at = @At("HEAD"), method = "tick")
    public void postTick(CallbackInfo info) {
        Osmium.EVENT_BUS.postEvent(new EventTick(EventDirection.POST), EventType.EVENT_TICK);
    }

    @Inject(at = @At("HEAD"), method = "close")
    public void close(CallbackInfo ci) {
        OptionUtil.save();
    }


    @Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/toast/ToastManager.draw(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    public void onPostRenderTick(CallbackInfo info) {
        Osmium.EVENT_BUS.postEvent(new EventRenderPostTick(EventDirection.POST), EventType.EVENT_RENDER_POST_TICK);
    }


}
