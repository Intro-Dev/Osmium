package com.intro.client.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intro.client.OsmiumClient;
import com.intro.client.render.cosmetic.Cape;
import com.intro.common.config.Options;
import com.intro.common.config.options.Option;
import com.intro.common.config.options.legacy.LegacyOption;
import com.intro.common.config.options.legacy.LegacyOptionDeserializer;
import com.intro.common.config.options.legacy.LegacyOptionSerializer;
import com.intro.common.network.NetworkingConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.lang.reflect.Modifier;

public class ClientNetworkHandler {

    public static boolean isRunningOsmiumServer = false;


    private static void sendToast(Minecraft client, Component title, Component description) {
        client.getToasts().addToast(SystemToast.multiline(client, SystemToast.SystemToastIds.TUTORIAL_HINT, title, description));
    }

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .registerTypeAdapter(LegacyOption.class, new LegacyOptionSerializer())
            .registerTypeAdapter(LegacyOption.class, new LegacyOptionDeserializer())
            .create();

    public static void registerPackets() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.SET_SETTING_PACKET_ID, (client, handler, buf, responseSender) -> {
            Options.setNormalOptions();
            int setCount = buf.readInt();
            for(int i = 0; i < setCount; i++) {
                try {
                    String utf = buf.readUtf();
                    Option<?> option = GSON.fromJson(utf, Option.class);
                    Options.putOverwrittenOption(option.getIdentifier(), Options.get(option.getIdentifier()));
                    Options.put(option.getIdentifier(), option);
                } catch (Exception e) {
                    e.printStackTrace();
                    OsmiumClient.LOGGER.log(Level.WARN, "Received invalid option data from server!");
                }
            }
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.RUNNING_OSMIUM_SERVER_PACKET_ID, (client, handler, buf, responseSender) -> {
            isRunningOsmiumServer = true;
            sendToast(Minecraft.getInstance(), Component.translatable("osmium.toast.running_osmium_server"), Component.translatable("osmium.toast.settings_change"));

        });

        ClientPlayConnectionEvents.JOIN.register((a, b, c) -> {
            if(isRunningOsmiumServer) {
                ClientPlayNetworking.send(NetworkingConstants.RUNNING_OSMIUM_CLIENT_PACKET_ID, PacketByteBufs.create());

            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            isRunningOsmiumServer = false;
            for(Option<?> option : Options.getOverwrittenOptions().values()) {
                Options.put(option.getIdentifier(), option);
            }
            Options.clearOverwrittenOptions();
        });

    }

    public static void sendCapeSetPacket(Cape cape) throws IOException {
        if(isRunningOsmiumServer) {
            FriendlyByteBuf byteBuf = PacketByteBufs.create();
            byteBuf.writeUtf(cape.creator);
            byteBuf.writeUtf(cape.name);
            byteBuf.writeBoolean(cape.animated);
            byteBuf.writeInt(cape.getTexture().getFrameDelay());
            byte[] imageData = cape.getTexture().image.asByteArray();
            byteBuf.writeBytes(imageData);
            ClientPlayNetworking.send(NetworkingConstants.SET_PLAYER_CAPE_SERVER_BOUND, byteBuf);
        }
    }


}
