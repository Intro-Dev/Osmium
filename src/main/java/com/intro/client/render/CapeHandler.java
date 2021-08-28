package com.intro.client.render;

import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventAddPlayer;
import com.intro.client.module.event.EventRemovePlayer;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
Cape code adapted from of-capes under GNU GPL
Credit for cape URL code goes to them

 of-capes GitHub: https://github.com/dragonostic/of-capes

@author Intro
@author dragonostic
 **/
public class CapeHandler {

    public void handleEvents(Event event) {
        if(event instanceof EventAddPlayer) {
            Thread CapeDownloaderThread = new Thread(new CapeDownloader(this, (EventAddPlayer) event));
            CapeDownloaderThread.start();
        }
        if(event instanceof EventRemovePlayer) {
            CapeRenderer.OptifineCapes.remove(((EventRemovePlayer) event).entity.getStringUUID());
            CapeRenderer.CapeArray.remove(((EventRemovePlayer) event).entity.getStringUUID());
        }
    }

    public void SetCape(String uuid, InputStream image) {
        NativeImage cape = null;
        try {
            cape = NativeImage.read(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ResourceLocation capeTexture = null;
        try {
            DynamicTexture dynamicTexture = new DynamicTexture(parseCape(cape));
            capeTexture = Minecraft.getInstance().getTextureManager().register(uuid.replace("-", ""), dynamicTexture);
        } catch (Exception e) {
            e.printStackTrace();
        }

        CapeRenderer.CapeArray.put(uuid, capeTexture);
    }



    public boolean SetCapeFromURL(String uuid, String url) {
        try {
            URL capeURL = new URL(url);
            HttpURLConnection huc = (HttpURLConnection) capeURL.openConnection();
            huc.setRequestMethod("HEAD");
            int responseCode = huc.getResponseCode();
            if (responseCode == 404) {
                CapeRenderer.CapeArray.put(uuid, null);
                return false;
            }
            InputStream stream;
            try {
                stream = capeURL.openStream();
            } catch (FileNotFoundException e) {
                CapeRenderer.CapeArray.put(uuid, null);
                return false;
            }
            this.SetCape(uuid, stream);
        } catch (IOException e) {
            CapeRenderer.CapeArray.put(uuid, null);
            return false;
        }
        return true;
    }




    public static NativeImage parseCape(NativeImage image) {

        if(image == null) {
            System.out.println("Null cape texture being parsed");
            return null;
        }
        int imageWidth = 64;
        int imageHeight = 32;
        int imageSrcWidth = image.getWidth();
        int srcHeight = image.getHeight();

        for (int imageSrcHeight = image.getHeight(); imageWidth < imageSrcWidth || imageHeight < imageSrcHeight; imageHeight *= 2) {
            imageWidth *= 2;
        }

        NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
        for (int x = 0; x < imageSrcWidth; x++) {
            for (int y = 0; y < srcHeight; y++) {
                imgNew.setPixelRGBA(x, y, image.getPixelRGBA(x, y));
            }
        }
        image.close();
        return imgNew;
    }

    private static class CapeDownloader implements Runnable {

        private final CapeHandler handler;
        private final EventAddPlayer playerJoin;

        public CapeDownloader(CapeHandler handler, EventAddPlayer e) {
            this.handler = handler;
            this.playerJoin = e;
        }

        public void run() {


            boolean optifineCapeFound = handler.SetCapeFromURL(playerJoin.entity.getStringUUID(), "http://s.optifine.net/capes/" + playerJoin.entity.getName().getString() + ".png");
            if(optifineCapeFound) {
                CapeRenderer.OptifineCapes.add(playerJoin.entity.getStringUUID());
                return;
            }
            handler.SetCapeFromURL(playerJoin.entity.getStringUUID(), "https://minecraftcapes.net/profile/" + playerJoin.entity.getStringUUID().replace("-", "") + "/cape/map");
        }
    }

}
