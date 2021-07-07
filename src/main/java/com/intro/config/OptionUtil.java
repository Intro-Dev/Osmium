package com.intro.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intro.Osmium;
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
 * Pretty proud of the config system
 * only uses a single hashmap and is called when needed
 *
 * @since 1.0
 * @author Intro
 */
public class OptionUtil {

    public static final Logger LOGGER = LogManager.getLogger();

    public static Options Options = Osmium.options;

    public static boolean ShouldResaveOptions = false;

    private static final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .registerTypeAdapter(Option.class, new OptionSerializer())
            .registerTypeAdapter(Option.class, new OptionDeserializer())
            .create();

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
            Options options =  gson.fromJson(builder.toString(), Options.class);
            if(options == null) {
                options = new Options();
                options.init();
                return options;
            }
            return options;

        } catch (Exception e) {
            LOGGER.warn("Error in loading osmium config!");
            e.printStackTrace();
        }
        return null;
    }

    public static void saveConfig(String path) {
        try {
            File file = Paths.get(path).toFile();
            StringBuilder builder = new StringBuilder();
            if(file.createNewFile()) {
                System.out.println("Couldn't find already existing config file, creating new one.");
                LOGGER.log(Level.ALL, "Couldn't find already existing config file, creating new one.");
            }
            FileWriter writer = new FileWriter(file);
            writer.write(gson.toJson(Osmium.options));
            writer.close();
        } catch (Exception e) {
            LOGGER.warn("Error in saving osmium config!");
        }
    }

    public static void save() {
        Osmium.options.getHashMap();
        saveConfig(FabricLoader.getInstance().getConfigDir().resolve("osmium-options.json").toString());
    }

    public static void load() {
        Osmium.options = OptionUtil.loadConfig(FabricLoader.getInstance().getConfigDir().resolve("osmium-options.json").toString());
        Osmium.options.putHashMap();
        System.out.println(FabricLoader.getInstance().getClass().getName());
    }


    public static boolean isJSONValid(String jsonInString) {
        try {
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }


}

