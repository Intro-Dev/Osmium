package com.intro;

import com.intro.config.OptionUtil;
import com.intro.config.Options;
import com.intro.module.Module;
import com.intro.module.*;
import com.intro.module.event.Event;
import com.intro.module.event.EventRender;
import com.intro.module.event.EventTick;
import com.intro.module.event.EventType;
import com.intro.render.CapeHandler;
import com.intro.render.shader.ShaderSystem;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

public class Osmium implements ModInitializer {

    public static final String MOD_ID = "osmium";

    public static final Logger LOGGER = LogManager.getLogger();

    public static ArrayList<Module> modules = new ArrayList<Module>();
    public static KeyBinding menuKey;
    public static KeyBinding perspectiveKey;
    public static KeyBinding zoomKey;

    public static Options options = new Options();

    public static ToggleSneak toggleSneak;
    public static Fullbright fullbright;
    public static Gui gui;
    public static FpsModule fpsModule;
    public static CapeHandler handler;


    public static void registerModules() {
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

    // Shaders are temporarily disabled until i can get them working
    public static void registerShaders() {
        ShaderSystem.loadShader(new Identifier("osmium", "shaders/post/blur.json"));
    }

    public void onInitialize() {
        OptionUtil.Options.init();
        OptionUtil.load();
        registerModules();
        // registerShaders();
        EVENT_BUS.ListenerInit();
        System.out.println("Osmium Initialized");

    }

    public static class EVENT_BUS {

        private static final ArrayList<MethodContainer> TickListenedMethods = new ArrayList<>();
        private static final ArrayList<MethodContainer> RenderListenedMethods = new ArrayList<>();

        public static ArrayList<MethodContainer> containers = new ArrayList<>();




        public static void ListenerInit() {
            containers.clear();
            for(Module m : Osmium.modules) {
                try{
                    Class<?> c = m.getClass();

                    Method[] methods = c.getMethods();
                    for(Method method : methods) {
                        EventListener anno = method.getAnnotation(EventListener.class);
                        if(anno != null) {
                            containers.add(new MethodContainer(m, method, anno.ListenedEvents()));
                        }
                    }

                } catch (Exception e){
                    System.out.println(e);
                }
            }
            for(MethodContainer container : containers) {
                if(Arrays.stream(container.type).toList().contains(EventType.EVENT_TICK)) {
                    TickListenedMethods.add(container);
                }
                if(Arrays.stream(container.type).toList().contains(EventType.EVENT_RENDER)) {
                    RenderListenedMethods.add(container);
                }
            }
        }


        public static void postEvent(Event event) {
           /*
           So it turns out that trying to optimise like this just makes it slower
           Ill make this work later
           */
            if(event instanceof EventTick) {
                for(MethodContainer m : TickListenedMethods) {
                    try {
                        m.method.setAccessible(true);
                        m.method.invoke(m.module, event);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
           if(event instanceof EventRender) {
                for(MethodContainer m : RenderListenedMethods) {
                    try {
                        m.method.setAccessible(true);
                        m.method.invoke(m.method, event);
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