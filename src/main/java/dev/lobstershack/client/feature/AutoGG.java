package dev.lobstershack.client.feature;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import dev.lobstershack.client.OsmiumClient;
import dev.lobstershack.client.config.Options;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.gson.JsonParser.parseReader;

public class AutoGG {

    private static final HashSet<Pattern> triggers = new HashSet<>();

    private static long lastGG = System.currentTimeMillis();

    public static void setupTriggers() {
        HttpURLConnection connection;
        try {
            // credit to sk1er for the trigger data
            connection = (HttpURLConnection) new URL("https://static.sk1er.club/autogg/regex_triggers_3.json").openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            JsonReader reader = new JsonReader(new InputStreamReader(connection.getInputStream()));
            JsonElement rootElement = parseReader(reader);
            JsonObject responseJson = rootElement.getAsJsonObject();
            JsonObject hypixelTriggers = responseJson.get("servers").getAsJsonArray().get(0).getAsJsonObject();
            JsonArray elements = hypixelTriggers.get("triggers").getAsJsonArray();
            for(JsonElement element : elements) {
                JsonObject object = (JsonObject) element;
                if(object.get("type").getAsInt() == 0)
                    triggers.add(Pattern.compile(object.get("pattern").getAsString()));
            }
        } catch (IOException e) {
            OsmiumClient.LOGGER.warn("Could not resolve AutoGG triggers");
            e.printStackTrace();
        }

    }

    public static void registerEventListeners() {
        ClientReceiveMessageEvents.GAME.register(((message, ignored) -> {
            if(System.currentTimeMillis() - lastGG > 1000 && Options.AutoGGEnabled.get()) {
                for(Pattern pattern : triggers) {
                    Matcher matcher = pattern.matcher(message.getString());
                    if(matcher.matches()) {
                        lastGG = System.currentTimeMillis();
                        Minecraft.getInstance().player.connection.sendChat(Options.AutoGGString.get());
                    }
                }
            }
        }));
    }

}
