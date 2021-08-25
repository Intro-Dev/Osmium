package com.intro.common.config;

import com.intro.client.render.Colors;
import com.intro.common.config.options.*;

import java.util.HashMap;

/**
 * The Options class stores all OsmiumClient options.
 * All options have a String identifier, which is used to get the option from the HashMap
 * On startup, default options are set first, and are then put to the HashMap from an Option array loaded from a json file
 *
 * @since 1.0
 * @author Intro
 */
public class Options {

    /**
     * Cache of all options
     */
    private final HashMap<String, Option> options = new HashMap<>();

    private final HashMap<String, Option> overwrittenOptions = new HashMap<>();

    public HashMap<String, Option> getOptions() {
        return options;
    }

    public void putOverwrittenOption(String key, Option value) {
        overwrittenOptions.put(key, value);
    }

    public HashMap<String, Option> getOverwrittenOptions() {
        return overwrittenOptions;
    }

    public Option getOverwrittenOption(String key) {
        return overwrittenOptions.get(key);
    }

    public void clearOverwrittenOptions() {
        overwrittenOptions.clear();
    }

    public Option get(String identifier) {
        return options.get(identifier);
    }

    public BooleanOption getBooleanOption(String identifier) {
        return (BooleanOption) get(identifier);
    }

    public EnumOption getEnumOption(String identifier) {
        return (EnumOption) get(identifier);
    }

    public DoubleOption getDoubleOption(String identifier) {
        return (DoubleOption) get(identifier);
    }

    public ElementPositionOption getElementPositionOption(String identifier) {
        return (ElementPositionOption) get(identifier);
    }

    public ColorOption getColorOption(String identifier) {
        return (ColorOption) get(identifier);
    };


    public void put(String identifier, Option option) {
        options.put(identifier, option);
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




    public void init() {
        // Default inits
        // Changed to preferences when config is loaded
        this.setDefaults();
    }



    /**
     * Assigns the default settings to the option variables.
     */
    public void setDefaults() {
        put(ToggleSprintEnabled, new BooleanOption(ToggleSprintEnabled, false));
        put(FullbrightEnabled, new BooleanOption(FullbrightEnabled, false));
        put(HurtbobbingEnabled, new BooleanOption(HurtbobbingEnabled, false));
        put(SneakMode, new EnumOption(SneakMode, com.intro.common.config.options.SneakMode.VANILLA));
        put(NoRainEnabled, new BooleanOption(NoRainEnabled, false));
        put(FpsEnabled, new BooleanOption(FpsEnabled, false));
        put(CustomCapeMode, new EnumOption(CustomCapeMode, CapeRenderingMode.DISABLED));
        put(NoFireEnabled, new BooleanOption(NoFireEnabled, false));
        put(ToggleSprintPosition, new ElementPositionOption(ToggleSprintPosition, 5, 5));
        put(FpsDisplayPosition, new ElementPositionOption(FpsDisplayPosition, 5, 5));
        put(ToggleSneakEnabled, new BooleanOption(ToggleSneakEnabled, false));
        put(FireworksDisabled, new BooleanOption(FireworksDisabled, false));
        put(FlyBoostAmount, new DoubleOption(FlyBoostAmount, 1d));
        put(FlyBoostEnabled, new BooleanOption(FlyBoostEnabled, false));
        put(DecreaseNetherParticles, new BooleanOption(DecreaseNetherParticles, false));
        put(BlockOutlineMode, new EnumOption(BlockOutlineMode, com.intro.common.config.options.BlockOutlineMode.VANILLA));
        put(BlockOutlineColor, new ColorOption(BlockOutlineColor, Colors.TRANSPARENT.getColor()));
        put(BlockOutlineAlpha, new DoubleOption(BlockOutlineAlpha, 1d));
        put(StatusEffectDisplayMode, new EnumOption(StatusEffectDisplayMode, com.intro.common.config.options.StatusEffectDisplayMode.VANILLA));
        put(StatusEffectDisplayPosition, new ElementPositionOption(StatusEffectDisplayPosition, 5, 5));
        put(MaxStatusEffectsDisplayed, new DoubleOption(MaxStatusEffectsDisplayed, 1d));
        put(StatusEffectDisplayScale, new DoubleOption(StatusEffectDisplayScale, 1d));
        put(ArmorDisplayEnabled, new BooleanOption(ArmorDisplayEnabled, false));
        put(ArmorDisplayPosition, new ElementPositionOption(ArmorDisplayPosition, 5, 5));
        put(PingDisplayEnabled, new BooleanOption(PingDisplayEnabled, false));
        put(CpsDisplayEnabled, new BooleanOption(CpsDisplayEnabled, false));
        put(CpsDisplayPosition, new ElementPositionOption(CpsDisplayPosition, 5, 5));
        put(PingDisplayPosition, new ElementPositionOption(PingDisplayPosition, 5, 5));
    }

    public HashMap<String, Option> getValues() {
        return this.options;
    }
}