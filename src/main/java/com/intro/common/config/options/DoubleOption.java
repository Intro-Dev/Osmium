package com.intro.common.config.options;

import com.intro.client.OsmiumClient;

public class DoubleOption extends Option {

    public double variable;

    public final double def;

    public DoubleOption(String identifier, double variable) {
        super(identifier, "DoubleOption");
        this.variable = variable;
        this.def = variable;
    }

    @Override
    public DoubleOption get() {
        return (DoubleOption) OsmiumClient.options.get(this.identifier);
    }

    @Override
    public void put() {
        OsmiumClient.options.put(this.identifier, this);
    }


}
