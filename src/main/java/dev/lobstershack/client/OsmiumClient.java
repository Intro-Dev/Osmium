package dev.lobstershack.client;

import com.mojang.blaze3d.platform.InputConstants;
import dev.lobstershack.client.api.OsmiumApi;
import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.feature.AutoGG;
import dev.lobstershack.client.feature.Gui;
import dev.lobstershack.client.render.cosmetic.CosmeticManager;
import dev.lobstershack.client.render.widget.drawable.DrawableRenderer;
import dev.lobstershack.client.render.widget.drawable.PingDisplay;
import dev.lobstershack.client.render.widget.drawable.ToggleSneak;
import dev.lobstershack.client.util.DebugUtil;
import dev.lobstershack.client.util.Util;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class OsmiumClient implements ClientModInitializer {

    public static final Logger LOGGER = LogManager.getLogger("OsmiumClient");

    public static KeyMapping menuKey;
    public static KeyMapping freeLookKey;

    public static boolean runningLatestVersion = true;

    public static CosmeticManager cosmeticManager = new CosmeticManager();

    public static void registerCallbacks() {
        ToggleSneak.getInstance().registerEventListeners();
        Gui.getInstance().registerEventListeners();
        cosmeticManager.registerEventListeners();
        PingDisplay.getInstance().registerEventListeners();
        AutoGG.registerEventListeners();
    }

    public void registerKeyBindings() {
        menuKey = new KeyMapping("keys.osmium.MenuKey", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "keys.category.osmium.keys");
        freeLookKey = new KeyMapping("keys.osmium.FreeLookKey", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_V, "keys.category.osmium.keys");
        KeyBindingHelper.registerKeyBinding(menuKey);
        KeyBindingHelper.registerKeyBinding(freeLookKey);
        Options.FreeLookEnabled.set(true);
    }


    public void onInitializeClient() {
        if(Boolean.parseBoolean(System.getProperty("osmium.debug"))) {{
            DebugUtil.enableDebugMode();
            DebugUtil.logIfDebug("Starting Osmium in debug mode", Level.INFO);
            DebugUtil.logIfDebug("If you don't want to see debug messages, change -Dosmium.debug=true to -Dosmium.debug=false in your launch parameters", Level.INFO);
        }}
        DebugUtil.initDebugCommands();
        Options.load();
        registerCallbacks();
        registerKeyBindings();
        DrawableRenderer.initDrawables();
        AutoGG.setupTriggers();
        runningLatestVersion = Util.isRunningLatestVersion();
        OsmiumApi.getInstance();
        DebugUtil.logIfDebug("Osmium Initialized", Level.INFO);
    }


}