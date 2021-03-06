package com.intro.common.config.options;

public enum CapeRenderingMode {
    DISABLED,
    OPTIFINE,
    ALL;

    private static final CapeRenderingMode[] vals = values();

    public CapeRenderingMode next() {
        return vals[(this.ordinal()+1) % vals.length];
    }
}
