package dev.lobstershack.common.config.options.legacy;

public abstract class LegacyOption {

    public final String identifier;

    public final String type;

    public LegacyOption(String identifier, String type) {
        this.identifier = identifier;
        this.type = type;
    }

    public abstract LegacyOption get();

    public abstract void put();



}
