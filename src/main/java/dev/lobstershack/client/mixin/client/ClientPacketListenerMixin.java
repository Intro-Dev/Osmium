package dev.lobstershack.client.mixin.client;

import dev.lobstershack.client.util.CustomEvents;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.UUID;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {

    @Inject(method = "handlePlayerInfoUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/social/PlayerSocialManager;addPlayer(Lnet/minecraft/client/multiplayer/PlayerInfo;)V"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void broadcastPlayerJoin(ClientboundPlayerInfoUpdatePacket packet, CallbackInfo ci, Iterator var2, ClientboundPlayerInfoUpdatePacket.Entry entry, PlayerInfo playerInfo) {
        CustomEvents.PLAYER_JOIN.invoker().onPlayerJoin(playerInfo);
    }

    @Inject(method = "handlePlayerInfoRemove", at = @At(value = "INVOKE", target = "Ljava/util/Set;remove(Ljava/lang/Object;)Z"), locals = LocalCapture.CAPTURE_FAILHARD)
    public void broadcastPlayerRemove(ClientboundPlayerInfoRemovePacket packet, CallbackInfo ci, Iterator var2, UUID uUID, PlayerInfo playerInfo) {
        CustomEvents.PLAYER_REMOVE.invoker().onPlayerRemove(playerInfo);
    }

}
