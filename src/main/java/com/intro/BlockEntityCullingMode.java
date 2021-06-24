package com.intro;

public enum BlockEntityCullingMode {

    DISABLED,
    LOW,
    MEDIUM,
    HIGH,
    EXTREME;

    private static BlockEntityCullingMode[] vals = values();

    public BlockEntityCullingMode next() {
        return vals[(this.ordinal()+1) % vals.length];
    }

}
