package com.intro.server.network;

import com.intro.common.network.NetworkingConstants;
import com.intro.server.api.PlayerApi;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ServerNetworkHandler {



    public static void sendPacket(ServerPlayer client, ResourceLocation channel, FriendlyByteBuf data) {
        ServerPlayNetworking.send(client, channel, data);
    }

    public static void registerPackets() {
        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.RUNNING_OSMIUM_CLIENT_PACKET_ID, (server, player, handler, buf, responseSender) -> {
            PlayerApi.setRunningOsmium(player, true);
            PlayerApi.playersRunningOsmium.put(player.getUUID().toString(), player);
        });

        ServerPlayConnectionEvents.DISCONNECT.register(((handler, server) -> {
            System.out.println("removed player");
            PlayerApi.removePlayerRegistry(handler.getPlayer());
            PlayerApi.playersRunningOsmium.remove(handler.getPlayer().getUUID().toString());
        }));
    }

}
