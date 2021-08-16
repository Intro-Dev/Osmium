package com.intro.common.mixin.server;

import com.intro.common.config.OptionSerializer;
import com.intro.common.config.options.Option;
import com.intro.common.network.NetworkingConstants;
import com.intro.server.api.OptionApi;
import com.intro.server.api.PlayerApi;
import com.intro.server.network.ServerNetworkHandler;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Shadow @Final private MinecraftServer server;

    @Inject(method = "placeNewPlayer", at = @At("TAIL"))
    public void placeNewPlayer(Connection netManager, ServerPlayer player, CallbackInfo ci) {

        OptionSerializer serializer = new OptionSerializer();
         ServerNetworkHandler.sendPacket(player, NetworkingConstants.RUNNING_OSMIUM_SERVER_PACKET_ID, PacketByteBufs.create());
         FriendlyByteBuf byteBuf = PacketByteBufs.create();
         byteBuf.writeInt(OptionApi.getServerSetOptions().size());
         System.out.println(OptionApi.getServerSetOptions().size());
         for(Option option : OptionApi.getServerSetOptions()) {
            String serializedOption = serializer.serialize(option, null, null).toString();
            byteBuf.writeUtf(serializedOption);
            }
          ServerNetworkHandler.sendPacket(player, NetworkingConstants.SET_SETTING_PACKET_ID, byteBuf);
         PlayerApi.registerPlayer(player);




    }

}

