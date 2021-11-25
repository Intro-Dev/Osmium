package com.intro.common.config.options.legacy;

import com.intro.client.render.color.Color;

public class ColorOption extends LegacyOption {

    public Color color;

    public final Color def;

    public ColorOption(String identifier, Color color) {
        super(identifier, "ColorOption");
        this.color = color;
        this.def = color;
    }

    @Override
    public ColorOption get() {
        throw new UnsupportedOperationException("Tried to call an operation on legacy option: " + this);
    }

    @Override
    public void put() {
        throw new UnsupportedOperationException("Tried to call an operation on legacy option: " + this);
    }


}
