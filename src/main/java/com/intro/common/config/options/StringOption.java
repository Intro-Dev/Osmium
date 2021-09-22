package com.intro.common.config.options;

import com.intro.client.OsmiumClient;

public class StringOption extends Option {

    public String variable;

    public final String def;

    public StringOption(String identifier, String variable) {
        super(identifier, "StringOption");
        this.variable = variable;
        this.def = variable;
    }

    @Override
    public StringOption get() {
        return (StringOption) OsmiumClient.options.get(this.identifier);
    }

    @Override
    public void put() {
        OsmiumClient.options.put(this.identifier, this);
    }


}
