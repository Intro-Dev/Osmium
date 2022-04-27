package com.intro.common.api;

import com.google.gson.Gson;
import com.intro.client.OsmiumClient;
import com.intro.client.render.cape.Cape;
import com.intro.client.render.texture.DynamicAnimation;
import com.intro.common.util.HttpRequester;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Level;
import org.lwjgl.system.MemoryUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
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

    private String sessionToken;
    private String hostName;

    protected OsmiumApiImpl(String hostName) throws IOException {
        String mcApiToken = Minecraft.getInstance().user.getAccessToken();
        HashMap<String, String> parameters = new HashMap<>();
        HashMap<String, String> headers = new HashMap<>();


        parameters.put("uuid", Minecraft.getInstance().user.getUuid());
        parameters.put("stage", "initial");
        // uses a short buffer to avoid value overflow
        HttpRequester.BinaryHttpResponse response = HttpRequester.fetchBin(new HttpRequester.HttpRequest(hostName + "/osmium/v2/login/", "GET", Collections.emptyMap(), parameters));

        System.out.println("Image data: " + Arrays.toString(response.body().array()));

        parameters.clear();
        File outputDir = new File("C:\\Osmium\\osmium-1.17\\osmium\\run\\testing");
        File outputFile = new File("C:\\Osmium\\osmium-1.17\\osmium\\run\\testing\\test.png");
        outputDir.mkdir();
        outputFile.createNewFile();
        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
            outputStream.write(response.body().array());
        }

        headers.put("Authorization", "Bearer " + mcApiToken);

        Map<String, String> formData = new HashMap<>();
        formData.put("variant", "classic");
        HttpRequester.HttpRequest request = new HttpRequester.HttpRequest("https://api.minecraftservices.com/minecraft/profile/skins", "POST", headers, parameters);
        HttpRequester.HttpResponse resp = HttpRequester.uploadFileWithOtherFormData(request, response.body(), "osverify.png", formData);
        System.out.println("MJAPI response: " + resp);
        parameters.clear();
        parameters.put("uuid", Minecraft.getInstance().user.getUuid());
        parameters.put("stage", "confirm");
        HttpRequester.HttpResponse httpResponse = HttpRequester.fetch(new HttpRequester.HttpRequest(hostName + "/osmium/v2/login/", "GET", Collections.emptyMap(), parameters));
        System.out.println("osauth confirm response: " + httpResponse);
        this.hostName = hostName;
        this.sessionToken = httpResponse.body();
    }

    @Override
    public void uploadCapeToServers(Cape cape) throws IOException {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("uuid", Minecraft.getInstance().user.getUuid());
        parameters.put("token", sessionToken);
        HttpRequester.HttpResponse response = HttpRequester.uploadFile(new HttpRequester.HttpRequest(hostName + "/osmium/v2/cape/upload/", "POST", Collections.emptyMap(), parameters), serializeCape(cape).asShortBuffer(), "cape.cape");
        System.out.println("Upload cape response: " + response);
    }

    @Override
    public Cape getCapeFromServers(String uuid) throws IOException {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("uuid", uuid);
        HttpRequester.BinaryHttpResponse response = HttpRequester.fetchBin(new HttpRequester.HttpRequest(hostName + "/osmium/v2/cape/get/", "GET", Collections.emptyMap(), parameters));
        return deserializeCape(response.body());
    }

    public static ByteBuffer serializeCape(Cape cape) throws IOException {
        Gson gsonObj = new Gson();
        int capeTextureSize = cape.getTexture().image.asByteArray().length;
        Map<String, Object> capeDataMap = new HashMap<>();
        capeDataMap.put("creator", cape.creator);
        capeDataMap.put("animated", cape.isAnimated);
        capeDataMap.put("registry_name", cape.registryName);

        Map<String, Object> dynamicAnimationDataMap = new HashMap<>();
        DynamicAnimation dyn = cape.getTexture();
        dynamicAnimationDataMap.put("max_animation_frames", dyn.maxAnimationFrames);
        dynamicAnimationDataMap.put("frame_width", dyn.frameWidth);
        dynamicAnimationDataMap.put("frame_height", dyn.frameHeight);
        dynamicAnimationDataMap.put("frame_delay", dyn.frameDelay);

        String serializedCapeData = gsonObj.toJson(capeDataMap);
        String serializedDynamicAnimationData = gsonObj.toJson(dynamicAnimationDataMap);
        ByteBuffer buffer = ByteBuffer.allocate(8 + serializedCapeData.getBytes(StandardCharsets.UTF_8).length + serializedDynamicAnimationData.getBytes(StandardCharsets.UTF_8).length + capeTextureSize);
        buffer.putInt(0, serializedCapeData.getBytes(StandardCharsets.UTF_8).length);
        buffer.putInt(4, serializedDynamicAnimationData.getBytes(StandardCharsets.UTF_8).length);
        buffer.put(8, serializedCapeData.getBytes(StandardCharsets.UTF_8));
        buffer.put(8 + serializedCapeData.getBytes(StandardCharsets.UTF_8).length, serializedDynamicAnimationData.getBytes(StandardCharsets.UTF_8));
        buffer.put(8 + serializedCapeData.getBytes(StandardCharsets.UTF_8).length + serializedDynamicAnimationData.getBytes(StandardCharsets.UTF_8).length, dyn.image.asByteArray());
        return buffer;
    }

    public static Cape deserializeCape(ByteBuffer buffer) throws IOException {
        Gson gsonObj = new Gson();
        int capeDataSize = buffer.getInt(0);
        int dynamicAnimationDataSize = buffer.getInt(4);
        String capeData = new String(buffer.array(), 8, capeDataSize, StandardCharsets.UTF_8);
        String dynamicAnimationData = new String(buffer.array(), 8 + capeDataSize, dynamicAnimationDataSize, StandardCharsets.UTF_8);

        Map<String, Object> capeDataMap = gsonObj.fromJson(capeData, Map.class);
        Map<String, Object> dynamicAnimationDataMap = gsonObj.fromJson(dynamicAnimationData, Map.class);
        // done so LWJGL memory utils can read the buffer
        ByteBuffer nativeBuffer = ByteBuffer.allocateDirect(buffer.capacity() - capeDataSize - dynamicAnimationDataSize - 8);
        nativeBuffer.put(buffer.slice(capeDataSize + dynamicAnimationDataSize + 8, buffer.capacity() - capeDataSize - dynamicAnimationDataSize - 8));
        int maxFrames = ((Double) dynamicAnimationDataMap.get("max_animation_frames")).byteValue();
        int imageWidth = ((Double) dynamicAnimationDataMap.get("frame_width")).byteValue() * maxFrames;
        int imageHeight = ((Double) dynamicAnimationDataMap.get("frame_height")).byteValue() * maxFrames;
        NativeImage image = new NativeImage(imageWidth, imageHeight, false);
        if(buffer.capacity() - capeDataSize - dynamicAnimationDataSize - 8 > imageWidth * imageHeight * 4) {
            OsmiumClient.LOGGER.log(Level.WARN, "Cape texture size is larger than expected. Someone probably tried a buffer overflow.");
            return new Cape(new DynamicAnimation(image, (String) capeDataMap.get("registry_name"), ((Double) dynamicAnimationDataMap.get("frame_width")).byteValue(), ((Double) dynamicAnimationDataMap.get("frame_height")).byteValue(), ((Double) dynamicAnimationDataMap.get("frame_delay")).byteValue()), false, (boolean) capeDataMap.get("animated"), "remote_server", (String) capeDataMap.get("registry_name"), (String) capeDataMap.get("creator"));
        }
        MemoryUtil.memCopy(MemoryUtil.memAddress(nativeBuffer), image.pixels, nativeBuffer.capacity());
        System.out.println(image.pixels);
        return new Cape(new DynamicAnimation(image, (String) capeDataMap.get("registry_name"), ((Double) dynamicAnimationDataMap.get("frame_width")).byteValue(), ((Double) dynamicAnimationDataMap.get("frame_height")).byteValue(), ((Double) dynamicAnimationDataMap.get("frame_delay")).byteValue()), false, (boolean) capeDataMap.get("animated"), "remote_server", (String) capeDataMap.get("registry_name"), (String) capeDataMap.get("creator"));
    }


}
