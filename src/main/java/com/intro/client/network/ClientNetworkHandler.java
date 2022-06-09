package com.intro.client.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intro.client.OsmiumClient;
import com.intro.client.render.cape.Cape;
import com.intro.client.render.cape.CosmeticManager;
import com.intro.client.util.OptionUtil;
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
            OptionUtil.setNormalOptions();
            int setCount = buf.readInt();
            for(int i = 0; i < setCount; i++) {
                try {
                    String utf = buf.readUtf();
                    Option<?> option = GSON.fromJson(utf, Option.class);
                    OsmiumClient.options.putOverwrittenOption(option.getIdentifier(), OsmiumClient.options.get(option.getIdentifier()));
                    OsmiumClient.options.put(option.getIdentifier(), option);
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
                try {
                    if(CosmeticManager.getPreLoadedPlayerCape() != null) {
                        CosmeticManager.playerCapes.put(Minecraft.getInstance().player.getStringUUID(), CosmeticManager.getPreLoadedPlayerCape());
                        sendCapeSetPacket(CosmeticManager.playerCapes.get(Minecraft.getInstance().player.getStringUUID()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            isRunningOsmiumServer = false;
            for(Option<?> option : OsmiumClient.options.getOverwrittenOptions().values()) {
                OsmiumClient.options.put(option.getIdentifier(), option);
            }
            OsmiumClient.options.clearOverwrittenOptions();
        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.SET_PLAYER_CAPE_CLIENT_BOUND, (client, handler, buf, responseSender) -> {
            try {
                String uuid = buf.readUtf();
                Cape playerCape = CosmeticManager.readCapeFromByteBuf(buf);
                CosmeticManager.playerCapes.put(uuid, playerCape);
            } catch (Exception e) {
                e.printStackTrace();
                sendToast(Minecraft.getInstance(), Component.translatable("osmium_failed_cape_load_title"), Component.translatable("osmium_failed_cape_load"));
            }
        });
    }

    public static void sendCapeSetPacket(Cape cape) throws IOException {
        if(isRunningOsmiumServer) {
            FriendlyByteBuf byteBuf = PacketByteBufs.create();
            byteBuf.writeUtf(cape.creator);
            byteBuf.writeUtf(cape.registryName);
            byteBuf.writeBoolean(cape.isAnimated);
            byteBuf.writeInt(cape.getTexture().getFrameDelay());
            byte[] imageData = cape.getTexture().image.asByteArray();
            byteBuf.writeBytes(imageData);
            ClientPlayNetworking.send(NetworkingConstants.SET_PLAYER_CAPE_SERVER_BOUND, byteBuf);
        }
    }


}
