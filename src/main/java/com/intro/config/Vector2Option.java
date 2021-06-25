package com.intro.config;

import com.intro.Osmium;
import com.intro.Vector2d;

public class Vector2Option extends Option {

    public double x;
    public double y;

    public Vector2Option(String identifier, double x, double y) {
        super(identifier, "Vector2Option");
        this.x = x;
        this.y = y;
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
