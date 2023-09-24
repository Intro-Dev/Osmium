package dev.lobstershack.client.api;

import com.mojang.blaze3d.platform.NativeImage;
import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.client.render.cosmetic.Cape;
import dev.lobstershack.client.util.InstanceHolder;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public interface OsmiumApi {

    void setServerSideCape(Cape cape) throws IOException;
    NativeImage getCapeTextureFromServers(UUID uuid) throws IOException;
    Map<String, ?> getCapeDataFromServers(UUID uuid) throws IOException;
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
