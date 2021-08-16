package com.intro.common.config.options;

import com.intro.client.OsmiumClient;

public class BooleanOption extends Option {

    public boolean variable;

    public final boolean def;

    public BooleanOption(String identifier, boolean variable) {
        super(identifier, "BooleanOption");
        this.variable = variable;
        this.def = variable;
    }

    @Override
    public BooleanOption get() {
        return (BooleanOption) OsmiumClient.options.get(this.identifier);
    }

    @Override
    public void put() {
        OsmiumClient.options.put(this.identifier, this);
    }


}
