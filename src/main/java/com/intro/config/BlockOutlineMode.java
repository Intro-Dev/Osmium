package com.intro.config;

public enum BlockOutlineMode {
    VANILLA,
    LINES,
    QUADS;

    private static final BlockOutlineMode[] vals = values();

    public BlockOutlineMode next() {
        return vals[(this.ordinal()+1) % vals.length];
    }
}
