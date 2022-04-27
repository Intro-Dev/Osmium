package com.intro.common.api;

import com.intro.client.OsmiumClient;
import com.intro.client.render.cape.Cape;
import com.intro.common.util.InstanceHolder;
import org.apache.logging.log4j.Level;

import java.io.IOException;

public interface OsmiumApi {
    void uploadCapeToServers(Cape cape) throws IOException;
    Cape getCapeFromServers(String uuid) throws IOException;

    InstanceHolder<OsmiumApi> INSTANCE = new InstanceHolder<>();

    static OsmiumApi getInstance() {
        if (INSTANCE.getInstance() == null) {
            try {
                INSTANCE.setInstance(new OsmiumApiImpl("http://localhost:8080"));
            } catch (Exception e) {
                OsmiumClient.LOGGER.log(Level.ERROR, "Failed to instantiate OsmiumApi, defaulting to non-functional implementation", e);
                INSTANCE.setInstance(new NonFunctionalOsmiumApi());
            }
        }
        return INSTANCE.getInstance();
    }
}
