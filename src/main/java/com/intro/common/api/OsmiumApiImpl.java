package com.intro.common.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.intro.client.render.cosmetic.Cape;
import com.intro.client.util.ExecutionUtil;
import com.intro.common.util.HttpRequester;
import com.intro.common.util.Util;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class OsmiumApiImpl implements OsmiumApi {

    private String sessionToken;
    private String hostName;

    private final Logger logger = LogManager.getLogger("OsmiumApi");

    protected OsmiumApiImpl(String hostName) throws IOException {
        this.hostName = hostName;
        login();
    }

    private void login() throws IOException {
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
        HttpRequester.BinaryHttpResponse response = HttpRequester.fetchBin(new HttpRequester.HttpRequest(hostName + "/osmium/v2/direct/login", "GET", new HashMap<>(), parameters));
        parameters.clear();
        headers.put("Authorization", "Bearer " + mcApiToken);
        headers.put("Content-Type",  "application/json");
        Map<String, String> formData = new HashMap<>();
        formData.put("variant", "classic");
        HttpRequester.HttpRequest request = new HttpRequester.HttpRequest("https://api.minecraftservices.com/minecraft/profile/skins", "POST", headers, parameters);
        HttpRequester.uploadFileWithOtherFormData(request, response.body(), "osverify.png", formData);
        headers.clear();
        parameters.clear();
        formData.clear();
        parameters.put("uuid", Minecraft.getInstance().user.getUuid());
        parameters.put("stage", "confirm");
        HttpRequester.HttpResponse httpResponse = HttpRequester.fetch(new HttpRequester.HttpRequest(hostName + "/osmium/v2/direct/login", "GET", new HashMap<>(), parameters));
        parameters.clear();
        logger.log(Level.INFO, "Authenticated with Osmium servers");
        this.sessionToken = (String) new Gson().fromJson(httpResponse.body(), Map.class).get("token");
        headers.put("Authorization", "Bearer " + mcApiToken);
        headers.put("Content-Type",  "application/json");
        HttpRequester.fetchWithJson(new HttpRequester.HttpRequest("https://api.minecraftservices.com/minecraft/profile/skins", "POST", headers, new HashMap<>()), new Gson().toJson(Map.of("variant", originalVariant.toLowerCase(), "url", originalSkinUrl)));
        ExecutionUtil.submitScheduledTask(this::sendKeepAlive, 45, TimeUnit.SECONDS);
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
        capeData.put("texture_scale", cape.textureScale);

        String capeJson = new Gson().toJson(capeData);
        HttpRequester.HttpRequest request = new HttpRequester.HttpRequest(hostName + "/osmium/v2/direct/cape/upload", "POST", new HashMap<>(), parameters);
        HttpRequester.uploadFileWithOtherFormData(request, ByteBuffer.wrap(cape.getTexture().image.asByteArray()), "cape.png", Util.mutableMapOf(new String[]{"data"}, new String[]{capeJson}));
    }

    @Override
    public NativeImage getCapeTextureFromServers(String uuid) {
        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("uuid", uuid.replace("-", ""));
            parameters.put("token", sessionToken);
            HttpRequester.HttpRequest request = new HttpRequester.HttpRequest(hostName + "/osmium/v2/static/cape/get/texture/", "GET", new HashMap<>(), parameters);
            HttpRequester.BinaryHttpResponse response = HttpRequester.fetchBin(request);
            return NativeImage.read(response.body());
        } catch (IOException ignored) {
            return null;
        }
    }

    @Override
    public Map<String, ?> getCapeDataFromServers(String uuid) {
        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("uuid", uuid.replace("-", ""));
            parameters.put("token", sessionToken);
                HttpRequester.HttpRequest request = new HttpRequester.HttpRequest(hostName + "/osmium/v2/static/cape/get/texture/", "GET", new HashMap<>(), parameters);
            HttpRequester.HttpResponse response = HttpRequester.fetch(request);
            return new Gson().fromJson(response.body(), Map.class);
        } catch (IOException ignored) {
            return null;
        }
    }

    @Override
    public void sendKeepAlive() {
        try {
            HashMap<String, String> parameters = new HashMap<>();
            parameters.put("uuid", Minecraft.getInstance().user.getUuid());
            parameters.put("token", sessionToken);
            HttpRequester.fetch(new HttpRequester.HttpRequest(hostName + "/osmium/v2/direct/keep-alive", "GET", new HashMap<>(), parameters));
            ExecutionUtil.submitScheduledTask(this::sendKeepAlive, 45, TimeUnit.SECONDS);
        } catch (IOException e) {
            logger.log(Level.WARN, "Failed to send keep alive request to osmium servers. Trying to log in again");
            ExecutionUtil.submitTask(() -> {
                try {
                    login();
                } catch (IOException exception) {
                    logger.log(Level.WARN, "Failed to log in again. Assuming osmium servers are offline");
                }
            });
        }
    }


}
