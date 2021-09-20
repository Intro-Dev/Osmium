package com.intro.server.network;

import com.intro.common.network.NetworkingConstants;
import com.intro.server.OsmiumServer;
import com.intro.server.api.PlayerApi;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.Level;

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
            PlayerApi.removePlayerRegistry(handler.getPlayer());
            PlayerApi.playersRunningOsmium.remove(handler.getPlayer().getUUID().toString());
        }));

        ServerPlayNetworking.registerGlobalReceiver(NetworkingConstants.SET_PLAYER_CAPE_SERVER_BOUND, (server, player, handler, buf, responseSender) -> {

            if(buf.capacity() > 16384) {
                OsmiumServer.LOGGER.log(Level.INFO, "Player tried sending massive cape set packet");
                return;
            }

            FriendlyByteBuf clientSendByteBuf = PacketByteBufs.create();
            clientSendByteBuf.writeUtf(player.getStringUUID());
            clientSendByteBuf.writeBytes(buf);

            for(ServerPlayer client : PlayerApi.playersRunningOsmium.values()) {
                sendPacket(client, NetworkingConstants.SET_PLAYER_CAPE_CLIENT_BOUND, clientSendByteBuf);
            }

            PlayerApi.setPlayerCapeBuffer(player.getStringUUID(), clientSendByteBuf);

        });
    }

}
