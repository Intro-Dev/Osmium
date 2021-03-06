package com.intro.common.api;

import com.intro.client.OsmiumClient;
import com.intro.client.render.cosmetic.Cape;
import com.intro.common.util.InstanceHolder;
import com.mojang.blaze3d.platform.NativeImage;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.util.Map;

public interface OsmiumApi {

    void setServerSideCape(Cape cape) throws IOException;
    NativeImage getCapeTextureFromServers(String uuid) throws IOException;
    Map<String, ?> getCapeDataFromServers(String uuid) throws IOException;
    void sendKeepAlive() throws IOException;

    InstanceHolder<OsmiumApi> INSTANCE = new InstanceHolder<>();

    static OsmiumApi getInstance() {
        if (INSTANCE.getInstance() == null) {
            try {
                INSTANCE.setInstance(new OsmiumApiImpl("https://lobstershack.dev:443"));
            } catch (Exception e) {
                OsmiumClient.LOGGER.log(Level.ERROR, "Failed to instantiate OsmiumApi, defaulting to non-functional implementation", e);
                INSTANCE.setInstance(new NonFunctionalOsmiumApi());
            }
        }
        return INSTANCE.getInstance();
    }

    static boolean isFunctional() {
        return INSTANCE.getInstance() instanceof OsmiumApiImpl;
    }
}
