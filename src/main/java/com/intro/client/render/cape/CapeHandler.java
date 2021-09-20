package com.intro.client.render.cape;

import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventAddPlayer;
import com.intro.client.module.event.EventRemovePlayer;
import com.intro.client.render.texture.DynamicAnimation;
import com.intro.common.ModConstants;
import com.intro.common.config.Options;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.resources.ResourceLocation;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Code rewritten for version 1.2.3
 *
 * @since 1.0
 */
public class CapeHandler {

    public static final HashMap<String, Cape> playerCapes = new HashMap<>();

    public void handleEvents(Event event) {
        if (event instanceof EventAddPlayer) {
            Thread CapeDownloaderThread = new Thread(new StandardCapeDownloader((EventAddPlayer) event));
            CapeDownloaderThread.start();
        }
        if (event instanceof EventRemovePlayer) {
            playerCapes.remove(((EventRemovePlayer) event).entity.getStringUUID());
        }
    }

    public void tickCapes(Event event) {
        if(event.isPost() && OsmiumClient.options.getBooleanOption(Options.AnimateCapes).variable) {
            for (Cape cape : playerCapes.values()) {
                if(cape.isAnimated) {
                    cape.nextFrame();
                }
            }
        }
    }


    public static void setCape(String uuid, String url, boolean animated) {
        try {
            if(animated) {
                setAnimatedCapeFromResourceLocation(uuid, ResourceLocation.tryParse(url));
            }
            URL capeUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) capeUrl.openConnection();
            connection.setRequestMethod("HEAD");

            if(connection.getResponseCode() == 404) {
                return;
            }

            if(url.startsWith("http://s.optifine.net/capes/")) {
                playerCapes.put(uuid, new Cape(new DynamicAnimation(parseOptifineCape(NativeImage.read(capeUrl.openStream())), uuid.replace("-", ""), 64, 32, 1), true, false, "optifine", uuid.replace("-", ""), "unknown"));
            } else {
                playerCapes.put(uuid, new Cape(new DynamicAnimation(NativeImage.read(capeUrl.openStream()), uuid.replace("-", ""), 64, 32, 1), false, false, url, uuid.replace("-", ""), "unknown"));
            }
        } catch (Exception e) {
            OsmiumClient.LOGGER.error("Failed setting player cape!");
            e.printStackTrace();
        }
    }

    public static void setCapeThreaded(String uuid, String url, boolean animated) {
        Thread customDownloader = new Thread(new CustomCapeDownloader(uuid, url, animated));
        customDownloader.start();
    }

    /**
     * <p>Used to parse optifine capes to a usable format</p>
     * <p>Has to be like this because optifine uses a different cape uv format</p>
     * <p>adapted from of-capes</p>
     * @param image Source image
     * @return Parsed Image
     */
    public static NativeImage parseOptifineCape(NativeImage image) {
        int imageWidth = 64;
        int imageHeight = 32;
        int imageSrcWidth = image.getWidth();
        int srcHeight = image.getHeight();

        for (int imageSrcHeight = image.getHeight(); imageWidth < imageSrcWidth || imageHeight < imageSrcHeight; imageHeight *= 2) {
            imageWidth *= 2;
        }

        NativeImage parsedImage = new NativeImage(imageWidth, imageHeight, true);

        for (int x = 0; x < imageSrcWidth; x++) {
            for (int y = 0; y < srcHeight; y++) {
                parsedImage.setPixelRGBA(x, y, image.getPixelRGBA(x, y));
            }
        }

        image.close();

        return parsedImage;

    }


    public static void setAnimatedCapeFromResourceLocation(String uuid, ResourceLocation location) {
        try {
            playerCapes.put(uuid, CosmeticManager.getCape(location).clone());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setCapeFromResourceLocation(String uuid, ResourceLocation location) {
        Thread multiThreaded = new Thread(() -> playerCapes.put(uuid, CosmeticManager.getCape(location).clone()));
        multiThreaded.start();
    }

    private record StandardCapeDownloader(EventAddPlayer playerJoin) implements Runnable {

        public void run() {
            if(ModConstants.DEVELOPER_UUIDS.contains(playerJoin.entity.getStringUUID())) {
                setAnimatedCapeFromResourceLocation(playerJoin.entity.getStringUUID(), new ResourceLocation("osmium", "textures/cape/osmium_logo_cape.png"));
            }
            setCape(playerJoin.entity.getStringUUID(), "http://s.optifine.net/capes/" + playerJoin.entity.getName().getString() + ".png", false);
            if(playerCapes.get(playerJoin.entity.getStringUUID()) == null) {
                setCape(playerJoin.entity.getStringUUID(), "https://minecraftcapes.net/profile/" + playerJoin.entity.getStringUUID().replace("-", "") + "/cape/map", false);
            }
        }
    }

    private record CustomCapeDownloader(String uuid, String url, boolean animated) implements Runnable {

        public void run() {
            setCape(uuid, url, animated);
        }
    }

}
