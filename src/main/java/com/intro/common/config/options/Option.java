package com.intro.common.config.options;

public abstract class Option {

    public final String identifier;

    public final String type;

    public Option(String identifier, String type) {
        this.identifier = identifier;
        this.type = type;
    }

    public abstract Option get();

    public abstract void put();

}
