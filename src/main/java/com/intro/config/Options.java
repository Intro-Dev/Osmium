package com.intro.config;

import java.util.HashMap;

public class Options {

    private HashMap<String, Option> options = new HashMap<>();

    public Option get(String identifier) {
        return options.get(identifier);
    }

    public void put(String identifier, Option option) {
        options.put(identifier, option);
    }


    public Option ToggleSprintEnabled;
    public Option FullbrightEnabled;
    public Option HurtbobbingEnabled;
    public Option SneakMode;
    public Option BlockEntityCullingMode;
    public Option NoRainEnabled;
    public Option FpsEnabled;
    public Option CustomCapeMode;
    public Option ZoomMode;
    public Option NoFireEnabled;


    public void init() {
        // Default inits
        // Changed to preferences when config is loaded
        ToggleSprintEnabled = new BooleanOption( "ToggleSprintEnabled", false);
        FullbrightEnabled = new BooleanOption("FullBrightEnabled", false);
        HurtbobbingEnabled = new BooleanOption("HurtBobbingEnabled", false);
        SneakMode = new EnumOption("SneakMode", com.intro.config.SneakMode.VANILLA);
        BlockEntityCullingMode = new EnumOption("BlockEntityCullingMode", com.intro.config.BlockEntityCullingMode.DISABLED);
        NoRainEnabled = new BooleanOption("NoRainEnabled", false);
        FpsEnabled = new BooleanOption("FpsEnabled", false);
        CustomCapeMode = new EnumOption( "CustomCapeMode", CapeRenderingMode.DISABLED);
        ZoomMode = new EnumOption( "ZoomMode", com.intro.config.ZoomMode.DISABLED);
        NoFireEnabled = new BooleanOption("NoFireEnabled", false);
    }

    public void putHashMap() {
        put(ToggleSprintEnabled.identifier, ToggleSprintEnabled);
        put(FullbrightEnabled.identifier, FullbrightEnabled);
        put(HurtbobbingEnabled.identifier, HurtbobbingEnabled);
        put(SneakMode.identifier, SneakMode);
        put(BlockEntityCullingMode.identifier, BlockEntityCullingMode);
        put(NoRainEnabled.identifier, NoRainEnabled);
        put(FpsEnabled.identifier, FpsEnabled);
        put(CustomCapeMode.identifier, CustomCapeMode);
        put(ZoomMode.identifier, ZoomMode);
        put(NoFireEnabled.identifier, NoFireEnabled);
    }

    public void getHashMap() {
        ToggleSprintEnabled = get(ToggleSprintEnabled.identifier);
        FullbrightEnabled = get(FullbrightEnabled.identifier);
        HurtbobbingEnabled = get(HurtbobbingEnabled.identifier);
        SneakMode = get(SneakMode.identifier);
        BlockEntityCullingMode = get(BlockEntityCullingMode.identifier);
        NoRainEnabled = get(NoRainEnabled.identifier);
        FpsEnabled = get(FpsEnabled.identifier);
        CustomCapeMode = get(CustomCapeMode.identifier);
        ZoomMode = get(ZoomMode.identifier);
        NoFireEnabled = get(NoFireEnabled.identifier);

    }
}