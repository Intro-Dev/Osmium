package com.intro.client.util;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventRemovePlayer;
import com.intro.common.config.Options;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.apache.ApacheHttpClient;
import net.hypixel.api.reply.PlayerReply;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Provides a layer between the hypixel api and the client to obtain information with minimal api calls
 */
public class HypixelAbstractionLayer {

    private static String API_KEY;

    private static final HashMap<String, CompletableFuture<PlayerReply>> cachedPlayerData = new HashMap<>();

    private static HypixelAPI api;

    private static boolean validApiKey = false;

    public static void loadApiKey() {
        API_KEY = OsmiumClient.options.getStringOption(Options.HypixelApiKey).variable;
        if(!API_KEY.equals("")) {
            api = new HypixelAPI(new ApacheHttpClient(UUID.fromString(API_KEY)));
            validApiKey = true;
        } else {
            validApiKey = false;
        }
    }

    public static boolean canUseHypixelService() {
        return validApiKey;
    }

    public static int getPlayerLevel(String uuid) throws ExecutionException, InterruptedException {
        if(cachedPlayerData.get(uuid) != null) {
            return (int) cachedPlayerData.get(uuid).get().getPlayer().getNetworkLevel();
        }
        loadPlayerData(uuid);
        return (int) cachedPlayerData.get(uuid).get().getPlayer().getNetworkLevel();
    }

    private static void loadPlayerData(String uuid) {
        cachedPlayerData.put(uuid, api.getPlayerByUuid(uuid));
    }

    private static void freePlayerData(String uuid) {
        cachedPlayerData.remove(uuid);
    }

    public static void handleDisconnectEvents(Event event) {
        if(event instanceof EventRemovePlayer eventRemovePlayer) {
            freePlayerData(eventRemovePlayer.entity.getStringUUID());
        }
    }

}
