package dev.lobstershack.client.api;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.blaze3d.platform.NativeImage;
import dev.lobstershack.client.render.cosmetic.Cape;
import dev.lobstershack.client.util.DebugUtil;
import dev.lobstershack.client.util.ExecutionUtil;
import dev.lobstershack.client.util.http.HttpRequestBuilder;
import dev.lobstershack.client.util.http.HttpRequester;
import dev.lobstershack.client.util.http.HttpResponse;
import dev.lobstershack.client.util.http.MultiPartRequestBuilder;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.system.MemoryUtil;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class OsmiumApiImpl implements OsmiumApi {

    private String sessionToken;
    private final String hostName;

    private final Logger logger = LogManager.getLogger("OsmiumApi");

    protected OsmiumApiImpl(String hostName) throws IOException {
        this.hostName = hostName;
        login();
    }

    private void login() throws IOException {
        String mcApiToken = Minecraft.getInstance().user.getAccessToken();
        String originalSkinUrl = null;
        String originalVariant = null;

        HttpResponse originalSkinString = HttpRequester.fetch(new HttpRequestBuilder()
                .url("https://api.minecraftservices.com/minecraft/profile")
                .method("GET")
                .header("Authorization", "Bearer " + mcApiToken)
                .build()
        );

        if(originalSkinString.getStatusCode() != 200) {
            throw new IOException("Failed to get original skin string: " + originalSkinString.getStatusCode() + ", " + originalSkinString.getAsString());
        }

        JsonObject originalSkinJson = JsonParser.parseString(originalSkinString.getAsString()).getAsJsonObject();

        for(JsonElement element : originalSkinJson.get("skins").getAsJsonArray()) {
            JsonObject object = element.getAsJsonObject();
            if(object.get("state").getAsString().equals("ACTIVE")) {
                originalSkinUrl = object.get("url").getAsString();
                originalVariant = object.get("variant").getAsString();
            }
        }

        HttpResponse response = HttpRequester.fetch(new HttpRequestBuilder()
                .url(hostName + "/osmium/v2/direct/login")
                .method("GET")
                .parameter("uuid", Minecraft.getInstance().user.getUuid())
                .parameter("stage", "initial")
                .build());

        if(response.getStatusCode() != 200) {
            throw new IOException("Failed initial osmium login. Server response: " + response.getAsString());
        }

        HttpResponse mcSkinUploadResponse = HttpRequester.fetch(new MultiPartRequestBuilder()
                .url("https://api.minecraftservices.com/minecraft/profile/skins")
                .method("POST")
                .addFileSection("osmium_verify.png", response.getAsBinary())
                .addTextSection("variant", "classic")
                .header("Authorization", "Bearer " + mcApiToken)
                .build());

        if(mcSkinUploadResponse.getStatusCode() != 200) {
            throw new IOException("Failed to upload new mc skin. Server response: " + mcSkinUploadResponse.getAsString());
        }


        HttpResponse finalLoginResponse = HttpRequester.fetch(new HttpRequestBuilder()
                .url(hostName + "/osmium/v2/direct/login")
                .method("GET")
                .parameter("uuid", Minecraft.getInstance().user.getUuid())
                .parameter("stage", "confirm")
                .build());

        if(finalLoginResponse.getStatusCode() != 200) {
            throw new IOException("Failed to confirm with osmium servers. Server response: " + finalLoginResponse.getAsString());
        }
        logger.log(Level.INFO, "Authenticated with Osmium servers");
        this.sessionToken = (String) new Gson().fromJson(finalLoginResponse.getAsString(), Map.class).get("token");

        HttpResponse resetSkinResponse = HttpRequester.fetch(new HttpRequestBuilder()
                .url("https://api.minecraftservices.com/minecraft/profile/skins")
                .method("POST")
                .header("Authorization", "Bearer " + mcApiToken)
                .header("Content-Type",  "application/json")
                .requestBody(new Gson().toJson(Map.of("variant", originalVariant.toLowerCase(), "url", originalSkinUrl)))
                .build());
        if(resetSkinResponse.getStatusCode() != 200) {
            logger.log(Level.WARN, "Failed to reset mc skin to pre-login: " + resetSkinResponse.getAsString());
        }

        ExecutionUtil.submitScheduledTask(this::sendKeepAlive, 45, TimeUnit.SECONDS);
    }

    /**
     * Sets the currently logged in players cape on Osmium servers
     * @param cape The cape to set
     * @throws IOException If the server returned a code other than 200 in response to cape upload
     */
    @Override
    public void setServerSideCape(Cape cape) throws IOException {
        DebugUtil.logIfDebug("Uploading cape to Osmium servers", Level.INFO);
        HashMap<String, Object> capeData = new HashMap<>();
        capeData.put("frame_delay", cape.getTexture().frameDelay);
        capeData.put("animated", cape.animated);
        capeData.put("creator", cape.creator);
        capeData.put("name", cape.name);
        capeData.put("texture_scale", cape.textureScale);

        String capeJson = new Gson().toJson(capeData);
        HttpResponse response = HttpRequester.fetch(new MultiPartRequestBuilder()
                .url(hostName + "/osmium/v2/direct/cape/upload")
                .method("POST")
                .parameter("uuid", Minecraft.getInstance().user.getUuid())
                .parameter("token", sessionToken)
                .addFileSection("cape.png", ByteBuffer.wrap(cape.getTexture().image.asByteArray()))
                .addTextSection("data", capeJson)
                .build());
        if(response.getStatusCode() != 200) {
            throw new IOException("Server Message: " + response.getAsString());
        }
    }

    /**
     * @param uuid UUID of player to get cape texture for
     * @return Cape texture if the player has a cape on osmium servers, returns null if a cape was not found or osmium servers returned any other status code
     * @throws IOException If an error occurs during connection to Osmium servers
     */
    @Override
    public NativeImage getCapeTextureFromServers(String uuid) throws IOException {
        DebugUtil.logIfDebug("Downloading cape texture for user " + uuid, Level.INFO);
        HttpResponse response = HttpRequester.fetch(new HttpRequestBuilder()
                .url(hostName + "/osmium/v2/static/cape/get/texture/")
                .method("GET")
                .parameter("uuid", uuid.replace("-", ""))
                .parameter("token", sessionToken)
                .build());
        if(response.getStatusCode() != 200 && response.getStatusCode() != 404) {
            logger.log(Level.INFO, "Failed to download cape texture from Osmium servers. Server response: " + response.getAsString());
            return null;
        }
        if(response.getStatusCode() == 404) return null;
        ByteBuffer directBuffer = MemoryUtil.memAlloc(response.getAsBinary().capacity());
        directBuffer.put(response.getAsBinary().array(), 0, response.getAsBinary().array().length);
        directBuffer.rewind();
        NativeImage image = NativeImage.read(directBuffer);
        MemoryUtil.memFree(directBuffer);
        return image;
    }

    /**
     * @param uuid UUID of player to get cape data for
     * @return Cape data if the player has a cape on osmium servers, returns null if a cape was not found or osmium servers returned any other status code
     * @throws IOException If an error occurs during connection to Osmium servers
     */
    @Override
    public Map<String, ?> getCapeDataFromServers(String uuid) throws IOException {
        DebugUtil.logIfDebug("Downloading cape data for user " + uuid, Level.INFO);
        HttpResponse response = HttpRequester.fetch(new HttpRequestBuilder()
                .url(hostName + "/osmium/v2/static/cape/get/data/")
                .method("GET")
                .parameter("uuid", uuid.replace("-", ""))
                .parameter("token", sessionToken)
                .build());
        if(response.getStatusCode() != 200 && response.getStatusCode() != 404) {
            logger.log(Level.INFO, "Failed to download cape data from Osmium servers. Server response: " + response.getAsString());
            return null;
        }
        if(response.getStatusCode() == 404) return null;
        return new Gson().fromJson(response.getAsString(), Map.class);
    }

    /**
     * Sends a keep alive packet to Osmium servers, telling servers to not discard the current auth token
     */
    @Override
    public void sendKeepAlive() {
        try {
            DebugUtil.logIfDebug("Sending keep alive packet!", Level.INFO);
            HttpResponse response = HttpRequester.fetch(new HttpRequestBuilder()
                    .url(hostName + "/osmium/v2/direct/keep-alive")
                    .method("GET")
                    .parameter("uuid", Minecraft.getInstance().user.getUuid())
                    .parameter("token", sessionToken)
                    .build());
            if(response.getStatusCode() != 204) {
                throw new IOException("Error in sending keep alive! Server response: " + response.getStatusCode() + " , " + response.getAsString());
            }
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
