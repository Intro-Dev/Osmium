package com.intro.server;

import com.intro.common.util.Util;
import com.intro.server.api.OptionApi;
import com.intro.server.command.CommandManager;
import com.intro.server.network.ServerNetworkHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("osmium")
public class OsmiumServer {

    public static final Logger LOGGER = LogManager.getLogger("OsmiumServer");

    public OsmiumServer() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onInitializeServer);
    }

    public void onInitializeServer(final FMLCommonSetupEvent event) {
        CommandManager.registerCommands();
        ServerNetworkHandler.registerPackets();
        OptionApi.load();
        if(!Util.isRunningLatestVersion()) {
            System.out.println("An update for Osmium is available! Download at https://github.com/Intro-Dev/Osmium/releases");
        }
    }
}
