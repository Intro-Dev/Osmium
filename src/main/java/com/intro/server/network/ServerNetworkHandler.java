package com.intro.server.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class ServerNetworkHandler {

    public static void sendPacket(ServerPlayer client, ResourceLocation channel, FriendlyByteBuf data) {
        ServerPlayNetworking.send(client, channel, data);
    }

}
