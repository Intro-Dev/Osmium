package dev.lobstershack.client.util;

import com.google.gson.JsonObject;
import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.client.event.Event;
import dev.lobstershack.client.event.EventRemovePlayer;
import dev.lobstershack.common.config.Options;
import dev.lobstershack.common.config.options.LevelHeadMode;
import dev.lobstershack.common.util.http.HttpRequestBuilder;
import dev.lobstershack.common.util.http.HttpRequester;
import dev.lobstershack.common.util.http.HttpResponse;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextColor;
import net.minecraft.world.entity.player.Player;
import org.apache.logging.log4j.Level;

import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Provides a layer between the hypixel api and the client to obtain information with minimal api calls
 */
public class HypixelAbstractionLayer {

    private static final ConcurrentHashMap<String, JsonObject> cachedPlayerData = new ConcurrentHashMap<>();
    private static final AtomicReference<HashSet<String>> alreadyGettingPlayers = new AtomicReference<>(new HashSet<>());
    private static final AtomicBoolean validApiKey = new AtomicBoolean(false);
    private static final AtomicInteger hypixelRequestCount = new AtomicInteger(0);

    private static void registerApiCall() {
        hypixelRequestCount.incrementAndGet();
        ExecutionUtil.submitScheduledTask(hypixelRequestCount::decrementAndGet, 1, TimeUnit.MINUTES);
    }

    private static boolean canCallApi() {
        return validApiKey.get() && hypixelRequestCount.get() <= 115;
    }

    private static UUID apiKey;

    public static void loadApiKey() {
        try {
            apiKey = UUID.fromString(Options.HypixelApiKey.get());
        } catch (IllegalArgumentException e) {
            OsmiumClient.LOGGER.warn("Bad hypixel api key!");
            validApiKey.set(false);
            return;
        }
        ExecutionUtil.submitTask(() -> {
            try {
                registerApiCall();
                HttpResponse response = HttpRequester.fetch(new HttpRequestBuilder()
                        .url("https://api.hypixel.net/key")
                        .method("GET")
                        .header("API-Key", apiKey.toString()).build());
                validApiKey.set(response.getStatusCode() == 200);
            } catch (IOException e) {
                validApiKey.set(false);
            }
        });
    }

    public static int getPlayerLevel(String uuid) {
        loadPlayerDataIfAbsent(uuid);
        LevelHeadMode mode = Options.LevelHeadMode.get();
        boolean isValid = cachedPlayerData.get(uuid) != null;
        JsonObject reply = cachedPlayerData.get(uuid);
        if(isValid) {
            return switch (mode) {
                case NETWORK_LEVEL -> reply.get("networkExp") != null ? (int) networkXpToLevel((reply.get("networkExp").getAsInt())) : 0;
                case BEDWARS_LEVEL -> reply.get("achievements").getAsJsonObject().get("bedwars_level") != null ? (int) Math.floor((reply.get("achievements").getAsJsonObject().get("bedwars_level").getAsFloat())) : 0;
                case SKYWARS_LEVEL -> reply.get("stats").getAsJsonObject().get("SkyWars").getAsJsonObject().get("levelFormatted") != null ? Integer.parseInt(reply.get("stats").getAsJsonObject().get("SkyWars").getAsJsonObject().get("levelFormatted").getAsString().replaceAll("[^0-9]", "").replace("*", "").replace("⋆", "")) : 0;
            };
        } else {
            return DebugUtil.isDebug() ? -1 : 0;
        }
    }

    private static void loadPlayerDataIfAbsent(String uuid) {
        if(canCallApi()) {
            if(cachedPlayerData.get(uuid) == null && !alreadyGettingPlayers.get().contains(uuid)) {
                ExecutionUtil.submitTask(() -> {
                    try {
                        Minecraft.getInstance().getProfiler().push("HypixelDataGet");
                        DebugUtil.logIfDebug("Getting hypixel data for player " + uuid, Level.INFO);
                        alreadyGettingPlayers.get().add(uuid);
                        registerApiCall();
                        HttpResponse response = HttpRequester.fetch(new HttpRequestBuilder()
                                .url("https://api.hypixel.net/player")
                                .method("GET")
                                .header("API-Key", apiKey.toString())
                                .parameter("uuid", uuid)
                                .build());
                        Minecraft.getInstance().getProfiler().pop();
                        DebugUtil.logIfDebug(response.getAsJson(), Level.INFO);
                        if(response.getAsJson().getAsJsonObject().get("success").getAsBoolean()) {
                            DebugUtil.logIfDebug("Finished getting data for player  " + uuid, Level.INFO);
                            cachedPlayerData.put(uuid, response.getAsJson().getAsJsonObject().get("player").getAsJsonObject());
                        } else {
                            DebugUtil.logIfDebug("Hypixel api failed to get info for player " + uuid + " json: " + response.getAsJson(), Level.INFO);
                            return;
                        }
                        alreadyGettingPlayers.get().remove(uuid);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    // yoinked from https://github.com/HypixelDev/PublicAPI/blob/master/hypixel-api-core/src/main/java/net/hypixel/api/util/ILeveling.java
    private static float networkXpToLevel(int xp) {
        double BASE = 10000;
        double GROWTH = 2500;
        double REVERSE_PQ_PREFIX = -(BASE - 0.5 * GROWTH) / GROWTH;
        double REVERSE_CONST = REVERSE_PQ_PREFIX * REVERSE_PQ_PREFIX;
        return xp < 0 ? 1 : (float) Math.floor(1 + REVERSE_PQ_PREFIX + Math.sqrt(REVERSE_CONST + 2 / GROWTH * xp));
    }



    private static void freePlayerData(String uuid) {
        cachedPlayerData.remove(uuid);
    }

    public static void handleDisconnectEvents(Event event) {
        if(event instanceof EventRemovePlayer eventRemovePlayer) {
            freePlayerData(eventRemovePlayer.entity.getStringUUID());
        }
    }

    public static boolean isPlayerNpc(Player player) {
        return player.getDisplayName().getString().toLowerCase().contains("[npc]") || player.getDisplayName().getStyle().getColor() == TextColor.parseColor("0xffffff");
    }

}
