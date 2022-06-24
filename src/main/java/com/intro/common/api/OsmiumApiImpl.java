package com.intro.common.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intro.client.render.cosmetic.Cape;
import com.intro.common.util.HttpRequester;
import com.intro.common.util.Util;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;


/**
 * Login pattern:
 * Client makes request to https://api.lobstershack.dev/osmium/v2/login/ with public key encrypted hash of time
 *
 *
 *
 *
 */
public class OsmiumApiImpl implements OsmiumApi {

    private final String sessionToken;
    private final String hostName;

    private final Logger logger = LogManager.getLogger("OsmiumApi");

    protected OsmiumApiImpl(String hostName) throws IOException {
        String mcApiToken = Minecraft.getInstance().user.getAccessToken();
        String originalSkinUrl = null;
        String originalVariant = null;

        HttpRequester.HttpResponse originalSkinString = HttpRequester.fetch(new HttpRequester.HttpRequest("https://api.minecraftservices.com/minecraft/profile", "GET", Map.of("Authorization", "Bearer " + mcApiToken), Map.of()));
        JsonObject originalSkinJson = JsonParser.parseString(originalSkinString.body()).getAsJsonObject();

        for(JsonElement element : originalSkinJson.get("skins").getAsJsonArray()) {
            JsonObject object = element.getAsJsonObject();
            if(object.get("state").getAsString().equals("ACTIVE")) {
                originalSkinUrl = object.get("url").getAsString();
                originalVariant = object.get("variant").getAsString();
            }
        }

        HashMap<String, String> parameters = new HashMap<>();
        HashMap<String, String> headers = new HashMap<>();
        parameters.put("uuid", Minecraft.getInstance().user.getUuid());
        parameters.put("stage", "initial");
        // uses a short buffer to avoid value overflow
        HttpRequester.BinaryHttpResponse response = HttpRequester.fetchBin(new HttpRequester.HttpRequest(hostName + "/osmium/v2/login", "GET", new HashMap<>(), parameters));
        parameters.clear();
        File outputDir = new File("C:\\Osmium\\osmium-1.17\\osmium\\run\\testing");
        File outputFile = new File("C:\\Osmium\\osmium-1.17\\osmium\\run\\testing\\test.png");
        outputDir.mkdir();
        outputFile.createNewFile();
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(response.body().array());
        }
        headers.put("Authorization", "Bearer " + mcApiToken);
        headers.put("Content-Type",  "application/json");
        Map<String, String> formData = new HashMap<>();
        formData.put("variant", "classic");
        HttpRequester.HttpRequest request = new HttpRequester.HttpRequest("https://api.minecraftservices.com/minecraft/profile/skins", "POST", headers, parameters);
        HttpRequester.HttpResponse resp = HttpRequester.uploadFileWithOtherFormData(request, response.body(), "osverify.png", formData);
        headers.clear();
        parameters.clear();
        formData.clear();
        parameters.put("uuid", Minecraft.getInstance().user.getUuid());
        parameters.put("stage", "confirm");
        HttpRequester.HttpResponse httpResponse = HttpRequester.fetch(new HttpRequester.HttpRequest(hostName + "/osmium/v2/login", "GET", new HashMap<>(), parameters));
        parameters.clear();
        logger.log(Level.INFO, "Authenticated with Osmium servers");
        this.hostName = hostName;
        this.sessionToken = (String) new Gson().fromJson(httpResponse.body(), Map.class).get("token");

        headers.put("Authorization", "Bearer " + mcApiToken);
        headers.put("Content-Type",  "application/json");
        HttpRequester.fetchWithJson(new HttpRequester.HttpRequest("https://api.minecraftservices.com/minecraft/profile/skins", "POST", headers, new HashMap<>()), new Gson().toJson(Map.of("variant", originalVariant.toLowerCase(), "url", originalSkinUrl)));
    }

    @Override
    public void setServerSideCape(Cape cape) throws IOException {
        logger.log(Level.INFO, "Uploading cape to Osmium servers");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("uuid", Minecraft.getInstance().user.getUuid());
        parameters.put("token", sessionToken);

        HashMap<String, Object> capeData = new HashMap<>();
        capeData.put("frame_delay", cape.getTexture().frameDelay);
        capeData.put("animated", cape.animated);
        capeData.put("creator", cape.creator);
        capeData.put("name", cape.name);

        String capeJson = new Gson().toJson(capeData);
        HttpRequester.HttpRequest request = new HttpRequester.HttpRequest(hostName + "/osmium/v2/cape/upload", "POST", new HashMap<>(), parameters);
        HttpRequester.uploadFileWithOtherFormData(request, ByteBuffer.wrap(cape.getTexture().image.asByteArray()), "cape.png", Util.mutableMapOf(new String[]{"data"}, new String[]{capeJson}));
    }

    @Override
    public NativeImage getCapeTextureFromServers(String uuid) throws IOException {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("uuid", uuid.replace("-", ""));
        parameters.put("token", sessionToken);
        HttpRequester.HttpRequest request = new HttpRequester.HttpRequest(hostName + "/osmium/v2/cape/get/texture/", "GET", new HashMap<>(), parameters);
        HttpRequester.BinaryHttpResponse response = HttpRequester.fetchBin(request);
        return NativeImage.read(response.body());
    }

    @Override
    public Map<String, ?> getCapeDataFromServers(String uuid) throws IOException {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("uuid", uuid.replace("-", ""));
        parameters.put("token", sessionToken);
        HttpRequester.HttpRequest request = new HttpRequester.HttpRequest(hostName + "/osmium/v2/cape/get/texture/", "GET", new HashMap<>(), parameters);
        HttpRequester.HttpResponse response = HttpRequester.fetch(request);
        return new Gson().fromJson(response.body(), Map.class);
    }


}
