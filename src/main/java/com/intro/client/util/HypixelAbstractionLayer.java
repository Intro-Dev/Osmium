package com.intro.client.util;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventRemovePlayer;
import com.intro.common.ModConstants;
import com.intro.common.config.Options;
import com.intro.common.config.options.LevelHeadMode;
import com.intro.common.util.http.JFetchHypixelHttpClient;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Provides a layer between the hypixel api and the client to obtain information with minimal api calls
 */
public class HypixelAbstractionLayer {

    private static final ConcurrentHashMap<String, PlayerReply> cachedPlayerData = new ConcurrentHashMap<>();
    private static final AtomicBoolean validApiKey = new AtomicBoolean(false);

    private static HypixelAPI api;

    public static void loadApiKey() {
        UUID API_KEY;
        try {
            API_KEY = UUID.fromString(Options.HypixelApiKey.get());
        } catch (IllegalArgumentException e) {
            OsmiumClient.LOGGER.warn("Bad hypixel api key!");
            validApiKey.set(false);
            return;
        }
        if(!API_KEY.equals(UUID.fromString(""))) {
            api = new HypixelAPI(new JFetchHypixelHttpClient(API_KEY));
            api.getKey().thenAccept((reply) -> validApiKey.set(true)).exceptionally((throwable) -> {
                throwable.printStackTrace();
                validApiKey.set(false);
                return null;
            });
        } else {
            validApiKey.set(false);
        }
    }

    public static boolean canUseHypixelService() {
        return validApiKey.get();
    }

    public static int getPlayerLevel(String uuid) {
        loadPlayerDataIfAbsent(uuid);
        LevelHeadMode mode = Options.LevelHeadMode.get();
        PlayerReply reply = cachedPlayerData.get(uuid);
        boolean isValid = reply != null && reply.getPlayer() != null && reply.getPlayer().getUuid() != null;
        System.out.println(isValid);
        if(isValid) {
            PlayerReply.Player player = reply.getPlayer();
            if(mode == LevelHeadMode.NETWORK_LEVEL) {
                return (int) player.getNetworkLevel();
            } else if (mode == LevelHeadMode.BEDWARS_LEVEL){
                return player.getIntProperty("achievements.bedwars_level", 0);
            } else if (mode == LevelHeadMode.SKYWARS_LEVEL){
                return Integer.parseInt(player.getStringProperty("stats.SkyWars.levelFormatted", "0").substring(2).replace("*", "").replace("⋆", ""));
            }
        } else {
            return ModConstants.DEBUG ? -1 : 0;
        }
        return ModConstants.DEBUG ? -2 : 0;
    }

    private static void loadPlayerDataIfAbsent(String uuid) {
        if(!canUseHypixelService()) return;
        if(cachedPlayerData.get(uuid) == null) {
            api.getPlayerByUuid(uuid).thenAccept((playerReply -> cachedPlayerData.put(uuid, playerReply))).exceptionally((throwable) -> {
                cachedPlayerData.put(uuid, new PlayerReply());
                throwable.printStackTrace();
                return null;
            });
        }
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
        return player.getUUID().version() == 2;
    }

    private static int getSkyWarsLevelFromXp(int xp) {
        int[] initialLevels = new int[]{0, 20, 70, 150, 250, 500, 1000, 2000, 3500, 6000, 10000, 15000};
        if(xp >= 15000) {
            return (xp - 15000) / 10000 + 12;
        }
        for(int level : initialLevels) {
            if(xp < level) {
                return level;
            }
        }
        return 0;
    }

}
