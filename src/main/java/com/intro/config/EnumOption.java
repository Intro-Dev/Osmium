package com.intro.config;

import com.intro.Osmium;

public class EnumOption extends Option{

    public Enum variable;

    public EnumOption(String identifier, Enum option) {
        super(identifier, "EnumOption");
        this.variable = option;
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
