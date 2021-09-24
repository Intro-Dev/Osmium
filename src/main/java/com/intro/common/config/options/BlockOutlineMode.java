package com.intro.common.config.options;

public enum BlockOutlineMode {
    VANILLA,
    LINES,
    QUADS;

    // TODO work on quads overlay for the next update


    private static final BlockOutlineMode[] vals = values();

    public BlockOutlineMode next() {
        return vals[(this.ordinal()+1) % vals.length];
    }
}
