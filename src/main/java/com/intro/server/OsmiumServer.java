package com.intro.server;

import com.intro.common.config.Options;
import com.intro.server.api.OptionApi;
import net.fabricmc.api.DedicatedServerModInitializer;

public class OsmiumServer implements DedicatedServerModInitializer  {
    @Override
    public void onInitializeServer() {
        System.out.println("osmium init");
        Options options = new Options();
        options.setDefaults();
        options.putHashMap();
        options.getBooleanOption(options.NoFireEnabled.identifier).variable = true;
        OptionApi.addSetOption(options.getBooleanOption(options.NoFireEnabled.identifier));
    }
}
