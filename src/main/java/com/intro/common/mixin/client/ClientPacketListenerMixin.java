package com.intro.common.mixin.client;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.EventAddPlayer;
import com.intro.client.module.event.EventJoinWorld;
import com.intro.client.module.event.EventType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.protocol.game.ClientboundAddPlayerPacket;
import net.minecraft.network.protocol.game.ClientboundLoginPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(at = @At("RETURN"), method = "handleLogin")
    public void onGameJoin(ClientboundLoginPacket packet, CallbackInfo info) {
        OsmiumClient.EVENT_BUS.postEvent(new EventJoinWorld(packet), EventType.EVENT_JOIN_WORLD);
        OsmiumClient.EVENT_BUS.postEvent(new EventAddPlayer(Minecraft.getInstance().player), EventType.EVENT_ADD_PLAYER);
    }

    @Inject(at = @At("RETURN"), method = "handleAddPlayer")
    public void onAddPlayer(ClientboundAddPlayerPacket packet, CallbackInfo ci) {
        OsmiumClient.EVENT_BUS.postEvent(new EventAddPlayer((AbstractClientPlayer) Minecraft.getInstance().level.getPlayerByUUID(packet.getPlayerId())), EventType.EVENT_ADD_PLAYER);
    }
    


}
