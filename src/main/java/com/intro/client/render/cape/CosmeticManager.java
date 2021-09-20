package com.intro.client.render.cape;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.intro.client.render.texture.DynamicAnimation;
import com.intro.client.util.TextureUtil;
import com.intro.common.util.Util;
import com.mojang.blaze3d.platform.NativeImage;
import io.netty.buffer.ByteBufInputStream;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.*;
import java.util.zip.ZipFile;

public class CosmeticManager {

    private static final HashMap<String, Cape> capes = new HashMap<>();

    private static final Minecraft mc = Minecraft.getInstance();

    private static void sendToast(Minecraft client, Component title, Component description) {
        client.getToasts().addToast(SystemToast.multiline(client, SystemToast.SystemToastIds.PACK_LOAD_FAILURE, title, description));
    }

    public static void put(String id, Cape cape) {
        capes.put(id, cape);
    }

    public static Cape get(String id) {
        return capes.get(id);
    }

    public static void refresh() {
        capes.clear();
        genCapes();
    }

    public static Cape readCapeFromByteBuf(FriendlyByteBuf byteBuf) throws IOException {
        String creator = byteBuf.readUtf();
        String identifier = byteBuf.readUtf();
        boolean animated = byteBuf.readBoolean();
        int frameDelay = byteBuf.readInt();

        NativeImage capeImage = NativeImage.read(new ByteBufInputStream(byteBuf));

        return new Cape(new DynamicAnimation(capeImage, creator + "-" + identifier, 64 ,32, frameDelay), false, animated, "Server", creator + "-" + identifier, creator);
    }

    public static void genCapes() {
        putCape(new ResourceLocation("osmium", "textures/cape/osmium_logo_cape.png"), new Cape(genCapeAnimation(new ResourceLocation("osmium", "textures/cape/osmium_logo_cape.png"), "osmium_logo_cape", 1), false, true, "local", "osmium_logo_cape", "Intro"));

        try {
            File cosmeticsDir = FabricLoader.getInstance().getGameDir().getParent().resolve("cosmetics").toFile();
            if(!Files.exists(cosmeticsDir.toPath())) {
                Files.createDirectory(cosmeticsDir.toPath());
            }
            for(File file : Objects.requireNonNull(cosmeticsDir.listFiles())) {
                if(!file.isDirectory() && file.toString().endsWith(".zip")) {
                    List<Cape> loaded = loadCapesFromZip(new ZipFile(file, ZipFile.OPEN_READ));
                    loaded.forEach((cape) -> capes.put(cape.registryName, cape));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendToast(mc, new TranslatableComponent("osmium_failed_cape_load_title"), new TranslatableComponent("osmium_failed_cape_load"));
        }
    }

    public static List<Cape> loadCapesFromZip(ZipFile file) throws IOException {
        InputStream manifestFile = file.getInputStream(file.getEntry(Util.getZipFileSystemPrefix(file) + "/manifest.json"));

        JsonReader reader = new JsonReader(new InputStreamReader(manifestFile));
        JsonParser parser = new JsonParser();
        JsonObject manifest = (JsonObject) parser.parse(reader);

        List<Cape> returns = new ArrayList<>();

        for(JsonElement element : manifest.get("capes").getAsJsonArray()) {
            try {
                JsonObject object = (JsonObject) element;

                InputStream capeImage = file.getInputStream(file.getEntry(Util.getZipFileSystemPrefix(file) + object.get("path").getAsString()));
                boolean animated = object.get("animated").getAsBoolean();
                String creator = object.get("creator").getAsString().toLowerCase();
                int frameDelay = object.get("frame_delay").getAsInt();
                String identifier = object.get("identifier").getAsString().toLowerCase();

                returns.add(new Cape(new DynamicAnimation(NativeImage.read(capeImage), creator + "-" + identifier, 64 ,32, frameDelay), false, animated, "cape pack", creator + "-" + identifier, creator));
            } catch (Exception e) {
                e.printStackTrace();
                sendToast(mc, new TranslatableComponent("osmium_failed_cape_load_title"), new TranslatableComponent("osmium_failed_cape_load"));
            }
        }


        return returns;
    }

    public static Cape getCape(ResourceLocation location) {
        return capes.get(location.getNamespace() + location.getPath());
    }

    private static void putCape(ResourceLocation location, Cape cape) {
        capes.put(location.getNamespace() + location.getPath(), cape);
    }

    public static DynamicAnimation genCapeAnimation(ResourceLocation location, String registryName, int frameDelay) {
        return new DynamicAnimation(TextureUtil.getImageAtLocation(location), registryName, 64, 32, frameDelay);
    }

    public static Collection<Cape> getAllCapes() {
        return capes.values();
    }

}
