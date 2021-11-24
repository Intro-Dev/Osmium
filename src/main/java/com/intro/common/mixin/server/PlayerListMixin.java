package com.intro.common.mixin.server;

import com.intro.common.config.options.Option;
import com.intro.common.config.options.OptionSerializer;
import com.intro.common.network.NetworkingConstants;
import com.intro.server.api.OptionApi;
import com.intro.server.api.PlayerApi;
import com.intro.server.network.ServerNetworkHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {

    @Unique private final OptionSerializer serializer = new OptionSerializer();

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    public void placeNewPlayer(Connection netManager, ServerPlayer player, CallbackInfo ci) {
        try {
            ServerNetworkHandler.sendPacket(player, NetworkingConstants.RUNNING_OSMIUM_SERVER_PACKET_ID, PacketByteBufs.create());
            FriendlyByteBuf byteBuf = PacketByteBufs.create();
            byteBuf.writeInt(OptionApi.getServerSetOptions().size());
            for (Option<?> option : OptionApi.getServerSetOptions()) {
                // can't use the GSON object here, or it doesn't serialize properly
                // I have no clue why
                byteBuf.writeUtf(serializer.serialize(option, null, null).toString());
            }
            ServerNetworkHandler.sendPacket(player, NetworkingConstants.SET_SETTING_PACKET_ID, byteBuf);
            PlayerApi.registerPlayer(player);

            for(ServerPlayer p : PlayerApi.playersRunningOsmium.values()) {
                if(PlayerApi.getPlayerProperties(p).capeDataBuffer != null) {
                    ServerNetworkHandler.sendPacket(player, NetworkingConstants.SET_PLAYER_CAPE_CLIENT_BOUND, PlayerApi.getPlayerProperties(p).capeDataBuffer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

