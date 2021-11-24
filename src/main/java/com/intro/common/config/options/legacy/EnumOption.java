package com.intro.common.config.options.legacy;

public class EnumOption extends LegacyOption {

    public Enum variable;

    public final Enum def;

    public EnumOption(String identifier, Enum option) {
        super(identifier, "EnumOption");
        this.variable = option;
        this.def = variable;
    }

    @Override
    public EnumOption get() {
        throw new UnsupportedOperationException("Tried to call an operation on legacy option: " + this);
    }

    @Override
    public void put() {
        throw new UnsupportedOperationException("Tried to call an operation on legacy option: " + this);
    }
}
