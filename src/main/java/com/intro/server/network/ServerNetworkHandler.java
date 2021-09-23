package com.intro.server.network;

import com.intro.common.network.NetworkingConstants;
import com.intro.server.OsmiumServer;
import com.intro.server.api.PlayerApi;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerNetworkHandler {

    private static HashMap<ResourceLocation, ArrayList<PacketListener>> listeners = new HashMap<>();

    public static void registerPacketListener(ResourceLocation channel, PacketListener listener) {
        listeners.computeIfAbsent(channel, k -> new ArrayList<>());
        listeners.get(channel).add(listener);
    }

    public static void handlePacketEvent(ServerboundCustomPayloadPacket payloadPacket, ServerPlayer player) {
        listeners.get(payloadPacket.getIdentifier()).forEach(listener -> listener.onPacket(payloadPacket.getData(), player));
    }

    public static void onDisconnect(ServerPlayer player) {
        PlayerApi.removePlayerRegistry(player);
        PlayerApi.playersRunningOsmium.remove(player.getStringUUID());
    }


    @FunctionalInterface
    private interface PacketListener {

        void onPacket(FriendlyByteBuf buf, ServerPlayer player);

    }



    public static void sendPacket(ServerPlayer client, ResourceLocation channel, FriendlyByteBuf data) {
        client.connection.send(new ClientboundCustomPayloadPacket(channel, data));
    }

    public static void registerPackets() {
        registerPacketListener(NetworkingConstants.RUNNING_OSMIUM_CLIENT_PACKET_ID, (buf, player) -> {
            PlayerApi.setRunningOsmium(player, true);
            PlayerApi.playersRunningOsmium.put(player.getUUID().toString(), player);
        });


        registerPacketListener(NetworkingConstants.SET_PLAYER_CAPE_SERVER_BOUND, (buf, player) -> {

            if(buf.capacity() > 16384) {
                OsmiumServer.LOGGER.log(Level.INFO, "Player tried sending massive cape set packet");
                return;
            }

            FriendlyByteBuf clientSendByteBuf = createByteBuf();
            clientSendByteBuf.writeUtf(player.getStringUUID());
            clientSendByteBuf.writeBytes(buf);

            for(ServerPlayer client : PlayerApi.playersRunningOsmium.values()) {
                sendPacket(client, NetworkingConstants.SET_PLAYER_CAPE_CLIENT_BOUND, clientSendByteBuf);
            }

            PlayerApi.setPlayerCapeBuffer(player.getStringUUID(), clientSendByteBuf);

        });
    }

    public static FriendlyByteBuf createByteBuf() {
        return new FriendlyByteBuf(Unpooled.buffer());
    }

}
