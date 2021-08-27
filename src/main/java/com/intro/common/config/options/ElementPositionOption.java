package com.intro.common.config.options;

import com.intro.client.OsmiumClient;
import com.intro.client.util.ElementPosition;

public class ElementPositionOption extends Option {

    public ElementPosition elementPosition;

    public ElementPositionOption(String identifier, double x, double y) {
        super(identifier, "ElementPositionOption");
        elementPosition = new ElementPosition((int) x, (int) y, 1f);
    }

    public ElementPositionOption(String identifier, double x, double y, double scale) {
        super(identifier, "ElementPositionOption");
        elementPosition = new ElementPosition((int) x, (int) y, scale);
    }

    public ElementPositionOption(String identifier, ElementPosition position) {
        super(identifier, "ElementPositionOption");
        elementPosition = position;
    }

    @Override
    public ElementPositionOption get() {
        return (ElementPositionOption) OsmiumClient.options.get(this.identifier);
    }

    @Override
    public void put() {
        OsmiumClient.options.put(this.identifier, this);
    }


}
