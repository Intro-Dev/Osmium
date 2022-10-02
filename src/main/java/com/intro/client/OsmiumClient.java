package com.intro.client;

import com.intro.client.module.AutoGG;
import com.intro.client.module.EventListenerSupplier;
import com.intro.client.module.Gui;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventType;
import com.intro.client.network.ClientNetworkHandler;
import com.intro.client.render.RenderManager;
import com.intro.client.render.cosmetic.CosmeticManager;
import com.intro.client.render.drawables.PingDisplay;
import com.intro.client.render.drawables.ToggleSneak;
import com.intro.client.util.DebugUtil;
import com.intro.client.util.HypixelAbstractionLayer;
import com.intro.common.api.OsmiumApi;
import com.intro.common.config.Options;
import com.intro.common.util.Util;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;

public class OsmiumClient implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("OsmiumClient");

    public static KeyMapping menuKey;

    public static boolean runningLatestVersion = true;

    public static CosmeticManager cosmeticManager = new CosmeticManager();

    public static void registerCallbacks() {
        Gui gui = new Gui();

        EVENT_BUS.registerCallback(ToggleSneak.getInstance()::onEvent, EventType.EVENT_TICK);
        EVENT_BUS.registerCallback(gui::onEvent, EventType.EVENT_TICK);
        EVENT_BUS.registerCallback(cosmeticManager::handleEvents, new EventType[] { EventType.EVENT_ADD_PLAYER, EventType.EVENT_REMOVE_PLAYER, EventType.EVENT_TICK } );
        EVENT_BUS.registerCallback(PingDisplay.getInstance()::onEvent, EventType.EVENT_TICK);
        EVENT_BUS.registerCallback(HypixelAbstractionLayer::handleDisconnectEvents, EventType.EVENT_REMOVE_PLAYER);
        EVENT_BUS.registerCallback(AutoGG::onEvent, EventType.EVENT_RECEIVE_CHAT_MESSAGE);
    }

    public void registerKeyBindings() {
        menuKey = new KeyMapping("keys.osmium.MenuKey", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "keys.category.osmium.keys");
        KeyBindingHelper.registerKeyBinding(menuKey);
    }


    public void onInitializeClient() {
        DebugUtil.DEBUG = Boolean.parseBoolean(System.getProperty("osmium.debug"));
        if(DebugUtil.DEBUG) LOGGER.info("Starting Osmium in debug mode");
        Options.load();
        EVENT_BUS.initListenerMap();
        registerCallbacks();
        registerKeyBindings();
        ClientNetworkHandler.registerPackets();
        RenderManager.initDrawables();
        HypixelAbstractionLayer.loadApiKey();
        AutoGG.setupTriggers();
        runningLatestVersion = Util.isRunningLatestVersion();
        OsmiumApi.getInstance();
        LOGGER.info("Osmium Initialized");
    }


    public static class EVENT_BUS {

        public static final HashMap<Integer, ArrayList<EventListenerSupplier>> mappedListeners = new HashMap<>();

        public static void initListenerMap() {
            for(EventType eventType : EventType.values()) {
                mappedListeners.put(eventType.getIntVal(), new ArrayList<>());
            }
        }

        public static void registerCallback(EventListenerSupplier supplier, EventType event) {
            ArrayList<EventListenerSupplier> list = mappedListeners.get(event.getIntVal());
            list.add(supplier);
            mappedListeners.put(event.getIntVal(), list);
        }

        public static void registerCallback(EventListenerSupplier supplier, EventType[] events) {
            for(EventType event : events) {
                ArrayList<EventListenerSupplier> list = mappedListeners.get(event.getIntVal());
                list.add(supplier);
                mappedListeners.put(event.getIntVal(), list);
            }
        }

        public static void postEvent(Event event, EventType type) {
           for(EventListenerSupplier supplier : mappedListeners.get(type.getIntVal())) {
               supplier.run(event);
           }
        }

    }
}