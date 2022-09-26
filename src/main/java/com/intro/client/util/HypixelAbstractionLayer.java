package com.intro.client.util;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventRemovePlayer;
import com.intro.common.config.Options;
import com.intro.common.config.options.LevelHeadMode;
import com.intro.common.util.http.JFetchHypixelHttpClient;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.reply.PlayerReply;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Provides a layer between the hypixel api and the client to obtain information with minimal api calls
 */
public class HypixelAbstractionLayer {

    private static String API_KEY;

    private static final ConcurrentHashMap<String, PlayerReply> cachedPlayerData = new ConcurrentHashMap<>();

    private static HypixelAPI api;

    private static boolean validApiKey = false;

    private static final AtomicInteger hypixelApiCalls = new AtomicInteger(0);

    private static void registerApiCall() {
        hypixelApiCalls.incrementAndGet();
        ExecutionUtil.submitScheduledTask(hypixelApiCalls::decrementAndGet, 1, TimeUnit.MINUTES);
    }


    public static void loadApiKey() {
        API_KEY = OsmiumClient.options.getStringOption(Options.HypixelApiKey).get();
        if(!API_KEY.equals("")) {
            api = new HypixelAPI(new JFetchHypixelHttpClient(UUID.fromString(API_KEY)));
            validApiKey = true;
        } else {
            validApiKey = false;
        }
    }

    public static boolean canUseHypixelService() {
        return validApiKey;
    }

    public static int getPlayerLevel(String uuid) {
       if(loadPlayerDataIfAbsent(uuid)) {
           Enum<?> mode = OsmiumClient.options.getEnumOption(Options.LevelHeadMode).get();
           PlayerReply.Player reply = cachedPlayerData.get(uuid).getPlayer();
           boolean isValid = reply != null && reply.getUuid() != null;
           if(mode == LevelHeadMode.NETWORK_LEVEL && isValid){
               return (int) reply.getNetworkLevel();
           } else if (mode == LevelHeadMode.BEDWARS_LEVEL && isValid){
               return reply.getIntProperty("achievements.bedwars_level", 0);
           } else if (mode == LevelHeadMode.SKYWARS_LEVEL && isValid){
               String formattedLevel = reply.getStringProperty("stats.SkyWars.levelFormatted", "§70⋆");
               return Integer.parseInt(formattedLevel.substring(2, formattedLevel.length()-1));
           }
       }
       return 0;
    }

    private static boolean loadPlayerDataIfAbsent(String uuid) {
        if(cachedPlayerData.get(uuid) == null) {
            ExecutionUtil.submitTask(() -> {
                try {
                    if(hypixelApiCalls.get() <= 115) {
                        registerApiCall();
                        cachedPlayerData.put(uuid, api.getPlayerByUuid(uuid).get());
                    }
                } catch (ExecutionException | InterruptedException e) {
                    cachedPlayerData.put(uuid, new PlayerReply());
                }
            });
            return false;
        }
        return true;
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
