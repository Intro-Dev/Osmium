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
import dev.lobstershack.client.api.OsmiumApi;
import dev.lobstershack.client.config.Options;
import dev.lobstershack.client.config.options.CapeRenderingMode;
import dev.lobstershack.client.util.*;
import dev.lobstershack.client.util.http.HttpRequestBuilder;
import dev.lobstershack.client.util.http.HttpRequester;
import dev.lobstershack.client.util.http.HttpResponse;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
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
 */
public class CosmeticManager {

    private String impersonationName = "";
    private UUID impersonationUUID = UUID.randomUUID();
    private boolean shouldImpersonate = false;

    // for debug purposes I promise
    // only changes the cape for the local player
    public void setImpersonationName(String name) {
        try {
            impersonationUUID = Util.fetchUUIDFromUsername(name);
            impersonationName = name;
        } catch (Exception e) {
            DebugUtil.logIfDebug("Failed trying to impersonate cape " + e, Level.ERROR);
        }

    }

    public void setShouldImpersonate(boolean shouldImpersonate) {
        this.shouldImpersonate = shouldImpersonate;
    }

    public boolean isImpersonating() {
        return this.shouldImpersonate;
    }

    private final HashMap<UUID, Cape> playerCapes = new HashMap<>();

    private final HashSet<Cape> locallyLoadedCapes = new HashSet<>();

    private boolean alreadyAddedLocalPlayersOptifineCape = false;

    public void loadLocalCapes() {
        locallyLoadedCapes.clear();
        try {
            Map<String, ?> defaultCapeParams = ImmutableMap.of("animated", Boolean.TRUE, "creator", "Lobster", "name", "Osmium Logo Cape", "frame_delay", 2, "texture_scale", 1);
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

    public void reloadAllCapes() {
        locallyLoadedCapes.clear();
        loadLocalCapes();
        refreshDownloadedCapes();
    }

    /**
     * Should only be used on local player
     * @param cape Cape
     */
    public void setLocalPlayerCape(Cape cape) {
        playerCapes.put(Minecraft.getInstance().user.getProfileId(), cape);
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

    public Cape getCapeFromEntityGotUUID(UUID uuid) {
        return getPlayerCape(uuid);
    }

    public HashSet<Cape> getLocalCapes() {
        return locallyLoadedCapes;
    }

    public void refreshDownloadedCapes() {
        for(UUID uuid : playerCapes.keySet()) {
            ExecutionUtil.submitTask(() -> {
                try {
                    downloadPlayerCape(Objects.requireNonNull(Minecraft.getInstance().level.getPlayerByUUID(uuid)));
                } catch (Exception e) {
                    OsmiumClient.LOGGER.log(Level.WARN, "Failed downloading player cape: " + e.getMessage());
                }
            });
        }
    }

    public void registerEventListeners() {
        ClientTickEvents.END_CLIENT_TICK.register((client -> {
            if(Options.AnimateCapes.get()) {
                for(Cape cape : playerCapes.values()) {
                    if(cape != null) cape.nextFrame();
                }
            }
        }));


        CustomEvents.PLAYER_JOIN.register(playerInfo -> {
            ExecutionUtil.submitTask(() -> {
                try {
                    Player player = Minecraft.getInstance().level.getPlayerByUUID(playerInfo.getProfile().getId());
                    if(player != null) downloadPlayerCape(player);
                } catch (Exception e) {
                    OsmiumClient.LOGGER.log(Level.WARN, "Failed downloading player cape: " + e.getMessage());
                    e.printStackTrace();
                }
            });
        });

        CustomEvents.PLAYER_REMOVE.register((playerInfo -> {
            // don't free local player cape, causes weirdness when changing between servers on hypixel if not here
            if(playerInfo.getProfile().getId().equals(Minecraft.getInstance().player.getUUID())) return;
            Cape cape = playerCapes.get(playerInfo.getProfile().getId());
            if(cape != null) cape.free();
            playerCapes.remove(playerInfo.getProfile().getId());
        }));
    }

    public Cape getPlayerCape(UUID uuid) {
        Cape cape = playerCapes.get(uuid);
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
            optifineCape = deserializeOptifineCape(player.getName().getString(), player.getUUID());
            if(optifineCape != null) {
                hasOptifine = true;
            }
            if(player == Minecraft.getInstance().player && !alreadyAddedLocalPlayersOptifineCape && hasOptifine) {
                locallyLoadedCapes.add(optifineCape);
                alreadyAddedLocalPlayersOptifineCape = true;
            }
            osmiumCape = deserializeOsmiumCape(shouldImpersonate && DebugUtil.isDebug() ? impersonationUUID : player.getUUID());
            if(osmiumCape != null) {
                hasOsmium = true;
            }
        } catch (IOException e) {
            OsmiumClient.LOGGER.log(Level.WARN, "Unknown error in downloading checked player cape: ", e);
        }

        if(hasOptifine && !hasOsmium) {
            playerCapes.put(player.getUUID(), optifineCape);
            return;
        }
        if(hasOsmium) {
            playerCapes.put(player.getUUID(), osmiumCape);
        }
    }


    public @Nullable Cape deserializeOptifineCape(String playerName, UUID playerUUID) throws IOException {
        HttpResponse response = DebugUtil.isDebug() && shouldImpersonate ? HttpRequester.fetch(new HttpRequestBuilder()
                .url("http://s.optifine.net/capes/" + impersonationName + ".png")
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
        DebugUtil.logIfDebug("Adding cape to player " + playerName + " from optifine", Level.INFO);

        return new Cape(capeTexture, true, false, "Optifine", "optifine-" + playerUUID, "Unknown", 1);
    }

    public Cape deserializeOsmiumCape(UUID playerUUID) throws IOException {
        Map<String, ?> capeData = OsmiumApi.getInstance().getCapeDataFromServers(playerUUID);
        NativeImage rawTexture = OsmiumApi.getInstance().getCapeTextureFromServers(playerUUID);
        if(capeData == null || rawTexture == null) return null;
        DebugUtil.logIfDebug("Adding cape to player " + playerUUID + " from osmium servers", Level.INFO);
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
