package dev.lobstershack.server;

import dev.lobstershack.common.util.Util;
import dev.lobstershack.server.api.OptionApi;
import dev.lobstershack.server.command.OsmiumCommandManager;
import dev.lobstershack.server.network.ServerNetworkHandler;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OsmiumServer implements DedicatedServerModInitializer  {

    public static final Logger LOGGER = LogManager.getLogger("OsmiumServer");

    @Override
    public void onInitializeServer() {
        OsmiumCommandManager.registerCommands();
        ServerNetworkHandler.registerPackets();
        OptionApi.load();
        if(!Util.isRunningLatestVersion()) {
            System.out.println("An update for Osmium is available! Download at https://github.com/Intro-Dev/Osmium/releases");
        }
    }
}
