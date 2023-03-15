package dev.lobstershack.client.render.cosmetic;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.blaze3d.platform.NativeImage;
import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.client.event.Event;
import dev.lobstershack.client.event.EventAddPlayer;
import dev.lobstershack.client.event.EventRemovePlayer;
import dev.lobstershack.client.event.EventTick;
import dev.lobstershack.client.util.DebugUtil;
import dev.lobstershack.client.util.ExecutionUtil;
import dev.lobstershack.client.util.TextureUtil;
import dev.lobstershack.common.api.OsmiumApi;
import dev.lobstershack.common.config.Options;
import dev.lobstershack.common.config.options.CapeRenderingMode;
import dev.lobstershack.common.util.Util;
import dev.lobstershack.common.util.http.HttpRequestBuilder;
import dev.lobstershack.common.util.http.HttpRequester;
import dev.lobstershack.common.util.http.HttpResponse;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.system.MemoryUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
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

    private boolean alreadyAddedLocalPlayersOptifineCape = false;

    public void loadLocalCapes() {
        locallyLoadedCapes.clear();
        try {
            Map<String, ?> defaultCapeParams = ImmutableMap.of("animated", Boolean.TRUE, "creator", "Lobster", "name", "Osmium Logo Cape", "frame_delay", 1, "texture_scale", 1);
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
            if(cape.name.equals(Options.SetCape.get())) {
                setLocalPlayerCape(cape);
            }
        }));
    }

    /**
     * Should only be used on local player
     * @param cape Cape
     */
    public void setLocalPlayerCape(Cape cape) {
        playerCapes.put(Minecraft.getInstance().user.getUuid().replaceAll("-", ""), cape);
        ExecutionUtil.submitTask(() -> {
            try {
                OsmiumApi.getInstance().setServerSideCape(cape);
            } catch (IOException e) {
                e.printStackTrace();
                OsmiumClient.LOGGER.log(Level.WARN, "Unable to set cape on Osmium servers: " + e.getMessage());
            }
        });
        Options.SetCape.set(cape.name);
    }

    public Cape getCapeFromEntityGotUUID(String uuid) {
        return getPlayerCape(uuid.toLowerCase().replaceAll("-", ""));
    }

    public HashSet<Cape> getLocalCapes() {
        return locallyLoadedCapes;
    }

    public void refreshDownloadedCapes() {
        for(String uuid : playerCapes.keySet()) {
            ExecutionUtil.submitTask(() -> {
                try {
                    downloadPlayerCape(Objects.requireNonNull(Minecraft.getInstance().level.getPlayerByUUID(UUID.fromString(uuid))));
                } catch (Exception e) {
                    OsmiumClient.LOGGER.log(Level.WARN, "Failed downloading player cape: " + e.getMessage());
                }
            });
        }
    }

    public void handleEvents(Event event) {
        if(event instanceof EventTick && Options.AnimateCapes.get()) {
            for(Cape cape : playerCapes.values()) {
                if(cape != null) cape.nextFrame();
            }
        }

        if(event instanceof EventAddPlayer addPlayer) {
            ExecutionUtil.submitTask(() -> {
                try {
                    downloadPlayerCape(addPlayer.entity);
                } catch (Exception e) {
                    OsmiumClient.LOGGER.log(Level.WARN, "Failed downloading player cape: " + e.getMessage());
                    e.printStackTrace();
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
        if(Options.CustomCapeMode.get() == CapeRenderingMode.ALL) return cape;
        if((Options.CustomCapeMode.get() == CapeRenderingMode.OPTIFINE) && cape.isOptifine) return cape;
        return null;
    }

    public void downloadPlayerCape(@NotNull Player player) {
        boolean hasOptifine = false, hasOsmium = false;
        Cape optifineCape = null;
        Cape osmiumCape = null;
        try {
            // I just spent 3 days trying to fix png corruption
            // because I typed fetch instead of fetchBin
            // please help me
            optifineCape = deserializeOptifineCape(player.getName().getString(), player.getStringUUID());
            if(optifineCape != null) {
                hasOptifine = true;
            }
            if(player == Minecraft.getInstance().player && !alreadyAddedLocalPlayersOptifineCape && hasOptifine) {
                locallyLoadedCapes.add(optifineCape);
                alreadyAddedLocalPlayersOptifineCape = true;
            }
            osmiumCape = deserializeOsmiumCape(player.getStringUUID().replaceAll("-", ""));
            if(osmiumCape != null) {
                hasOsmium = true;
            }
        } catch (IOException e) {
            OsmiumClient.LOGGER.log(Level.WARN, "Unknown error in downloading checked player cape: ", e);
        }

        if(hasOptifine && !hasOsmium) {
            playerCapes.put(player.getStringUUID().replaceAll("-", ""), optifineCape);
            return;
        }
        if(hasOsmium) {
            playerCapes.put(player.getStringUUID().replaceAll("-", ""), osmiumCape);
        }
    }


    public @Nullable Cape deserializeOptifineCape(String playerName, String playerUUID) throws IOException {
        HttpResponse response = DebugUtil.isDebug() ? HttpRequester.fetch(new HttpRequestBuilder()
                .url("http://s.optifine.net/capes/DiscordLion.png")
                .method("GET")
                .build())
                :
                HttpRequester.fetch(new HttpRequestBuilder()
                .url("http://s.optifine.net/capes/" + playerName + ".png")
                .method("GET")
                .build());
        if(response.getStatusCode() != 200) {
            return null;
        }
        // stbi_load_from_memory used by NativeImage.read() can only use direct buffers or else it fails to recognize the format
        // this took longer than im willing to admit figuring out
        ByteBuffer directBuffer = MemoryUtil.memAlloc(response.getAsBinary().capacity());
        directBuffer.put(response.getAsBinary().array(), 0, response.getAsBinary().array().length);
        directBuffer.rewind();
        NativeImage rawTexture = NativeImage.read(directBuffer);
        MemoryUtil.memFree(directBuffer);
        NativeImage parsedTexture;
        if(rawTexture.getWidth() < 64 && rawTexture.getHeight() < 32) {
            parsedTexture = TextureUtil.overlayOnImage(new NativeImage(64, 32, false), rawTexture);
        } else {
            parsedTexture = rawTexture;
        }
        DynamicAnimation capeTexture = new DynamicAnimation(parsedTexture, "optifine-" + playerUUID, 64, 32, 0);
        return new Cape(capeTexture, true, false, "Optifine", "optifine-" + playerUUID, "Unknown", 1);
    }

    public Cape deserializeOsmiumCape(String playerUUID) throws IOException {
        Map<String, ?> capeData = OsmiumApi.getInstance().getCapeDataFromServers(playerUUID);
        NativeImage rawTexture = OsmiumApi.getInstance().getCapeTextureFromServers(playerUUID);
        if(capeData == null || rawTexture == null) return null;
        return capeFromMapAndTexture(capeData, rawTexture, "osmium-servers");
    }

    private static Cape capeFromMapAndTexture(Map<String, ?> capeData, NativeImage texture, String source) {
        if(capeData == null || texture == null) return null;
        int textureScale = 1;
        int frameDelay = 0;
        boolean animated = false;
        String creator = "Unknown";
        String name = "Unknown";
        if(capeData.containsKey("frame_delay") && capeData.get("frame_delay").getClass() == Integer.class) frameDelay = (Integer) capeData.get("frame_delay");
        if(capeData.containsKey("animated") && capeData.get("animated").getClass() == Boolean.class) animated = (Boolean) capeData.get("animated");
        if(capeData.containsKey("creator") && capeData.get("creator").getClass() == String.class) creator = (String) capeData.get("creator");
        if(capeData.containsKey("name") && capeData.get("name").getClass() == String.class) name = (String) capeData.get("name");
        if(capeData.containsKey("texture_scale") && capeData.get("texture_scale").getClass() == Integer.class) textureScale = (Integer) capeData.get("texture_scale");

        int capeHash = ImmutableSet.of(frameDelay, animated, creator, texture).hashCode();
        DynamicAnimation capeTexture = new DynamicAnimation(texture, source + "-" + capeHash, 64, 32, frameDelay);
        return new Cape(capeTexture, false, animated, source, name, creator, textureScale);

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
