package com.intro.config.options;

import com.intro.Osmium;

public class EnumOption extends Option {

    public Enum variable;

    public final Enum def;

    public EnumOption(String identifier, Enum option) {
        super(identifier, "EnumOption");
        this.variable = option;
        this.def = variable;
        Osmium.options.put(identifier, this);
    }

    @Override
    public EnumOption get() {
        return (EnumOption) Osmium.options.get(this.identifier);
    }

    @Override
    public void put() {
        Osmium.options.put(this.identifier, this);
    }
}
