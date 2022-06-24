package com.intro.client.render.cosmetic;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventAddPlayer;
import com.intro.client.module.event.EventRemovePlayer;
import com.intro.client.module.event.EventTick;
import com.intro.client.render.texture.DynamicAnimation;
import com.intro.client.util.ExecutionUtil;
import com.intro.client.util.TextureUtil;
import com.intro.common.api.OsmiumApi;
import com.intro.common.config.Options;
import com.intro.common.config.options.CapeRenderingMode;
import com.intro.common.config.options.Option;
import com.intro.common.util.HttpRequester;
import com.intro.common.util.Util;
import com.mojang.blaze3d.platform.NativeImage;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipFile;

/**
 * <p>This class contains all code for managing cosmetics</p>
 * <p>Previously done by CapeHandler</p>
 *
 * @since 1.2.3
 * @author Intro
 * @see Cape
 * @see CapeRenderer
 */
public class CosmeticManager {

    private final HashMap<String, Cape> playerCapes = new HashMap<>();

    private final HashSet<Cape> locallyLoadedCapes = new HashSet<>();

    public void loadLocalCapes() {
        locallyLoadedCapes.clear();
        try {
            Map<String, ?> defaultCapeParams = ImmutableMap.of("animated", Boolean.TRUE, "creator", "Lobster", "name", "Osmium Logo Cape", "frame_delay", 1);
            locallyLoadedCapes.add(capeFromMapAndTexture(defaultCapeParams, TextureUtil.getImageAtLocation(new ResourceLocation("osmium", "textures/cape/osmium_logo_cape.png")), "local"));
            File cosmeticsDir = FabricLoader.getInstance().getGameDir().resolve("cosmetics").toFile();
            if(!Files.exists(cosmeticsDir.toPath())) {
                Files.createDirectory(cosmeticsDir.toPath());
            }
            for(File file : Objects.requireNonNull(cosmeticsDir.listFiles())) {
                if(!file.isDirectory() && file.toString().endsWith(".zip")) {
                    HashSet<Cape> loaded = loadCapesFromZip(new ZipFile(file, ZipFile.OPEN_READ));
                    locallyLoadedCapes.addAll(loaded);
                }
            }
        } catch (Exception e) {
            OsmiumClient.LOGGER.log(Level.WARN, "Failed to load local capes " + e.getMessage());
        }
        locallyLoadedCapes.forEach((cape -> {
            if(cape.name.equals(OsmiumClient.options.get(Options.SetCape).get())) {
                setLocalPlayerCape(cape);
            }
        }));
    }

    /**
     * Should only be used on local player
     * @param cape Cape
     */
    public void setLocalPlayerCape(Cape cape) {
        playerCapes.put(Minecraft.getInstance().user.getUuid().toLowerCase(), cape);
        ExecutionUtil.submitTask(() -> {
            try {
                OsmiumApi.getInstance().setServerSideCape(cape);
            } catch (IOException e) {
                OsmiumClient.LOGGER.log(Level.WARN, "Unable to set cape on Osmium servers: " + e.getMessage());
            }
        });
        OsmiumClient.options.put(Options.SetCape, new Option<>(Options.SetCape, cape.name));
    }

    public HashSet<Cape> getLocalCapes() {
        return locallyLoadedCapes;
    }

    public void handleEvents(Event event) {
        if(event instanceof EventTick && OsmiumClient.options.getBooleanOption(Options.AnimateCapes).get()) {
            for(Cape cape : playerCapes.values()) {
                cape.nextFrame();
            }
        }

        if(event instanceof EventAddPlayer addPlayer) {
            ExecutionUtil.submitTask(() -> {
                try {
                    downloadPlayerCape(addPlayer.entity);
                } catch (Exception e) {
                    OsmiumClient.LOGGER.log(Level.WARN, "Failed downloading player cape: " + e.getMessage());
                }
            });
        }

        if(event instanceof EventRemovePlayer removePlayer) {
            Cape cape = playerCapes.get(removePlayer.entity.getStringUUID());
            cape.free();
            playerCapes.remove(removePlayer.entity.getStringUUID().replaceAll("-", ""));
        }
    }

    public Cape getPlayerCape(String UUID) {
        Cape cape = playerCapes.get(UUID);
        if(cape == null) return null;
        if(OsmiumClient.options.getEnumOption(Options.CustomCapeMode).get() == CapeRenderingMode.ALL) return cape;
        if((OsmiumClient.options.getEnumOption(Options.CustomCapeMode).get() == CapeRenderingMode.OPTIFINE) && cape.isOptifine) return cape;
        return null;
    }

    public void downloadPlayerCape(Player player) throws IOException {
        boolean hasOptifine = false, hasOsmium = false;
        try {
            // will throw error if optifine cape isn't found
            HttpRequester.fetch(new HttpRequester.HttpRequest("http://s.optifine.net/capes/" + player.getName().getString() + ".png", "GET", Collections.emptyMap(), Collections.emptyMap()));
            hasOptifine = true;
            OsmiumApi.getInstance().getCapeDataFromServers(player.getStringUUID());
            hasOsmium = true;
        } catch (IOException ignored) {}

        // if else gore but oh well
        if(OsmiumClient.options.getEnumOption(Options.CustomCapeMode).get() == CapeRenderingMode.ALL || OsmiumClient.options.getEnumOption(Options.CustomCapeMode).get() == CapeRenderingMode.OPTIFINE) {
            if(OsmiumClient.options.getEnumOption(Options.CustomCapeMode).get() == CapeRenderingMode.OPTIFINE && hasOptifine) {
                playerCapes.put(player.getStringUUID().replaceAll("-", ""), deserializeOptifineCape(player.getName().getString()));
                return;
            }
            if(!hasOsmium && hasOptifine) {
                playerCapes.put(player.getStringUUID().replaceAll("-", ""), deserializeOptifineCape(player.getName().getString()));
                return;
            }
            if(hasOsmium) {
                playerCapes.put(player.getStringUUID().replaceAll("-", ""), deserializeOsmiumCape(player.getStringUUID().replaceAll("-", "")));
            }
        }
    }

    public Cape deserializeSimpleCape(String capeUrl) throws IOException {
        HttpRequester.HttpRequest request = new HttpRequester.HttpRequest(capeUrl, "GET", Collections.emptyMap(), Collections.emptyMap());
        HttpRequester.BinaryHttpResponse response = HttpRequester.fetchBin(request);
        NativeImage rawTexture = NativeImage.read(response.body());
        if(!(rawTexture.getWidth() == 64 && rawTexture.getHeight() == 32)) throw new IOException("Texture at URL is not using the proper cape format!");
        DynamicAnimation capeTexture = new DynamicAnimation(NativeImage.read(response.body()), capeUrl + Math.random(), 64, 32, 0);
        // optifine capes should use the deserializeOptifineCape() method
        return new Cape(capeTexture, false, false, capeUrl, capeUrl + Math.random(), "Unknown");
    }

    public Cape deserializeOptifineCape(String playerName) throws IOException {
        HttpRequester.HttpRequest request = new HttpRequester.HttpRequest("http://s.optifine.net/capes/" + playerName + ".png", "GET", Collections.emptyMap(), Collections.emptyMap());
        HttpRequester.BinaryHttpResponse response = HttpRequester.fetchBin(request);
        System.out.println(new String(response.body().array()));
        NativeImage rawTexture = NativeImage.read(response.body());
        NativeImage parsedTexture = TextureUtil.overlayOnImage(new NativeImage(64, 32, false), rawTexture);
        DynamicAnimation capeTexture = new DynamicAnimation(parsedTexture, "optifine-" + playerName, 64, 32, 0);
        return new Cape(capeTexture, false, false, "Optifine", "Optifine-" + playerName, "Unknown");
    }

    public Cape deserializeOsmiumCape(String playerUUID) throws IOException {
        Map<String, ?> capeData = OsmiumApi.getInstance().getCapeDataFromServers(playerUUID);
        NativeImage rawTexture = OsmiumApi.getInstance().getCapeTextureFromServers(playerUUID);

        return capeFromMapAndTexture(capeData, rawTexture, "osmium-servers");
    }

    private static Cape capeFromMapAndTexture(Map<String, ?> capeData, NativeImage texture, String source) {
        int frameDelay = 0;
        boolean animated = false;
        String creator = "Unknown";
        String name = "Unknown";
        if(capeData.containsKey("frame_delay") && capeData.get("frame_delay").getClass() == Integer.class) frameDelay = (Integer) capeData.get("frame_delay");
        if(capeData.containsKey("animated") && capeData.get("animated").getClass() == Boolean.class) animated = (Boolean) capeData.get("animated");
        if(capeData.containsKey("creator") && capeData.get("creator").getClass() == String.class) creator = (String) capeData.get("creator");
        if(capeData.containsKey("name") && capeData.get("name").getClass() == String.class) name = (String) capeData.get("name");

        int capeHash = ImmutableSet.of(frameDelay, animated, creator, texture).hashCode();
        DynamicAnimation capeTexture = new DynamicAnimation(texture, source + "-" + capeHash, 64, 32, frameDelay);
        return new Cape(capeTexture, false, animated, source, name, creator);

    }

    private static HashSet<Cape> loadCapesFromZip(ZipFile file) throws IOException {
        InputStream manifestFile = file.getInputStream(file.getEntry(Util.getZipFileSystemPrefix(file) + "/manifest.json"));

        JsonReader reader = new JsonReader(new InputStreamReader(manifestFile));
        JsonObject manifest = (JsonObject) JsonParser.parseReader(reader);

        HashSet<Cape> returns = new HashSet<>();

        try {
            for (JsonElement element : manifest.get("capes").getAsJsonArray()) {
                Map<String, Object> keyMap = new HashMap<>();
                element.getAsJsonObject().entrySet().forEach((entry) -> keyMap.put(entry.getKey(), new Gson().fromJson(entry.getValue(), Object.class)));
                InputStream capeImage = file.getInputStream(file.getEntry(Util.getZipFileSystemPrefix(file) + element.getAsJsonObject().get("path").getAsString()));

                returns.add(capeFromMapAndTexture(keyMap, NativeImage.read(capeImage), "local"));
            }
        } catch (Exception e) {
            OsmiumClient.LOGGER.log(Level.WARN, "Failed to load capes from zip file: " + e.getMessage());
        }
        return returns;
    }


}
