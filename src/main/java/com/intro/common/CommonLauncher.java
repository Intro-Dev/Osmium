package com.intro.common;

import com.intro.client.OsmiumClient;
import com.intro.server.OsmiumServer;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

/**
 * provides an abstraction layer between the mod launchers and forge
 * not having this causes crashes due to forge being weird
 */
@Mod("osmium")
public class CommonLauncher {

    private static EnvType environment;

    public CommonLauncher() {
        try {
            Minecraft mc = Minecraft.getInstance();
            environment = EnvType.CLIENT;
        } catch (RuntimeException e) {
            environment = EnvType.SERVER;
        }

        switch (environment) {
            case CLIENT -> launchClient();
            case SERVER -> launchServer();
        }
    }

    public void launchClient() {
        OsmiumClient.onInitializeClient();
    }

    public void launchServer() {
        OsmiumServer.onInitializeServer();
    }

    public static EnvType getEnvironment() {
        return environment;
    }

    private enum EnvType {
        CLIENT,
        SERVER
    }
}
