package com.intro.server.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intro.common.config.OptionDeserializer;
import com.intro.common.config.OptionSerializer;
import com.intro.common.config.options.Option;
import com.intro.server.OsmiumServer;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
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

    public static Collection<Option> getServerSetOptions()   {
        return serverSetOptions.values().stream().toList();
    }

    public static void removeSetOption(Option option) {
        serverSetOptions.remove(option.identifier);
        save();
    }

    public static void clearSetOptions() {
        serverSetOptions.clear();
    }

    public static void removeSetOption(String  identifier) {
        serverSetOptions.remove(identifier);}


    /**
     * <p>Saves set options to server config</p>
     */
    public static void save() {
        try {
            File file = Paths.get(FMLPaths.CONFIGDIR.get().resolve("osmium-server-config.json").toString()).toFile();
            if(file.createNewFile()) {
                System.out.println("Couldn't find already existing config file, creating new one.");
                OsmiumServer.LOGGER.log(Level.ALL, "Couldn't find already existing config file, creating new one.");
            }
            FileWriter writer = new FileWriter(file);

            Option[] array = serverSetOptions.values().toArray(new Option[0]);

            writer.write(GSON.toJson(array));
            writer.close();
        } catch (Exception e) {
            OsmiumServer.LOGGER.error("Error in saving osmium config!");
        }
    }

    /**
     * <p>Loads set options from server config</p>
     */
    public static void load() {
        try {
            File file = Paths.get(FMLPaths.CONFIGDIR.get().resolve("osmium-server-config.json").toString()).toFile();
            StringBuilder builder = new StringBuilder();
            boolean createdFile = file.createNewFile();
            Scanner reader = new Scanner(file);
            while(reader.hasNextLine()) {
                builder.append(reader.nextLine());
            }
            if(createdFile || !isJSONValid(builder.toString())) {
                OsmiumServer.LOGGER.log(Level.WARN, "Config file either didn't exist or is corrupted, creating new one using default settings.");
                serverSetOptions = new HashMap<>();
                save();
                return;
            }

            Option[] array = GSON.fromJson(builder.toString(), Option[].class);

            if(array.length != 0) {
                for(Option o : array) {
                    serverSetOptions.put(o.identifier, o);
                }
            }



        } catch (Exception e) {
            e.printStackTrace();
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
