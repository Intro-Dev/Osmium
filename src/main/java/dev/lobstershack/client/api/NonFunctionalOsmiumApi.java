package dev.lobstershack.client.api;

import com.mojang.blaze3d.platform.NativeImage;
import dev.lobstershack.client.render.cosmetic.Cape;

import java.util.Map;
import java.util.UUID;

public class NonFunctionalOsmiumApi implements OsmiumApi {
    @Override
    public void setServerSideCape(Cape cape) {}

    @Override
    public NativeImage getCapeTextureFromServers(UUID uuid) {
        return null;
    }

    @Override
    public Map<String, ?> getCapeDataFromServers(UUID uuid) {
        return null;
    }

    @Override
    public void sendKeepAlive() {}


}
