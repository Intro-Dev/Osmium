package com.intro.common.config.options;

import com.intro.client.OsmiumClient;
import com.intro.client.render.color.Color;

public class ColorOption extends Option {

    public Color color;

    public final Color def;

    public ColorOption(String identifier, Color color) {
        super(identifier, "ColorOption");
        this.color = color;
        this.def = color;
    }

    @Override
    public ColorOption get() {
        return (ColorOption) OsmiumClient.options.get(this.identifier);
    }

    @Override
    public void put() {
        OsmiumClient.options.put(this.identifier, this);
    }


}
