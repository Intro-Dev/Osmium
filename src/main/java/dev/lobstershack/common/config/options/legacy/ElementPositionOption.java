package dev.lobstershack.common.config.options.legacy;

import dev.lobstershack.client.util.ElementPosition;

public class ElementPositionOption extends LegacyOption {

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
        throw new UnsupportedOperationException("Tried to call an operation on legacy option: " + this);
    }

    @Override
    public void put() {
        throw new UnsupportedOperationException("Tried to call an operation on legacy option: " + this);
    }


}
