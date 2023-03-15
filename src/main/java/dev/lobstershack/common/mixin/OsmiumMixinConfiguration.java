package dev.lobstershack.common.mixin;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.lobstershack.common.config.options.Option;
import dev.lobstershack.common.config.options.OptionDeserializer;
import dev.lobstershack.common.config.options.OptionSerializer;
import dev.lobstershack.common.config.options.legacy.LegacyOption;
import dev.lobstershack.common.config.options.legacy.LegacyOptionDeserializer;
import dev.lobstershack.common.config.options.legacy.LegacyOptionSerializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

public class OsmiumMixinConfiguration {

    private final HashMap<String, Option<?>> mixins = new HashMap<>();

    private final Logger LOGGER = LogManager.getLogger(OsmiumMixinConfiguration.class);

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .registerTypeAdapter(LegacyOption.class, new LegacyOptionSerializer())
            .registerTypeAdapter(LegacyOption.class, new LegacyOptionDeserializer())
            .registerTypeAdapter(Option.class, new OptionSerializer())
            .registerTypeAdapter(Option.class, new OptionDeserializer())
            .create();

    public OsmiumMixinConfiguration(String pathToConfigFile) throws IOException {

        File file = Paths.get(pathToConfigFile).toFile();
        StringBuilder builder = new StringBuilder();
        boolean createdFile = file.createNewFile();
        Scanner reader = new Scanner(file);
        while(reader.hasNextLine()) {
            builder.append(reader.nextLine());
        }
        reader.close();

        try {
            Option<?>[] mixinConfigData = GSON.fromJson(builder.toString(), Option[].class);
            for(Option<?> option : mixinConfigData) {
                mixins.put(option.getIdentifier(), option);
            }
        } catch (Exception e) {
            LOGGER.info("Failed deserialization, ignoring incoming data");
        }

        FileWriter writer = new FileWriter(file);
        Option<?>[] arr = mixins.values().toArray(new Option<?>[0]);
        writer.write(GSON.toJson(arr));
        writer.close();
    }

    // assumes mixin is enabled if not present in config file
    public boolean isEnabledInConfig(String mixin) {
        if(mixins.get(mixin) != null) {
            return (boolean) mixins.get(mixin).get();
        }
        return true;
    }


}
