package com.intro.client.network;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intro.client.OsmiumClient;
import com.intro.common.config.OptionDeserializer;
import com.intro.common.config.OptionSerializer;
import com.intro.common.config.options.BooleanOption;
import com.intro.common.config.options.Option;
import com.intro.common.network.NetworkingConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import org.apache.logging.log4j.Level;

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
            .registerTypeAdapter(Option.class, new OptionSerializer())
            .registerTypeAdapter(Option.class, new OptionDeserializer())
            .create();

    public static void registerPackets() {
        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.SET_SETTING_PACKET_ID, (client, handler, buf, responseSender) -> {
            BooleanOption booleanOption = OsmiumClient.options.getBooleanOption(OsmiumClient.options.NoFireEnabled.identifier);
            System.out.println(booleanOption.identifier);
            System.out.println(booleanOption.variable);
            // what
            // deserializing an option here sets the option to true
            // no reason for it
            // just does
            String utf = buf.readUtf();
            Option option = GSON.fromJson(utf, Option.class);
            booleanOption = OsmiumClient.options.getBooleanOption(OsmiumClient.options.NoFireEnabled.identifier);
            System.out.println(booleanOption.identifier);
            System.out.println(booleanOption.variable);


            try {
                OsmiumClient.options.overwrittenOptions.put(option.identifier, OsmiumClient.options.get(option.identifier));
                OsmiumClient.options.put(option.identifier, option);
            } catch (Exception e) {
                OsmiumClient.LOGGER.log(Level.WARN, "Received invalid option data from server!");
            }


        });

        ClientPlayNetworking.registerGlobalReceiver(NetworkingConstants.RUNNING_OSMIUM_SERVER_PACKET_ID, (client, handler, buf, responseSender) -> {
            isRunningOsmiumServer = true;
            sendToast(Minecraft.getInstance(), new TranslatableComponent("osmium.toast.running_osmium_server"), new TranslatableComponent("osmium.toast.settings_change"));
        });

        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
            isRunningOsmiumServer = false;
            System.out.println(OsmiumClient.options.overwrittenOptions.size());
            for(Option option : OsmiumClient.options.overwrittenOptions.values()) {
                System.out.println(option.identifier);
                System.out.println(((BooleanOption) option).variable);

                OsmiumClient.options.put(option.identifier, option);
                System.out.println(option);
                System.out.println(OsmiumClient.options.get(option.identifier));
            }
            OsmiumClient.options.getHashMap();
            System.out.println(((BooleanOption) OsmiumClient.options.get(OsmiumClient.options.NoFireEnabled.identifier)).variable);
            OsmiumClient.options.overwrittenOptions.clear();
        });
    }
}
