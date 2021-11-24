package com.intro.client.module;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.intro.client.OsmiumClient;
import com.intro.client.module.event.Event;
import com.intro.client.module.event.EventReceiveChatMessage;
import com.intro.common.config.Options;
import net.minecraft.client.Minecraft;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            JsonParser parser = new JsonParser();
            JsonElement rootElement = parser.parse(reader);
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

    public static void onEvent(Event event) {
        if(System.currentTimeMillis() - lastGG > 1000 && OsmiumClient.options.getBooleanOption(Options.AutoGGEnabled).variable) {
            for(Pattern pattern : triggers) {
                Matcher matcher = pattern.matcher(((EventReceiveChatMessage) event).getComponent().getString());
                if(matcher.matches()) {
                    lastGG = System.currentTimeMillis();
                    Minecraft.getInstance().player.chat("gg");
                }
            }
        }
    }

}
