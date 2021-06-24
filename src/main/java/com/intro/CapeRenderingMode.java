package com.intro;

public enum CapeRenderingMode {
    DISABLED,
    OPTIFINE,
    ALL;

    private static CapeRenderingMode[] vals = values();

    public CapeRenderingMode next() {
        return vals[(this.ordinal()+1) % vals.length];
    }
}
