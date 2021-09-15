package com.intro.client;

import com.intro.client.module.EventListenerSupplier;
import com.intro.client.module.FullBright;
import com.intro.client.module.Gui;
import com.intro.client.module.ToggleSneak;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventType;
import com.intro.client.network.ClientNetworkHandler;
import com.intro.client.render.RenderManager;
import com.intro.client.render.cape.CapeHandler;
import com.intro.client.render.drawables.PingDisplay;
import com.intro.client.util.OptionUtil;
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

    public static final String MOD_ID = "osmium";

    public static final Logger LOGGER = LogManager.getLogger();

    public static KeyMapping menuKey;

    public static final Options options = new Options();

    public static boolean runningLatestVersion = true;


    public static void registerCallbacks() {
        ToggleSneak toggleSneak = new ToggleSneak();
        FullBright fullbright = new FullBright();
        Gui gui = new Gui();
        CapeHandler handler = new CapeHandler();

        EVENT_BUS.registerCallback(toggleSneak::onEvent, EventType.EVENT_TICK);
        EVENT_BUS.registerCallback(fullbright::onEvent, EventType.EVENT_TICK);
        EVENT_BUS.registerCallback(gui::onEvent, EventType.EVENT_TICK);
        EVENT_BUS.registerCallback(handler::handleEvents, new EventType[] { EventType.EVENT_ADD_PLAYER, EventType.EVENT_REMOVE_PLAYER } );
        EVENT_BUS.registerCallback(PingDisplay.getInstance()::onEvent, EventType.EVENT_TICK);
        EVENT_BUS.registerCallback(handler::tickCapes, EventType.EVENT_TICK);
    }

    public void registerKeyBindings() {
        menuKey = new KeyMapping("keys.osmium.MenuKey", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "keys.category.osmium.keys");
        KeyBindingHelper.registerKeyBinding(menuKey);
    }


    public void onInitializeClient() {
        OptionUtil.Options.init();
        OptionUtil.load();
        EVENT_BUS.initListenerMap();
        registerCallbacks();
        registerKeyBindings();
        ClientNetworkHandler.registerPackets();
        RenderManager.initDrawables();
        runningLatestVersion = Util.isRunningLatestVersion();
        System.out.println("Osmium Initialized");
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