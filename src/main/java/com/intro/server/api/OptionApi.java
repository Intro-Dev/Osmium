package com.intro.server.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intro.common.config.OptionDeserializer;
import com.intro.common.config.OptionSerializer;
import com.intro.common.config.options.Option;
import com.intro.server.OsmiumServer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class OptionApi {

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .registerTypeAdapter(Option.class, new OptionSerializer())
            .registerTypeAdapter(Option.class, new OptionDeserializer())
            .create();

    private static HashMap<String, Option> serverSetOptions = new HashMap<>();

    public static void addSetOption(Option option) {
        serverSetOptions.put(option.identifier, option);
        save();
    }

    public static List<Option> getServerSetOptions() {
        return serverSetOptions.values().stream().toList();
    }

    public static void removeSetOption(Option option) {
        serverSetOptions.remove(option.identifier);
        save();
    }

    public static void removeSetOption(String identifier) {
        serverSetOptions.remove(identifier);
        save();
    }

    public static void save() {
        try {
            File file = Paths.get(FabricLoader.getInstance().getConfigDir().resolve("osmium-server-config.json").toString()).toFile();
            if(file.createNewFile()) {
                System.out.println("Couldn't find already existing config file, creating new one.");
                OsmiumServer.LOGGER.log(Level.ALL, "Couldn't find already existing config file, creating new one.");
            }
            FileWriter writer = new FileWriter(file);
            writer.write(GSON.toJson(serverSetOptions));
            writer.close();
        } catch (Exception e) {
            OsmiumServer.LOGGER.error("Error in saving osmium config!");
        }
    }

    @SuppressWarnings("unchecked")
    public static void load() {
        try {
            File file = Paths.get(FabricLoader.getInstance().getConfigDir().resolve("osmium-server-config.json").toString()).toFile();
            StringBuilder builder = new StringBuilder();
            boolean createdFile = file.createNewFile();
            Scanner reader = new Scanner(file);

            while(reader.hasNextLine()) {
                builder.append(reader.nextLine());
            }
            if(createdFile || !isJSONValid(builder.toString())) {
                OsmiumServer.LOGGER.log(Level.WARN, "Config file either didn't exist or is corrupted, creating new one using default settings.");
                System.out.println("Config file either didn't exist or is corrupted, creating new one using default settings.");
                serverSetOptions = new HashMap<>();
                save();
                return;
            }

            serverSetOptions = GSON.fromJson(builder.toString(), HashMap.class);
        } catch (Exception e) {
            OsmiumServer.LOGGER.error("Error in loading osmium config!");
        }
    }

    public static boolean isJSONValid(String jsonInString) {
        try {
            GSON.fromJson(jsonInString, Object.class);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

}
