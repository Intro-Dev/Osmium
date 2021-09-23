package com.intro.client.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventCustomPacket;
import com.intro.client.module.event.EventType;
import com.intro.client.render.cape.Cape;
import com.intro.client.render.cape.CosmeticManager;
import com.intro.client.util.OptionUtil;
import com.intro.common.config.OptionDeserializer;
import com.intro.common.config.OptionSerializer;
import com.intro.common.config.options.Option;
import com.intro.common.network.NetworkingConstants;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientNetworkHandler {

    public static boolean isRunningOsmiumServer = false;

    private static HashMap<ResourceLocation, ArrayList<PacketListener>> listeners = new HashMap<>();

    public static void registerPacketListener(ResourceLocation channel, PacketListener listener) {
        listeners.computeIfAbsent(channel, k -> new ArrayList<>());
        listeners.get(channel).add(listener);
    }

    public static void handlePacketEvent(Event event) {
        EventCustomPacket customPacket = (EventCustomPacket) event;
        listeners.computeIfAbsent(customPacket.getPayload().getIdentifier(), k -> new ArrayList<>());
        listeners.get(customPacket.getPayload().getIdentifier()).forEach(listener -> listener.onPacket(Minecraft.getInstance(), customPacket.getPayload().getData()));
    }

    private static void sendToast(Minecraft client, Component title, Component description) {
        client.getToasts().addToast(SystemToast.multiline(client, SystemToast.SystemToastIds.TUTORIAL_HINT, title, description));
    }

    private static void send(ResourceLocation channel, FriendlyByteBuf byteBuf) {
        Minecraft.getInstance().getConnection().send(new ServerboundCustomPayloadPacket(channel, byteBuf));
    }

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .registerTypeAdapter(Option.class, new OptionSerializer())
            .registerTypeAdapter(Option.class, new OptionDeserializer())
            .create();

    public static void registerPackets() {
        registerPacketListener(NetworkingConstants.SET_SETTING_PACKET_ID, (client, buf) -> {
            OptionUtil.setNormalOptions();
            int setCount = buf.readInt();
            for(int i = 0; i < setCount; i++) {
                try {
                    String utf = buf.readUtf();
                    Option option = GSON.fromJson(utf, Option.class);
                    OsmiumClient.options.putOverwrittenOption(option.identifier, OsmiumClient.options.get(option.identifier));
                    OsmiumClient.options.put(option.identifier, option);
                } catch (Exception e) {
                    e.printStackTrace();
                    OsmiumClient.LOGGER.log(Level.WARN, "Received invalid option data from server!");
                }
            }
        });

        registerPacketListener(NetworkingConstants.RUNNING_OSMIUM_SERVER_PACKET_ID, (client, buf) -> {
            isRunningOsmiumServer = true;
            sendToast(Minecraft.getInstance(), new TranslatableComponent("osmium.toast.running_osmium_server"), new TranslatableComponent("osmium.toast.settings_change"));

        });

        OsmiumClient.EVENT_BUS.registerCallback(((event) -> {
            if(isRunningOsmiumServer) {
                send(NetworkingConstants.RUNNING_OSMIUM_CLIENT_PACKET_ID, create());
                try {
                    if(CosmeticManager.getPreLoadedPlayerCape() != null) {
                        CosmeticManager.playerCapes.put(Minecraft.getInstance().player.getStringUUID(), CosmeticManager.getPreLoadedPlayerCape());
                        sendCapeSetPacket(CosmeticManager.playerCapes.get(Minecraft.getInstance().player.getStringUUID()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }), EventType.EVENT_JOIN_WORLD);

        OsmiumClient.EVENT_BUS.registerCallback(((event) -> {
            isRunningOsmiumServer = false;
            for(Option option : OsmiumClient.options.getOverwrittenOptions().values()) {
                OsmiumClient.options.put(option.identifier, option);
            }
            OsmiumClient.options.clearOverwrittenOptions();
        }), EventType.EVENT_DISCONNECT);

        registerPacketListener(NetworkingConstants.SET_PLAYER_CAPE_CLIENT_BOUND, (client, buf) -> {
            try {
                String uuid = buf.readUtf();
                Cape playerCape = CosmeticManager.readCapeFromByteBuf(buf);
                CosmeticManager.playerCapes.put(uuid, playerCape);
            } catch (Exception e) {
                e.printStackTrace();
                sendToast(Minecraft.getInstance(), new TranslatableComponent("osmium_failed_cape_load_title"), new TranslatableComponent("osmium_failed_cape_load"));
            }
        });
    }

        public static void sendCapeSetPacket(Cape cape) throws IOException {
        FriendlyByteBuf byteBuf = create();
        byteBuf.writeUtf(cape.creator);
        byteBuf.writeUtf(cape.registryName);
        byteBuf.writeBoolean(cape.isAnimated);
        byteBuf.writeInt(cape.getTexture().getFrameDelay());

        byte[] imageData = cape.getTexture().image.asByteArray();

        byteBuf.writeBytes(imageData);
        send(NetworkingConstants.SET_PLAYER_CAPE_SERVER_BOUND, byteBuf);
    }

    public static FriendlyByteBuf create() {
        return new FriendlyByteBuf(Unpooled.buffer());
    }

    @FunctionalInterface
    private interface PacketListener {

        void onPacket(Minecraft client, FriendlyByteBuf buf);

    }

}
