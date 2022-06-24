package com.intro.common.api;

import com.intro.client.render.cosmetic.Cape;
import com.mojang.blaze3d.platform.NativeImage;

import java.util.Map;

public class NonFunctionalOsmiumApi implements OsmiumApi {
    @Override
    public void setServerSideCape(Cape cape) {

    }

    @Override
    public NativeImage getCapeTextureFromServers(String uuid) {
        return null;
    }

    @Override
    public Map<String, ?> getCapeDataFromServers(String uuid) {
        return null;
    }


}
