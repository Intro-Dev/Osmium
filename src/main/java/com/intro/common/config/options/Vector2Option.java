package com.intro.common.config.options;

import com.intro.client.OsmiumClient;

public class Vector2Option extends Option {

    public final double x;
    public final double y;

    public final double defX;
    public final double defY;

    public Vector2Option(String identifier, double x, double y) {
        super(identifier, "Vector2Option");
        this.x = x;
        this.y = y;
        this.defX = x;
        this.defY = y;
    }

    @Override
    public Vector2Option get() {
        return (Vector2Option) OsmiumClient.options.get(this.identifier);
    }

    @Override
    public void put() {
        OsmiumClient.options.put(this.identifier, this);
    }


}
