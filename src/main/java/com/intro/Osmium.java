package com.intro;

import com.intro.config.*;
import com.intro.module.*;
import com.intro.module.Module;
import com.intro.module.event.Event;
import com.intro.module.event.EventRender;
import com.intro.module.event.EventTick;
import com.intro.module.event.EventType;
import com.intro.render.CapeHandler;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class Osmium implements ModInitializer {

    public static final String MOD_ID = "osmium";

    public static ArrayList<Module> modules = new ArrayList<Module>();
    public static KeyBinding menuKey;
    public static KeyBinding perspectiveKey;
    public static KeyBinding zoomKey;

    public static Options options = new Options();

    ToggleSneak toggleSneak;
    Fullbright fullbright;
    Gui gui;
    FpsModule fpsModule;
    CapeHandler handler;


    public void RegisterModules() {
        toggleSneak = new ToggleSneak();
        fullbright = new Fullbright();
        gui = new Gui();
        fpsModule = new FpsModule();
        handler = new CapeHandler();
        menuKey = new KeyBinding("keys.osmium.MenuKey", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "keys.category.osmium.keys");
        perspectiveKey = new KeyBinding("keys.osmium.perspectivekey", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_V, "keys.category.osmium.keys");
        zoomKey = new KeyBinding("keys.osmium.zoomkey", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, "keys.category.osmium.keys");
        KeyBindingHelper.registerKeyBinding(menuKey);
    }

    public void onInitialize() {
        OptionUtil.Options.init();
        OptionUtil.load();
        RegisterModules();
        EVENT_BUS.ListenerInit();
        System.out.println("Osmium Initialized");
    }

    public static class EVENT_BUS {

        private static ArrayList<Method> TickListenedMethods = new ArrayList<>();
        private static ArrayList<Method> RenderListenedMethods = new ArrayList<>();




        public static void ListenerInit() {
            for(Module m : Osmium.modules) {
                try{
                    Class<?> c = m.getClass();

                    Method[] methods = c.getMethods();
                    for(Method method : methods) {
                        Annotation anno = method.getAnnotation(EventListener.class);
                        if(anno != null) {
                            for(EventType type : ((EventListener) anno).ListenedEvents()) {
                                switch (type) {
                                    case EVENT_TICK -> TickListenedMethods.add(method);
                                    case EVENT_RENDER -> RenderListenedMethods.add(method);
                                }
                            }
                        }
                    }

                } catch (Exception e){
                    System.out.println(e);
                }
            }
        }


        public static void PostEvent(Event event) {
            /*
             * Tick and render event listeners are cached for performance
             *
             *
             */

            if(event instanceof EventTick) {
                for(Method m : TickListenedMethods) {
                    try {
                        m.invoke(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            if(event instanceof EventRender) {
                for(Method m : RenderListenedMethods) {
                    try {
                        m.invoke(event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
            for(Module m : Osmium.modules) {
                m.OnEvent(event);
            }
        }
    }
}