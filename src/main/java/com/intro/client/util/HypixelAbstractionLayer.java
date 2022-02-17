package com.intro.client.util;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventRemovePlayer;
import com.intro.common.config.Options;
import com.intro.common.config.options.LevelHeadMode;
import net.hypixel.api.HypixelAPI;
import net.hypixel.api.apache.ApacheHttpClient;
import net.hypixel.api.reply.PlayerReply;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Provides a layer between the hypixel api and the client to obtain information with minimal api calls
 */
public class HypixelAbstractionLayer {

    private static String API_KEY;

    private static final HashMap<String, CompletableFuture<PlayerReply>> cachedPlayerData = new HashMap<>();

    private static HypixelAPI api;

    private static boolean validApiKey = false;

    private static final AtomicInteger hypixelApiCalls = new AtomicInteger(0);


    public static void loadApiKey() {
        API_KEY = OsmiumClient.options.getStringOption(Options.HypixelApiKey).get();
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

    public static int getPlayerLevel(String uuid) {
       if(loadPlayerDataIfAbsent(uuid)) {
           try {

               Enum<?> mode = OsmiumClient.options.getEnumOption(Options.LevelHeadMode).get();
               System.out.println(cachedPlayerData.get(uuid).get(1, TimeUnit.MICROSECONDS).getPlayer().getRaw());
               if(mode == LevelHeadMode.NETWORK_LEVEL){
                   return (int) cachedPlayerData.get(uuid).get(1, TimeUnit.MICROSECONDS).getPlayer().getNetworkLevel();
               } else if (mode == LevelHeadMode.BEDWARS_LEVEL){
                   return cachedPlayerData.get(uuid).get(1, TimeUnit.MICROSECONDS).getPlayer().getIntProperty("achievements.bedwars_level", 0);
               } else if (mode == LevelHeadMode.SKYWARS_LEVEL){
                   String formattedLevel = cachedPlayerData.get(uuid).get(1, TimeUnit.MICROSECONDS).getPlayer().getStringProperty("stats.SkyWars.levelFormatted", "§70⋆");
                   return Integer.parseInt(formattedLevel.substring(2, formattedLevel.length()-1));
               }
           } catch (TimeoutException | InterruptedException | ExecutionException e) {
               return 0;
           }
       }
       return 0;
    }

    private static boolean loadPlayerDataIfAbsent(String uuid) {
        if(cachedPlayerData.get(uuid) == null) {
            // set at 115 to have a buffer in case of disparity between threads
            if(hypixelApiCalls.get() <= 115) {
                cachedPlayerData.put(uuid, api.getPlayerByUuid(uuid));
                hypixelApiCalls.incrementAndGet();
                ExecutionUtil.submitScheduledTask(hypixelApiCalls::decrementAndGet, 1, TimeUnit.MINUTES);
                return true;
            }
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
