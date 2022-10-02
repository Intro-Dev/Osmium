package com.intro.common.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.intro.client.OsmiumClient;
import com.intro.client.render.color.Color;
import com.intro.client.render.color.Colors;
import com.intro.client.util.DebugUtil;
import com.intro.client.util.ElementPosition;
import com.intro.common.config.options.*;
import com.intro.common.config.options.legacy.LegacyOption;
import com.intro.common.config.options.legacy.LegacyOptionDeserializer;
import com.intro.common.config.options.legacy.LegacyOptionSerializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Modifier;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;

/**
 * @since 1.0
 * @author Intro
 */
public class Options {


    private static final HashMap<String, Option<?>> options = new HashMap<>();

    private static final HashMap<String, Option<?>> overwrittenOptions = new HashMap<>();

    public static HashMap<String, Option<?>> getOptions() {
        return options;
    }

    public static void putOverwrittenOption(String key, Option<?> value) {
        overwrittenOptions.put(key, value);
    }

    public static HashMap<String, Option<?>> getOverwrittenOptions() {
        return overwrittenOptions;
    }

    public static void clearOverwrittenOptions() {
        overwrittenOptions.clear();
    }

    public static Option<?> get(String identifier) {
        return options.get(identifier);
    }

    public static void put(String identifier, Option<?> option) {
        options.put(identifier, option);
    }

    public static final Logger LOGGER = LogManager.getLogger();

    public static final Option<String> OPTION_FILE_SCHEMA = new Option<>("OPTION_FILE_SCHEMA", "V2");
    public static final Option<Boolean> ToggleSprintEnabled = new Option<>("ToggleSprintEnabled", false);
    public static final Option<Boolean> FulbrightEnabled = new Option<>("FullbrightEnabled", false);
    public static final Option<Boolean> HurtBobbingEnabled = new Option<>("HurtBobbingEnabled", false);
    public static final Option<SneakMode> SneakMode = new Option<>("HurtBobbingEnabled", com.intro.common.config.options.SneakMode.VANILLA);
    public static final Option<Boolean> NoRainEnabled = new Option<>("NoRainEnabled", false);
    public static final Option<Boolean> FpsEnabled = new Option<>("FpsEnabled", false);
    public static final Option<CapeRenderingMode> CustomCapeMode = new Option<>("CustomCapeMode", CapeRenderingMode.ALL);
    public static final Option<Boolean> NoFireEnabled = new Option<>("NoFireEnabled", false);
    public static final Option<ElementPosition> ToggleSprintPosition = new Option<>("ToggleSprintPosition", new ElementPosition(5, 5, 1));
    public static final Option<ElementPosition> FpsDisplayPosition = new Option<>("FpsDisplayPosition", new ElementPosition(5, 5, 1));
    public static final Option<Boolean> ToggleSneakEnabled = new Option<>("ToggleSneakEnabled", false);
    public static final Option<Boolean> FireworksDisabled = new Option<>("FireworksDisabled", false);
    public static final Option<Double> FlyBoostAmount = new Option<>("FlyBoostAmount", 1d);
    public static final Option<Boolean> FlyBoostEnabled = new Option<>("FlyBoostEnabled", false);
    public static final Option<Boolean> DecreaseNetherParticles = new Option<>("DecreaseNetherParticles", false);
    public static final Option<com.intro.common.config.options.BlockOutlineMode> BlockOutlineMode = new Option<>("CustomBlockOutline", com.intro.common.config.options.BlockOutlineMode.VANILLA);
    public static final Option<Color> BlockOutlineColor = new Option<>("BlockOutlineColor", Colors.PURPLE.getColor());
    public static final Option<Double> BlockOutlineAlpha = new Option<>("BlockOutlineAlpha", 1d);
    public static final Option<com.intro.common.config.options.StatusEffectDisplayMode> StatusEffectDisplayMode = new Option<>("StatusEffectDisplayMode", com.intro.common.config.options.StatusEffectDisplayMode.VANILLA);
    public static final Option<ElementPosition> StatusEffectDisplayPosition = new Option<>("StatusEffectDisplayPosition", new ElementPosition(5, 5, 1));
    public static final Option<Double> MaxStatusEffectsDisplayed = new Option<>("MaxStatusEffectsDisplayed", 1d);
    public static final Option<Boolean> ArmorDisplayEnabled = new Option<>("ArmorDisplayEnabled", false);
    public static final Option<ElementPosition> ArmorDisplayPosition = new Option<>("ArmorDisplayPosition", new ElementPosition(5, 5, 1));
    public static final Option<Boolean> PingDisplayEnabled = new Option<>("PingDisplayEnabled", false);
    public static final Option<ElementPosition> PingDisplayPosition = new Option<>("PingDisplayPosition", new ElementPosition(5, 5, 1));
    public static final Option<Boolean> CpsDisplayEnabled = new Option<>("CpsDisplayEnabled", false);
    public static final Option<ElementPosition> CpsDisplayPosition = new Option<>("CpsDisplayPosition", new ElementPosition(5, 5, 1));
    public static final Option<Color> KeystrokesColor = new Option<>("BlockOutlineColor", Colors.PURPLE.getColor());
    public static final Option<Boolean> KeystrokesRgb = new Option<>("KeystrokesRgb", false);
    public static final Option<ElementPosition> KeystrokesPosition = new Option<>("KeystrokesPosition", new ElementPosition(5, 5, 1));
    public static final Option<Boolean> KeystrokesEnabled = new Option<>("KeystrokesEnabled", false);
    public static final Option<Double> KeystrokesAlpha = new Option<>("KeystrokesAlpha", 1d);
    public static final Option<Boolean> AnimateCapes = new Option<>("AnimateCapes", true);
    public static final Option<Boolean> ShowOtherPlayersCapes = new Option<>("ShowOtherPlayersCapes", true);
    public static final Option<String> SetCape = new Option<>("SetCape", "");
    public static final Option<String> HypixelApiKey = new Option<>("HypixelApiKey", "");
    public static final Option<String> AutoGGString = new Option<>("AutoGGString", "gg");
    public static final Option<Boolean> LevelHeadEnabled = new Option<>("LevelHeadEnabled", false);
    public static final Option<com.intro.common.config.options.LevelHeadMode> LevelHeadMode = new Option<>("LevelHeadMode", com.intro.common.config.options.LevelHeadMode.NETWORK_LEVEL);
    public static final Option<Boolean> AutoGGEnabled = new Option<>("AutoGGEnabled", false);
    public static final Option<Boolean> MotionBlurEnabled = new Option<>("MotionBlurEnabled", false);

    public static HashMap<String, Option<?>> getValues() {
        return options;
    }

    private static final Gson GSON = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .setPrettyPrinting()
            .excludeFieldsWithModifiers(Modifier.PRIVATE)
            .registerTypeAdapter(LegacyOption.class, new LegacyOptionSerializer())
            .registerTypeAdapter(LegacyOption.class, new LegacyOptionDeserializer())
            .registerTypeAdapter(Option.class, new OptionSerializer())
            .registerTypeAdapter(Option.class, new OptionDeserializer())
            .create();


    public static void loadConfig(String path) {
        try {
            File file = Paths.get(path).toFile();
            StringBuilder builder = new StringBuilder();

            boolean createdFile = file.createNewFile();

            Scanner reader = new Scanner(file);

            while(reader.hasNextLine()) {
                builder.append(reader.nextLine());
            }

            reader.close();
            if(createdFile || !isJSONValid(builder.toString())) {
                LOGGER.log(Level.WARN, "Config file either didn't exist or is corrupted, creating new one using default settings.");
                save();
                return;
            }
            Option<?>[] arr;
            try {
                arr = GSON.fromJson(builder.toString(), Option[].class);
            } catch (NullPointerException e) {
                LOGGER.log(Level.INFO, "Detected legacy config, fixing up...");
                arr = DataFixer.fixLegacyOptions(GSON.fromJson(builder.toString(), LegacyOption[].class));
            }
            if(arr.length != 0) {
                for(Option<?> o : arr)  {
                    try {
                        Options.get(o.getIdentifier()).setUnsafe(o.get());
                    } catch (ClassCastException e) {
                        LOGGER.warn("Failed to set option " + o.getIdentifier() + " to value " + o.get() + " of type " + o.get().getClass() + " (Expected value of type " + Options.get(o.getIdentifier()).get().getClass() + ")");
                    } catch (NullPointerException e) {
                        LOGGER.warn("Unknown option " + o.getIdentifier() + " found in options file with value " + o.get() + " (Type " + o.get().getClass() + ")");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.warn("Error in loading osmium config, resetting config to avoid crash!");
            resetOptionsFile();
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
            Option<?>[] arr = Options.getOptions().values().toArray(new Option<?>[0]);
            writer.write(GSON.toJson(arr));
            writer.close();
            if(DebugUtil.DEBUG) LOGGER.info("Saved config to file " + path);
        } catch (Exception e) {
            LOGGER.warn("Error in saving osmium config!");
        }
    }

    /**
     * <p>A method to quickly save to the default config file</p>
     */
    public static void save() {
        setNormalOptions();
        saveConfig(FabricLoader.getInstance().getConfigDir().resolve("osmium-options.json").toString());
    }

    /**
     * <p>Sets the options back to their player set values</p>
     */
    public static void setNormalOptions() {
        for(Option<?> option : Options.getOverwrittenOptions().values()) {
            if(option != null) {
                Options.put(option.getIdentifier(), option);
            } else {
                LOGGER.log(Level.ERROR, "Null option!");
            }
        }
        Options.clearOverwrittenOptions();
    }

    /**
     * <p>A method to quickly load from the default config file</p>
     */
    public static void load() {
        loadConfig(FabricLoader.getInstance().getConfigDir().resolve("osmium-options.json").toString());
    }


    /**
     * <p>Checks if a string is valid JSON</p>
     */
    public static boolean isJSONValid(String jsonInString) {
        try {
            GSON.fromJson(jsonInString, Object.class);
            return true;
        } catch(JsonSyntaxException e) {
            return false;
        }
    }
}