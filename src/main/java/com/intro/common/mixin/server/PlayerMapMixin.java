package com.intro.common.mixin.server;

import net.minecraft.server.level.PlayerMap;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerMap.class)
public class PlayerMapMixin {

    @Inject(method = "removePlayer", at = @At("HEAD"))
    public void removePlayer(long chunkPos, ServerPlayer player, CallbackInfo ci) {
        // System.out.println("removed player");
        // PlayerApi.removePlayerRegistry(player);
        // PlayerApi.playersRunningOsmium.remove(player.getUUID().toString());
    }
}
