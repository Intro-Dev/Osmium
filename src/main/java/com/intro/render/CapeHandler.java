package com.intro.render;

import com.intro.module.Module;
import com.intro.module.event.Event;
import com.intro.module.event.EventPlayerJoin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
Cape code adapted from dragonostic under GNU GPL
Credit for cape URL code goes to them

@Author Intro
@Author dragonostic
 **/
public class CapeHandler extends Module {
    public CapeHandler() {
        super("Cape Handler");
    }



    public void OnEvent(Event event) {

        if(event instanceof EventPlayerJoin) {
            Thread CapeDownloaderThread = new Thread(new CapeDownloader(this, (EventPlayerJoin) event));
            CapeDownloaderThread.start();
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
            if(cape != null) {
                NativeImageBackedTexture nIBT = new NativeImageBackedTexture(parseCape(cape));
                capeTexture = MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(uuid.replace("-", ""), nIBT);
            } else {
                System.out.println("Error: Null Cape Texture!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        CapeRenderer.CapeArray.put(uuid, capeTexture);
    }

    public boolean SetCapeFromURL(String uuid, String url) {
        try {
            URL capeURL = new URL(url);
            this.SetCape(uuid, capeURL.openStream());
            return true;
        } catch (IOException e) {
            CapeRenderer.CapeArray.put(uuid, null);
            e.printStackTrace();
            return false;
        }
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
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
class CapeDownloader implements Runnable {

    CapeHandler handler;
    EventPlayerJoin playerJoin;

    public CapeDownloader(CapeHandler handler, EventPlayerJoin e) {
        this.handler = handler;
        this.playerJoin = e;
    }

    public void run() {
        try {
            handler.SetCapeFromURL(playerJoin.entity.getUuidAsString(), "https://raw.githubusercontent.com/ewanhowell5195/customOptiFineCapeServer/main/capes/8onfire.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
