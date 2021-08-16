package com.intro.server;

import com.intro.server.api.OptionApi;
import com.intro.server.command.CommandManager;
import net.fabricmc.api.DedicatedServerModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OsmiumServer implements DedicatedServerModInitializer  {

    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeServer() {
        CommandManager.registerCommands();
        // ServerNetworkHandler.registerPackets();
        OptionApi.load();
    }
}
