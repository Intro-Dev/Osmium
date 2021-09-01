package com.intro.client.render.cape;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventAddPlayer;
import com.intro.client.module.event.EventRemovePlayer;
import com.intro.client.render.texture.CapeTextureManager;
import com.intro.client.render.texture.DynamicAnimation;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
Cape code adapted from of-capes under GNU GPL
Credit for cape URL code goes to them

 of-capes GitHub: https://github.com/dragonostic/of-capes

@author Intro
@author dragonostic
 **/
public class CapeHandler {

    public static final HashMap<String, Cape> capes = new HashMap<>();

    public void handleEvents(Event event) {
        if (event instanceof EventAddPlayer) {
            Thread CapeDownloaderThread = new Thread(new CapeDownloader((EventAddPlayer) event));
            CapeDownloaderThread.start();
        }
        if (event instanceof EventRemovePlayer) {
            Cape cape = capes.get(((EventRemovePlayer) event).entity.getStringUUID());
            cape.texture.free();
            capes.remove(((EventRemovePlayer) event).entity.getStringUUID());
        }
    }


    public static void setCape(String uuid, String url, boolean animated) {
        try {
            if(animated) {
                setAnimatedCapeFromResourceLocation(uuid, ResourceLocation.tryParse(url));
            }
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            if(connection.getResponseCode() == 404) {
                throw new RuntimeException("Failed to contact cape server!");
            }
            connection.setRequestMethod("HEAD");

            if(url.startsWith("http://s.optifine.net/capes/")) {
                capes.put(uuid, new Cape(new DynamicAnimation(NativeImage.read(connection.getInputStream()), uuid.replace("-", ""), 64, 32), true, false));
            } else {
                capes.put(uuid, new Cape(new DynamicAnimation(NativeImage.read(connection.getInputStream()), uuid.replace("-", ""), 64, 32), false, false));
            }
        } catch (Exception e) {
            OsmiumClient.LOGGER.error("Failed setting player cape!");
            e.printStackTrace();
        }

    }

    public static void setAnimatedCapeFromResourceLocation(String uuid, ResourceLocation location) {
        try {
            capes.put(uuid, new Cape(CapeTextureManager.getStitchedCape(location).clone(), false, true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class CapeDownloader implements Runnable {

        private final EventAddPlayer playerJoin;

        public CapeDownloader(EventAddPlayer e) {
            this.playerJoin = e;
        }

        public void run() {
            setAnimatedCapeFromResourceLocation(playerJoin.entity.getStringUUID(), new ResourceLocation("osmium", "textures/cape/debug_cape.png"));
        }
    }

}
