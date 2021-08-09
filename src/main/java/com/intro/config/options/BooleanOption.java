package com.intro.config.options;

import com.intro.Osmium;

public class BooleanOption extends Option {

    public boolean variable;

    public final boolean def;

    public BooleanOption(String identifier, boolean variable) {
        super(identifier, "BooleanOption");
        this.variable = variable;
        this.def = variable;
        Osmium.options.put(identifier, this);
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
