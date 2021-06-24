package com.intro.mixin;

import com.intro.Osmium;
import com.intro.module.event.EventJoinWorld;
import com.intro.module.event.EventAddPlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(at = @At("RETURN"), method = "onGameJoin")
    public void onGameJoin(GameJoinS2CPacket packet, CallbackInfo info) {
        Osmium.EVENT_BUS.PostEvent(new EventJoinWorld(packet));
        Osmium.EVENT_BUS.PostEvent(new EventAddPlayer(MinecraftClient.getInstance().player));
    }
}
