package com.intro;

import com.intro.config.Options;
import com.intro.module.*;
import com.intro.module.event.Event;
import com.intro.module.event.EventType;
import com.intro.render.CapeHandler;
import com.intro.render.RenderManager;
import com.intro.render.shader.ShaderSystem;
import com.intro.util.OptionUtil;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.HashMap;

public class Osmium implements ModInitializer {

    public static final String MOD_ID = "osmium";

    public static final Logger LOGGER = LogManager.getLogger();

    public static KeyBinding menuKey;

    public static Options options = new Options();




    public static void registerModules() {
        ToggleSneak toggleSneak = new ToggleSneak();
        FullBright fullbright = new FullBright();
        Gui gui = new Gui();
        FpsModule fpsModule = new FpsModule();
        CapeHandler handler = new CapeHandler();

        EVENT_BUS.registerCallback(toggleSneak::onEvent, EventType.EVENT_TICK);
        EVENT_BUS.registerCallback(fullbright::onEvent, EventType.EVENT_TICK);
        EVENT_BUS.registerCallback(gui::onEvent, EventType.EVENT_TICK);
        EVENT_BUS.registerCallback(handler::handleEvents, new EventType[] { EventType.EVENT_ADD_PLAYER, EventType.EVENT_REMOVE_PLAYER } );
        EVENT_BUS.registerCallback(fpsModule::onEvent, EventType.EVENT_TICK);

    }

    public void registerKeyBindings() {
        menuKey = new KeyBinding("keys.osmium.MenuKey", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "keys.category.osmium.keys");
        KeyBindingHelper.registerKeyBinding(menuKey);
    }

    // Shaders are temporarily disabled until I can get them working
    public static void registerShaders() {
        ShaderSystem.loadShader(new Identifier("osmium", "shaders/post/blur.json"));
    }

    public void onInitialize() {
        OptionUtil.Options.init();
        OptionUtil.load();
        EVENT_BUS.initListenerMap();
        registerModules();
        registerKeyBindings();
        RenderManager.initDrawables();
        // registerShaders();
        System.out.println("Osmium Initialized");

    }

    public static class EVENT_BUS {

        public static HashMap<Integer, ArrayList<EventListenerSupplier>> mappedListeners = new HashMap<>();

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