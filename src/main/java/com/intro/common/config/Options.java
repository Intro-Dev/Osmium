package com.intro.common.config;

import com.intro.client.render.color.Color;
import com.intro.client.render.color.Colors;
import com.intro.client.util.ElementPosition;
import com.intro.common.config.options.CapeRenderingMode;
import com.intro.common.config.options.Option;

import java.util.HashMap;

/**
 * The Options class stores all OsmiumClient options.
 * All options have a String identifier, which is used to get the option from the HashMap
 * On startup, default options are set first, and are then put to the HashMap from an LegacyOption array loaded from a json file
 *
 * @since 1.0
 * @author Intro
 */
public class Options {

    /**
     * Cache of all options
     */

    private final HashMap<String, Option<?>> options = new HashMap<>();

    private final HashMap<String, Option<?>> overwrittenOptions = new HashMap<>();

    public HashMap<String, Option<?>> getOptions() {
        return options;
    }

    public void putOverwrittenOption(String key, Option<?> value) {
        overwrittenOptions.put(key, value);
    }

    public HashMap<String, Option<?>> getOverwrittenOptions() {
        return overwrittenOptions;
    }

    public void clearOverwrittenOptions() {
        overwrittenOptions.clear();
    }

    public Option<?> get(String identifier) {
        return options.get(identifier);
    }

    public void put(String identifier, Option<?> option) {
        options.put(identifier, option);
    }

    public Option<Boolean> getBooleanOption(String identifier) {
        return (Option<Boolean>) get(identifier);
    }

    public Option<Enum<?>> getEnumOption(String identifier) {
        return (Option<Enum<?>>) get(identifier);
    }

    public Option<ElementPosition> getElementPositionOption(String identifier) {
        return (Option<ElementPosition>) get(identifier);
    }

    public Option<String> getStringOption(String identifier) {
        return (Option<String>) get(identifier);
    }

    public Option<Double> getDoubleOption(String identifier) {
        return (Option<Double>) get(identifier);
    }

    public Option<Color> getColorOption(String identifier) {
        return (Option<Color>) get(identifier);
    }

    public static final String ToggleSprintEnabled = "ToggleSprintEnabled";
    public static final String FullbrightEnabled = "FullbrightEnabled";
    public static final String HurtbobbingEnabled = "HurtBobbingEnabled";
    public static final String SneakMode = "SneakMode";
    public static final String NoRainEnabled = "NoRainEnabled";
    public static final String FpsEnabled = "FpsEnabled";
    public static final String CustomCapeMode = "CustomCapeMode";
    public static final String NoFireEnabled = "NoFireEnabled";
    public static final String ToggleSprintPosition = "ToggleSprintPosition";
    public static final String FpsDisplayPosition = "FpsDisplayPosition";
    public static final String ToggleSneakEnabled = "ToggleSneakEnabled";
    public static final String FireworksDisabled = "FireworksDisabled";
    public static final String FlyBoostAmount = "FlyBoostAmount";
    public static final String FlyBoostEnabled = "FlyBoostEnabled";
    public static final String DecreaseNetherParticles = "DecreaseNetherParticles";
    public static final String BlockOutlineMode = "CustomBlockOutline";
    public static final String BlockOutlineColor = "BlockOutlineColor";
    public static final String BlockOutlineAlpha = "BlockOutlineAlpha";
    public static final String StatusEffectDisplayMode = "StatusEffectDisplayMode";
    public static final String StatusEffectDisplayPosition = "StatusEffectDisplayPosition";
    public static final String MaxStatusEffectsDisplayed = "MaxStatusEffectsDisplayed";
    public static final String StatusEffectDisplayScale = "StatusEffectDisplayScale";
    public static final String ArmorDisplayEnabled = "ArmorDisplayEnabled";
    public static final String ArmorDisplayPosition = "ArmorDisplayPosition";
    public static final String PingDisplayEnabled = "PingDisplayEnabled";
    public static final String PingDisplayPosition = "PingDisplayPosition";
    public static final String CpsDisplayEnabled = "CpsDisplayEnabled";
    public static final String CpsDisplayPosition = "CpsDisplayPosition";
    public static final String KeystrokesColor = "KeystrokesColor";
    public static final String KeystrokesRgb = "KeystrokesRgb";
    public static final String KeystrokesPosition = "KeystrokesPosition";
    public static final String KeystrokesEnabled = "KeystrokesEnabled";
    public static final String KeystrokesAlpha = "KeystrokesAlpha";
    public static final String AnimateCapes = "AnimateCapes";
    public static final String ShowOtherPlayersCapes = "ShowOtherPlayersCapes";
    public static final String SetCape = "SetCape";
    public static final String HypixelApiKey = "HypixelApiKey";
    public static final String AutoGGString = "AutoGGString";
    public static final String LevelHeadEnabled = "LevelHeadEnabled";
    public static final String AutoGGEnabled = "AutoGGEnabled";
    public static final String LevelHeadMode = "LevelHeadMode";

    // contains the string for the option schema that the client is using
    public static final String OPTION_FILE_SCHEMA = "OPTION_FILE_SCHEMA";




    public void init() {
        // Default inits
        // Changed to preferences when config is loaded
        this.setDefaults();
    }



    /**
     * Assigns the default settings to the option variables.
     */
    public void setDefaults() {
        put(OPTION_FILE_SCHEMA, new Option<>(OPTION_FILE_SCHEMA, "v2"));

        put(ToggleSprintEnabled, new Option<>(ToggleSprintEnabled, false));
        put(FullbrightEnabled, new Option<>(FullbrightEnabled, false));
        put(HurtbobbingEnabled, new Option<>(HurtbobbingEnabled, false));
        put(SneakMode, new Option<>(SneakMode, com.intro.common.config.options.SneakMode.VANILLA));
        put(NoRainEnabled, new Option<>(NoRainEnabled, false));
        put(FpsEnabled, new Option<>(FpsEnabled, false));
        put(CustomCapeMode, new Option<>(CustomCapeMode, CapeRenderingMode.DISABLED));
        put(NoFireEnabled, new Option<>(NoFireEnabled, false));
        put(ToggleSprintPosition, new Option<>(ToggleSprintPosition, new ElementPosition(5, 5, 1)));
        put(FpsDisplayPosition, new Option<>(FpsDisplayPosition, new ElementPosition(5, 5, 1)));
        put(ToggleSneakEnabled, new Option<>(ToggleSneakEnabled, false));
        put(FireworksDisabled, new Option<>(FireworksDisabled, false));
        put(FlyBoostAmount, new Option<>(FlyBoostAmount, 1d));
        put(FlyBoostEnabled, new Option<>(FlyBoostEnabled, false));
        put(DecreaseNetherParticles, new Option<>(DecreaseNetherParticles, false));
        put(BlockOutlineMode, new Option<>(BlockOutlineMode, com.intro.common.config.options.BlockOutlineMode.VANILLA));
        put(BlockOutlineColor, new Option<>(BlockOutlineColor, Colors.TRANSPARENT.getColor()));
        put(BlockOutlineAlpha, new Option<>(BlockOutlineAlpha, 1d));
        put(StatusEffectDisplayMode, new Option<>(StatusEffectDisplayMode, com.intro.common.config.options.StatusEffectDisplayMode.VANILLA));
        put(StatusEffectDisplayPosition, new Option<>(StatusEffectDisplayPosition, new ElementPosition(5, 5, 1)));
        put(MaxStatusEffectsDisplayed, new Option<>(MaxStatusEffectsDisplayed, 1d));
        put(StatusEffectDisplayScale, new Option<>(StatusEffectDisplayScale, 1d));
        put(ArmorDisplayEnabled, new Option<>(ArmorDisplayEnabled, false));
        put(ArmorDisplayPosition, new Option<>(ArmorDisplayPosition, new ElementPosition(5, 5, 1)));
        put(PingDisplayEnabled, new Option<>(PingDisplayEnabled, false));
        put(CpsDisplayEnabled, new Option<>(CpsDisplayEnabled, false));
        put(CpsDisplayPosition, new Option<>(CpsDisplayPosition, new ElementPosition(5, 5, 1)));
        put(PingDisplayPosition, new Option<>(PingDisplayPosition, new ElementPosition(5, 5, 1)));
        put(KeystrokesColor, new Option<>(KeystrokesColor, new Color(0.1f, 0.1f, 0.1f, 0.2f)));
        put(KeystrokesRgb, new Option<>(KeystrokesRgb, false));
        put(KeystrokesPosition, new Option<>(KeystrokesPosition, new ElementPosition(5, 5, 1)));
        put(KeystrokesEnabled, new Option<>(KeystrokesEnabled, false));
        put(KeystrokesAlpha, new Option<>(KeystrokesAlpha, 0.2));
        put(AnimateCapes, new Option<>(AnimateCapes, true));
        put(ShowOtherPlayersCapes, new Option<>(ShowOtherPlayersCapes, true));
        put(SetCape, new Option<>(SetCape, ""));
        put(HypixelApiKey, new Option<>(HypixelApiKey, ""));
        put(LevelHeadEnabled, new Option<>(LevelHeadEnabled, false));
        put(AutoGGString, new Option<>(AutoGGString, "gg"));
        put(AutoGGEnabled, new Option<>(AutoGGEnabled, false));
        put(LevelHeadMode, new Option<>(LevelHeadMode, com.intro.common.config.options.LevelHeadMode.NETWORK_LEVEL));
    }

    public HashMap<String, Option<?>> getValues() {
        return this.options;
    }
}