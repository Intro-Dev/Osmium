package com.intro.common.config.options;

import com.intro.client.OsmiumClient;

public class EnumOption extends Option {

    public Enum variable;

    public final Enum def;

    public EnumOption(String identifier, Enum option) {
        super(identifier, "EnumOption");
        this.variable = option;
        this.def = variable;
    }

    @Override
    public EnumOption get() {
        return (EnumOption) OsmiumClient.options.get(this.identifier);
    }

    @Override
    public void put() {
        OsmiumClient.options.put(this.identifier, this);
    }
}
