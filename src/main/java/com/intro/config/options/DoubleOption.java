package com.intro.config.options;

import com.intro.Osmium;

public class DoubleOption extends Option {

    public double variable;

    public final double def;

    public DoubleOption(String identifier, double variable) {
        super(identifier, "DoubleOption");
        this.variable = variable;
        this.def = variable;
        Osmium.options.put(identifier, this);
    }

    @Override
    public DoubleOption get() {
        return (DoubleOption) Osmium.options.get(this.identifier);
    }

    @Override
    public void put() {
        Osmium.options.put(this.identifier, this);
    }


}
