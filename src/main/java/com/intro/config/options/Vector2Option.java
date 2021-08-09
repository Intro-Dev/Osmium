package com.intro.config.options;

import com.intro.Osmium;

public class Vector2Option extends Option {

    public double x;
    public double y;

    public final double defX;
    public final double defY;

    public Vector2Option(String identifier, double x, double y) {
        super(identifier, "Vector2Option");
        this.x = x;
        this.y = y;
        this.defX = x;
        this.defY = y;
        Osmium.options.put(identifier, this);
    }

    @Override
    public Vector2Option get() {
        return (Vector2Option) Osmium.options.get(this.identifier);
    }

    @Override
    public void put() {
        Osmium.options.put(this.identifier, this);
    }


}
