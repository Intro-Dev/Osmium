package com.intro.render;

import com.intro.module.Module;
import com.intro.module.event.Event;
import com.intro.module.event.EventAddPlayer;
import com.intro.module.event.EventRemovePlayer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
Cape code adapted from of-capes under GNU GPL
Credit for cape URL code goes to them

 of-capes github: https://github.com/dragonostic/of-capes

@author Intro
@author dragonostic
 **/
public class CapeHandler extends Module {
    public CapeHandler() {
        super("Cape Handler");
    }



    public void OnEvent(Event event) {

        if(event instanceof EventAddPlayer) {
            Thread CapeDownloaderThread = new Thread(new CapeDownloader(this, (EventAddPlayer) event));
            CapeDownloaderThread.start();
        }
        if(event instanceof EventRemovePlayer) {
            if(CapeRenderer.OptifineCapes.contains(((EventRemovePlayer) event).entity.getUuidAsString())) {
                CapeRenderer.OptifineCapes.remove(((EventRemovePlayer) event).entity.getUuidAsString());
            }
            CapeRenderer.CapeArray.remove(((EventRemovePlayer) event).entity.getUuidAsString());
        }
    }

    public void SetCape(String uuid, InputStream image) {
        NativeImage cape = null;
        try {
            cape = NativeImage.read(image);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Identifier capeTexture = null;
        try {
            NativeImageBackedTexture nIBT = new NativeImageBackedTexture(parseCape(cape));
            capeTexture = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(uuid.replace("-", ""), nIBT);
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
            InputStream stream = null;
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

    public boolean SetCapeFromURL(String uuid, URL capeURL) {
        try {
            this.SetCape(uuid, capeURL.openStream());
            return true;
        } catch (IOException e) {
            CapeRenderer.CapeArray.put(uuid, null);
            e.printStackTrace();
            return false;
        }
    }




    public static NativeImage parseCape(NativeImage image) {

                if(image == null) {
                    System.out.println("Null cape texture being parsed");
                }
                int imageWidth = 64;
                int imageHeight = 32;
                int imageSrcWidth = image.getWidth();
                int srcHeight = image.getHeight();

                for (int imageSrcHeight = image.getHeight(); imageWidth < imageSrcWidth
                        || imageHeight < imageSrcHeight; imageHeight *= 2) {
                    imageWidth *= 2;
                }

                NativeImage imgNew = new NativeImage(imageWidth, imageHeight, true);
                for (int x = 0; x < imageSrcWidth; x++) {
                    for (int y = 0; y < srcHeight; y++) {
                        imgNew.setPixelColor(x, y, image.getPixelColor(x, y));
                    }
                }
                image.close();

                return imgNew;

    }
}
class CapeDownloader implements Runnable {

    CapeHandler handler;
    EventAddPlayer playerJoin;

    public CapeDownloader(CapeHandler handler, EventAddPlayer e) {
        this.handler = handler;
        this.playerJoin = e;
    }

    public void run() {


        boolean optifineCapeFound = handler.SetCapeFromURL(playerJoin.entity.getUuidAsString(), "http://s.optifine.net/capes/Boonka.png");// + playerJoin.entity.getName().asString() + ".png");
        if(optifineCapeFound) {
            CapeRenderer.OptifineCapes.add(playerJoin.entity.getUuidAsString());
            return;
        }
        if(handler.SetCapeFromURL(playerJoin.entity.getUuidAsString(), "https://minecraftcapes.net/profile/" + playerJoin.entity.getUuidAsString().replace("-", "") + "/cape/map"))
            return;
    }
}
