package com.intro.common.config.options;

public enum LevelHeadMode {

    NETWORK_LEVEL,
    BEDWARS_LEVEL,
    SKYWARS_LEVEL;


    private static final LevelHeadMode[] vals = values();

    public LevelHeadMode next() {
        return vals[(this.ordinal()+1) % vals.length];
    }
}
