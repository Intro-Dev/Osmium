package com.intro.common.api;

import com.intro.client.render.cape.Cape;

import java.io.IOException;

public class NonFunctionalOsmiumApi implements OsmiumApi {
    @Override
    public void uploadCapeToServers(Cape cape) throws IOException {
        // No-op
    }

    @Override
    public Cape getCapeFromServers(String uuid) throws IOException {
        // No-op
        return null;
    }


}
