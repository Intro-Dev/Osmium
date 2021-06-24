package com.intro;

import com.intro.module.*;
import com.intro.module.event.Event;
import com.intro.render.CapeHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class Osmium implements ModInitializer {

    public static final String MOD_ID = "osmium";

    public static ArrayList<Module> modules = new ArrayList<Module>();
    public static KeyBinding menuKey;
    public static KeyBinding perspectiveKey;

    ToggleSneak toggleSneak;
    Fullbright fullbright;
    Gui gui;
    ShowEntityHealth showEntityHealth;
    FpsModule fpsModule;
    //BetterEntityCulling betterEntityCulling;
    OptimizationModule optimizationModule;
    CapeHandler handler;


    public void RegisterModules() {
        toggleSneak = new ToggleSneak();
        fullbright = new Fullbright();
        gui = new Gui();
        showEntityHealth = new ShowEntityHealth();
        fpsModule = new FpsModule();
        optimizationModule = new OptimizationModule();
        //betterEntityCulling = new BetterEntityCulling();
        handler = new CapeHandler();
        menuKey = new KeyBinding("keys.osmium.MenuKey", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "keys.category.osmium.keys");
        perspectiveKey = new KeyBinding("keys.osmium.perspectivekey", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "keys.category.osmium.keys");
        KeyBindingHelper.registerKeyBinding(menuKey);
        //KeyBindingHelper.registerKeyBinding(perspectiveKey);
    }

    public void onInitialize() {
        System.out.println("Osmium Initialized");
        RegisterModules();
    }

    public static class EVENT_BUS {
        public static void PostEvent(Event event) {
            for(Module m : Osmium.modules) {
                m.OnEvent(event);
            }
        }
    }
}
