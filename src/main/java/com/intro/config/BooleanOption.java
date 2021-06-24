package com.intro.config;

import com.intro.Osmium;

public class BooleanOption extends Option {

    public boolean variable;

    public BooleanOption(String identifier, boolean variable) {
        super(identifier, "BooleanOption");
        this.variable = variable;
    }

    @Override
    public BooleanOption get() {
        return (BooleanOption) Osmium.options.get(this.identifier);
    }

    @Override
    public void put() {
        Osmium.options.put(this.identifier, this);
    }


}
