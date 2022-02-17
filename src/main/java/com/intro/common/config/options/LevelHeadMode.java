package com.intro.common.config.options;

public enum LevelHeadMode {
    BEDWARS_LEVEL,
    NETWORK_LEVEL,
    SKYWARS_LEVEL;

    private static final LevelHeadMode[] vals = values();

    public LevelHeadMode next() {
        return vals[(this.ordinal()+1) % vals.length];
    }
}
