package com.intro.client.util;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intro.client.OsmiumClient;
import com.intro.common.config.OptionDeserializer;
import com.intro.common.config.OptionSerializer;
import com.intro.common.config.Options;
import com.intro.common.config.options.Option;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.Scanner;


/**
 * <p>This class contains the functions to load a config file into an {@link com.intro.common.config.Options} object</p>
 * @since 1.0
 * @author Intro
 */
public class OptionUtil {

    public static final Logger LOGGER = LogManager.getLogger();

    public static Options Options = OsmiumClient.options;

    public static boolean ShouldResaveOptions = false;

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .registerTypeAdapter(Option.class, new OptionSerializer())
            .registerTypeAdapter(Option.class, new OptionDeserializer())
            .create();

    /**
     * <p>Loads a {@link Options} object from a config file</p>
     * @param path The path of the config file
     * @return A completed {@link Options} object
     */
    public static Options loadConfig(String path) {
        try {
            File file = Paths.get(path).toFile();
            StringBuilder builder = new StringBuilder();

            boolean createdFile = file.createNewFile();

            Scanner reader = new Scanner(file);

            while(reader.hasNextLine()) {
                builder.append(reader.nextLine());
            }
            if(createdFile || !isJSONValid(builder.toString())) {
                LOGGER.log(Level.WARN, "Config file either didn't exist or is corrupted, creating new one using default settings.");
                System.out.println("Config file either didn't exist or is corrupted, creating new one using default settings.");
                Options o = new Options();
                o.init();
                save();
                return o;
            }
            Option[] arr = GSON.fromJson(builder.toString(), Option[].class);
            com.intro.common.config.Options options = new Options();
            options.init();
            for(Option o : arr)  {
                options.put(o.identifier, o);
            }
            return options;
        } catch (Exception e) {
            LOGGER.warn("Error in loading osmium config, resetting config to avoid crash!");
            resetOptionsFile();
            com.intro.common.config.Options options = new Options();
            options.init();
            return options;
        }
    }

    public static void resetOptionsFile() {
        File file = Paths.get(FabricLoader.getInstance().getConfigDir().resolve("osmium-options.json").toString()).toFile();
        boolean deleted = file.delete();
        if(!deleted) {
            LOGGER.error("Error in resetting osmium config file. If this issue persists file an issue report at https://github.com/Intro-Dev/Osmium/issues");
        }
    }

    /**
     * <p>Saves the current {@link Options} object in the {@link OsmiumClient} class</p>
     * @param path The path of the config file
     */
    public static void saveConfig(String path) {
        try {
            File file = Paths.get(path).toFile();
            if(file.createNewFile()) {
                System.out.println("Couldn't find already existing config file, creating new one.");
                LOGGER.log(Level.ALL, "Couldn't find already existing config file, creating new one.");
            }
            FileWriter writer = new FileWriter(file);
            Option[] arr = OsmiumClient.options.getOptions().values().toArray(new Option[0]);
            writer.write(GSON.toJson(arr));
            writer.close();
        } catch (Exception e) {
            LOGGER.warn("Error in saving osmium config!");
        }
    }

    /**
     * <p>A method to quickly save to the default config file</p>
     */
    public static void save() {
        for(Option option : OsmiumClient.options.getOverwrittenOptions().values()) {
            if(option != null) {
                OsmiumClient.options.put(option.identifier, option);
            } else {
                LOGGER.log(Level.ERROR, "Null option!");
            }
        }
        OsmiumClient.options.getHashMap();
        saveConfig(FabricLoader.getInstance().getConfigDir().resolve("osmium-options.json").toString());
    }

    /**
     * <p>A method to quickly load from the default config file</p>
     */
    public static void load() {
        OsmiumClient.options = OptionUtil.loadConfig(FabricLoader.getInstance().getConfigDir().resolve("osmium-options.json").toString());
        OsmiumClient.options.putHashMap();
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

