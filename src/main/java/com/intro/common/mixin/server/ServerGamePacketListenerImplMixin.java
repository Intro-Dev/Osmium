package com.intro.common.mixin.server;

import com.intro.server.network.ServerNetworkHandler;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Shadow public ServerPlayer player;

    @Inject(method = "onDisconnect", at = @At("RETURN"))
    public void handleDisconnect(CallbackInfo ci) {
        ServerNetworkHandler.onDisconnect(player);
    }
}
