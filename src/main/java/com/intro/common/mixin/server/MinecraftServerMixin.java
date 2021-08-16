package com.intro.common.mixin.server;

import com.intro.server.api.OptionApi;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {

    @Inject(method = "onServerExit", at = @At("HEAD"))
    public void onClose(CallbackInfo ci) {
        OptionApi.save();
    }
}
