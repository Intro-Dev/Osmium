package com.intro.config;

public enum SneakMode {
    VANILLA,
    SMOOTH,
    INSTANT;

    private static final SneakMode[] vals = values();

    public SneakMode next() {
        return vals[(this.ordinal()+1) % vals.length];
    }
}
