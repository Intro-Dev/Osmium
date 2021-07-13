package com.intro.config;

import com.intro.Osmium;
import com.intro.render.Color;

public class ColorOption extends Option {

    public Color color;

    public final Color def;

    public ColorOption(String identifier, Color color) {
        super(identifier, "BooleanOption");
        this.color = color;
        this.def = color;
        Osmium.options.put(identifier, this);
    }

    @Override
    public ColorOption get() {
        return (ColorOption) Osmium.options.get(this.identifier);
    }

    @Override
    public void put() {
        Osmium.options.put(this.identifier, this);
    }


}
