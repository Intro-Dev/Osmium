package dev.lobstershack.common.api;

import com.mojang.blaze3d.platform.NativeImage;
import dev.lobstershack.client.render.cosmetic.Cape;

import java.util.Map;

public class NonFunctionalOsmiumApi implements OsmiumApi {
    @Override
    public void setServerSideCape(Cape cape) {}

    @Override
    public NativeImage getCapeTextureFromServers(String uuid) {
        return null;
    }

    @Override
    public Map<String, ?> getCapeDataFromServers(String uuid) {
        return null;
    }

    @Override
    public void sendKeepAlive() {}


}
